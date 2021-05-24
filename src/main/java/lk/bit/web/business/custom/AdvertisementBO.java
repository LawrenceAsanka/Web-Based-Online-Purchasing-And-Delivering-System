package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.util.tm.AdvertisementTM;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertisementBO extends SuperBO {

    List<AdvertisementTM> getAllAdsDetails();

    void saveAdvertisement(MultipartFile image,String advertisementName);

    void updateAdsStatus(int id,String status);

    List<AdvertisementTM> getAdvertisementDetail();

    void deleteAd(int id);
}
