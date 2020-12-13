package lk.bit.web.api;

import lk.bit.web.business.custom.AdvertisementBO;
import lk.bit.web.util.AdvertisementTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private void saveAdvertisement(@RequestPart("image")MultipartFile image,
                                   @RequestParam("data") String advertisementName){
        if (image.isEmpty() || advertisementName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        advertisementBO.saveAdvertisement(image,advertisementName);
    }
}
