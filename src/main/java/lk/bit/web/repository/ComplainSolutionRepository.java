package lk.bit.web.repository;

import lk.bit.web.entity.ComplainSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainSolutionRepository extends JpaRepository<ComplainSolution, String> {

    @Query(value = "SELECT id FROM complain_solution ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getLastSolutionId();

    @Query(value = "SELECT COUNT(*) FROM complain_solution WHERE status=0", nativeQuery = true)
    public int getUnReadMsgCount();

    @Query(value = "SELECT * FROM complain_solution WHERE complain_id=?1", nativeQuery = true)
    public ComplainSolution getComplainSolutionByComplainId(String complainId);

}
