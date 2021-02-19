package lk.bit.web.repository;

import lk.bit.web.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, String> {

    @Query(value = "SELECT id FROM complaint ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getComplaintId();
}
