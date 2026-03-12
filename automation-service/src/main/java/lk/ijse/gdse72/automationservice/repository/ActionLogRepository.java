package lk.ijse.gdse72.automationservice.repository;

import lk.ijse.gdse72.automationservice.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
