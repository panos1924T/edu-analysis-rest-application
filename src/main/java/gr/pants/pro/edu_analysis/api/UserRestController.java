package gr.pants.pro.edu_analysis.api;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.core.exceptions.ValidationException;
import gr.pants.pro.edu_analysis.dto.UserInsertDTO;
import gr.pants.pro.edu_analysis.dto.UserReadOnlyDTO;
import gr.pants.pro.edu_analysis.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserReadOnlyDTO> registerUser(@Valid @RequestBody UserInsertDTO userInsertDTO,
                                                        BindingResult bindingResult)
            throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("User", "Invalid user data", bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(userReadOnlyDTO.uuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(userReadOnlyDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserReadOnlyDTO> getUserByUuid(@PathVariable UUID uuid)
            throws EntityNotFoundException {

        return ResponseEntity.ok(userService.getUserByUuidDeletedFalse(uuid));

    }
}
