package gr.pants.pro.edu_analysis.api;

import gr.pants.pro.edu_analysis.authentication.AuthenticationService;
import gr.pants.pro.edu_analysis.dto.AuthenticationRequestDTO;
import gr.pants.pro.edu_analysis.dto.AuthenticationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO requestDTO) {
        AuthenticationResponseDTO responseDTO = authenticationService.authenticate(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
