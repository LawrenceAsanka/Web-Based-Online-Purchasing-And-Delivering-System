package lk.bit.web.repository;

import lk.bit.web.entity.ComplainSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainSolutionRepository extends JpaRepository<ComplainSolution, String> {

    @Query(value = "SELECT id FROM complain_solution ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getLastSolutionId();


}
