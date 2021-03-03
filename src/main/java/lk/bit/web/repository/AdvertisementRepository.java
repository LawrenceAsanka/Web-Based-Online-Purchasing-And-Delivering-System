package lk.bit.web.repository;

import lk.bit.web.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement,Integer> {

    @Query(value = "SELECT * FROM advertisement WHERE status='ACTIVE' ORDER BY adver_id DESC LIMIT 4", nativeQuery = true)
    public List<Advertisement> getAdvertisementDetail();
}
