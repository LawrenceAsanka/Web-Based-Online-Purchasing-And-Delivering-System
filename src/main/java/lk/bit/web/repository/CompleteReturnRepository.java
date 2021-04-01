package lk.bit.web.repository;

import lk.bit.web.dto.CompleteReturnDTO;
import lk.bit.web.entity.CompleteReturn;
import lk.bit.web.entity.CustomEntity12;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompleteReturnRepository extends JpaRepository<CompleteReturn, String> {

    @Query(value = "SELECT id FROM complete_return ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getCompleteReturnId();

    @Query(value = "SELECT AR.return_id AS returnId, AR.assigned_date_time AS assignedDateTime, " +
            "CR.returned_date_time AS completedDateTime, R.order_id AS orderId FROM complete_return CR " +
            "INNER JOIN assigned_return AR on CR.assign_return_id = AR.id " +
            "INNER JOIN `return` R on AR.return_id = R.id WHERE assignee_id=?1 AND status=4", nativeQuery = true)
    public List<CustomEntity12> readAllCompleteReturnDetailsByAssignee(String assigneeId);

    @Query(value = "SELECT * FROM complete_return WHERE assign_return_id = ?1", nativeQuery = true)
    public CompleteReturn readCompleteReturnByAssignReturnId(int AssignReturnId);
}