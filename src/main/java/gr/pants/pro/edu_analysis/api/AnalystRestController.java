package gr.pants.pro.edu_analysis.api;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.ValidationException;
import gr.pants.pro.edu_analysis.dto.AnalystInsertDTO;
import gr.pants.pro.edu_analysis.dto.AnalystReadOnlyDTO;
import gr.pants.pro.edu_analysis.service.IAnalystSevice;
import gr.pants.pro.edu_analysis.validator.AnalystInsertValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/analysts")
@RequiredArgsConstructor
public class AnalystRestController {

    private final IAnalystSevice analystSevice;
    private final AnalystInsertValidator analystInsertValidator;

    public ResponseEntity<AnalystReadOnlyDTO> saveAnalyst(
            @Valid @RequestBody AnalystInsertDTO insertDTO,
            BindingResult bindingResult)
                throws EntityAlreadyExistsException, EntityInvalidArgumentException, ValidationException {

        analystInsertValidator.validate(insertDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Analyst", "Invalid analyst data", bindingResult);
        }

        AnalystReadOnlyDTO readOnlyDTO = analystSevice.saveAnalyst(insertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(readOnlyDTO.uuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(readOnlyDTO);
    }
}
