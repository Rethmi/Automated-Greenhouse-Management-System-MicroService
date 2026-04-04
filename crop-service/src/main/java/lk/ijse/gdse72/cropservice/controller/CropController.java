package lk.ijse.gdse72.cropservice.controller;

import lk.ijse.gdse72.cropservice.entity.CropBatch;
import lk.ijse.gdse72.cropservice.entity.CropStatus;
import lk.ijse.gdse72.cropservice.repository.CropBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    @Autowired
    private CropBatchRepository cropBatchRepository;

    @PostMapping
    public ResponseEntity<CropBatch> registerBatch(@RequestBody CropBatch batch) {
        if (batch.getStatus() == null) {
            batch.setStatus(CropStatus.SEEDLING); // Default state
        }
        CropBatch saved = cropBatchRepository.save(batch);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CropBatch>> getAllBatches() {
        return ResponseEntity.ok(cropBatchRepository.findAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CropBatch> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> payload) {
        return cropBatchRepository.findById(id).map(batch -> {
            try {
                String newStatusStr = payload.get("status");
                if (newStatusStr != null) {
                    CropStatus newStatus = CropStatus.valueOf(newStatusStr.toUpperCase());
                    
                    // Optional State Machine Validation:
                    if (batch.getStatus() == CropStatus.HARVESTED && newStatus != CropStatus.HARVESTED) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot move backwards from HARVESTED");
                    }
                    
                    batch.setStatus(newStatus);
                    return ResponseEntity.ok(cropBatchRepository.save(batch));
                }
                return ResponseEntity.badRequest().body(batch);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status. Must be SEEDLING, VEGETATIVE, or HARVESTED");
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
