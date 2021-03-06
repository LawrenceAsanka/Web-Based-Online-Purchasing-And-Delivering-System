package lk.bit.web.repository;

import lk.bit.web.entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReturnRepository extends JpaRepository<Return, String> {

    @Query(value = "SELECT id FROM `return` ORDER BY id DESC LIMIT 1",nativeQuery = true)
    public String getReturnId();

    @Query(value = "SELECT * FROM `return`WHERE status=0 ORDER BY id", nativeQuery = true)
    public List<Return> readAllByStatus();

    @Query(value = "SELECT * FROM `return`WHERE status=1 ORDER BY id", nativeQuery = true)
    public List<Return> readAllByStatusCancel();

    @Query(value = "SELECT * FROM `return`WHERE status=2 ORDER BY id", nativeQuery = true)
    public List<Return> readAllByStatusConfirm();

    @Query(value = "SELECT * FROM `return`WHERE status=3 ORDER BY id",nativeQuery = true)
    public List<Return> readAllByStatusProcessing();

    @Query(value = "SELECT * FROM `return`WHERE status=4 ORDER BY id",nativeQuery = true)
    public List<Return> readAllByStatusComplete();

    @Query(value = "SELECT COUNT(*) FROM `return`WHERE status=0", nativeQuery = true)
    public int readNewReturnCount();
}
