package lk.bit.web.repository;

import lk.bit.web.entity.Complain;
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
}
