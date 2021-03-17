package lk.bit.web.repository;

import lk.bit.web.entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReturnRepository extends JpaRepository<Return, String> {

    @Query(value = "SELECT id FROM `return` ORDER BY id DESC LIMIT 1",nativeQuery = true)
    public String getReturnId();
}
