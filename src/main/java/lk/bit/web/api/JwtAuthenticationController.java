package lk.bit.web.api;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.business.custom.SystemUserBO;
import lk.bit.web.config.JwtTokenUtil;
import lk.bit.web.dto.AuthenticationRequestDTO;
import lk.bit.web.dto.AuthenticationResponseDTO;
import lk.bit.web.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/authenticate")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomerBO customerBO;

    @Autowired
    private SystemUserBO systemUserBO;

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequest) throws Exception {

        UserDetails userDetails = null;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                            authenticationRequest.getPassword()));
        }catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid username or password", e);
        }

        if (authenticationRequest.getRole() == Role.ROLE_CUSTOMER) {
            userDetails = customerBO.loadUserByUsername(authenticationRequest.getUserName());
        } else {
            userDetails = systemUserBO.loadUserByUsername(authenticationRequest.getUserName());
        }

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }

}
