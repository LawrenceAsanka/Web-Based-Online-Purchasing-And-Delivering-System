package lk.bit.web.api;

import lk.bit.web.business.custom.AdvertisementBO;
import lk.bit.web.util.tm.AdvertisementTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/advertisements")
public class AdvertisementController {

    @Autowired
    private AdvertisementBO advertisementBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<AdvertisementTM> getAllAdsDetails(){
        return advertisementBO.getAllAdsDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    private List<AdvertisementTM> getAllLastFourAds(){
        return advertisementBO.getAdvertisementDetail();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private void saveAdvertisement(@RequestPart("image")MultipartFile image,
                                   @RequestParam("data") String advertisementName){
        if (image.isEmpty() || advertisementName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        advertisementBO.saveAdvertisement(image,advertisementName);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value= "/{id}")
    private void updateAdsStatus(@PathVariable("id") int adsId,
                                 @RequestParam String status){
        advertisementBO.updateAdsStatus(adsId,status);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value= "/{id}")
    private void deleteAd(@PathVariable("id") int adId){
        advertisementBO.deleteAd(adId);
    }
}
