package lk.ijse.gdse72.cropservice.repository;

import lk.ijse.gdse72.cropservice.entity.CropBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropBatchRepository extends JpaRepository<CropBatch, Long> {
}
