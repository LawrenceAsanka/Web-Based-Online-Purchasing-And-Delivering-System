package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.AdvertisementBO;
import lk.bit.web.entity.Advertisement;
import lk.bit.web.repository.AdvertisementRepository;
import lk.bit.web.util.tm.AdvertisementTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class AdvertisementBOImpl implements AdvertisementBO {

    @Autowired
    private Environment env;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public List<AdvertisementTM> getAllAdsDetails(){
        List<AdvertisementTM> adsDetails = new ArrayList<>();
        List<Advertisement> details = advertisementRepository.findAll();
        for (Advertisement detail : details) {

            // get differences between dates
            Date createdDate = detail.getCreatedDate();
            long diffInMillis = Math.abs(new Date().getTime() - createdDate.getTime());
            long diffDate = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

            adsDetails.add(new AdvertisementTM(
                   detail.getAdvertisementId(),detail.getAdvertisementName(),detail.getAdvertisementImage(),
                   diffDate,createdDate.toString(), detail.getStatus()
           ));
        }
        return adsDetails;
    }

    @Override
    public void saveAdvertisement(MultipartFile image, String advertisementName) {

        // check whether folder is exist or not
        String uploadDir = env.getProperty("static.path") + "ads/" + LocalDate.now();
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        // save data into database
        advertisementRepository.save(new Advertisement(
                advertisementName, image.getOriginalFilename(), new Date(), "ACTIVE"
        ));

        // save image in locally
        try {
            image.transferTo(new File(file.getAbsolutePath() + "/" + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAdsStatus(int id,String status) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        ad.ifPresent(advertisement -> advertisement.setStatus(status));
        advertisementRepository.save(ad.get());
    }

    @Override
    public List<AdvertisementTM> getAdvertisementDetail() {
        List<Advertisement> advertisementDetail = advertisementRepository.getAdvertisementDetail();
        List<AdvertisementTM> advertisementTMList = new ArrayList<>();

        for (Advertisement advertisement : advertisementDetail) {
            AdvertisementTM advertisementTM = new AdvertisementTM();

            advertisementTM.setAdsId(advertisement.getAdvertisementId());
            advertisementTM.setAdsName(advertisement.getAdvertisementName());
            advertisementTM.setAdsImage(advertisement.getAdvertisementImage());
            advertisementTM.setCreatedDate(advertisement.getCreatedDate().toString());

            advertisementTMList.add(advertisementTM);
        }
        return advertisementTMList;
    }
}
