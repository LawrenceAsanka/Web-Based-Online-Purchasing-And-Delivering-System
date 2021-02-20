package lk.bit.web.api;

import lk.bit.web.business.custom.ComplainBO;
import lk.bit.web.business.custom.ComplainSolutionBO;
import lk.bit.web.dto.ComplainSolutionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/solutions")
public class ComplainSolutionController {
    @Autowired
    private ComplainSolutionBO complainSolutionBO;
    @Autowired
    private ComplainBO complainBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void saveComplainSolution(@RequestBody ComplainSolutionDTO complainSolutionDTO){
        if (!complainBO.IsComplaintExist(complainSolutionDTO.getComplaintId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        complainSolutionBO.save(complainSolutionDTO);
    }
}
