package lk.bit.web.repository;

import lk.bit.web.entity.Complain;
import lk.bit.web.entity.CustomEntity6;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainRepository extends JpaRepository<Complain, String> {

    @Query(value = "SELECT id FROM complaint ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getComplaintId();

    @Query(value = "SELECT * FROM complaint WHERE is_deleted_by_admin=0 ORDER BY id", nativeQuery = true)
    public List<Complain> getComplaintDetailsByAdminStatus();

    @Query(value = "SELECT * FROM complaint WHERE is_deleted_by_admin=0 AND res_status=? ORDER BY id", nativeQuery = true)
    public List<Complain> getComplaintDetailsByStatus(int status);

    @Query(value = "SELECT C FROM Complain C WHERE C.customer.customerId=?1 order by C.id")
    public List<Complain> getComplainDetailsByCustomerId(String customerId);

    @Query(value = "SELECT COUNT(*) FROM complaint", nativeQuery = true)
    public int getTotalComplainCount();

    @Query(value = "SELECT COUNT(*) FROM complaint WHERE res_status = 0", nativeQuery = true)
    public int getTotalComplainCountByResponseStatus();

    @Query(value = "SELECT C.id AS complainId, C.msgSubject AS complainSubject, C.msgDescription AS complainDesc," +
            "C.createdDateTime AS complainCreatedDate, CS.createdDateTime AS solutionCreatedDate, CS.msgDescription AS solutionDesc," +
            "CS.status AS solutionStatus FROM Complain C INNER JOIN ComplainSolution CS ON C.id = CS.complain.id WHERE C.isDeleteByCustomer=0")
/*@Query(value = "SELECT C.id, C.msg_subject, C.msg_description," +
        "C.created_date_time, CS.created_date_time, CS.msg_description," +
        "CS.status FROM complaint C INNER JOIN complain_solution CS WHERE C.id = CS.complain_id AND C.is_deleted_by_customer=?1",
nativeQuery = true)*/
    public List<CustomEntity6> getAllComplainAndComplainSolutionDetails();

    @Query(value = "SELECT C.id AS complainId, C.msgSubject AS complainSubject, C.msgDescription AS complainDesc," +
            "C.createdDateTime AS complainCreatedDate, CS.createdDateTime AS solutionCreatedDate, CS.msgDescription AS solutionDesc," +
            "CS.status AS solutionStatus FROM Complain C INNER JOIN ComplainSolution CS ON C.id = CS.complain.id WHERE C.id=?1")
    public List<CustomEntity6> getAllComplainAndComplainSolutionDetailsById(String complaintId);
}
