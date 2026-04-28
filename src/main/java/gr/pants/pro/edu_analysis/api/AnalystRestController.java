package gr.pants.pro.edu_analysis.api;

import gr.pants.pro.edu_analysis.core.exceptions.*;
import gr.pants.pro.edu_analysis.core.filters.AnalystFilters;
import gr.pants.pro.edu_analysis.dto.AnalystInsertDTO;
import gr.pants.pro.edu_analysis.dto.AnalystReadOnlyDTO;
import gr.pants.pro.edu_analysis.dto.AnalystUpdateDTO;
import gr.pants.pro.edu_analysis.service.IAnalystSevice;
import gr.pants.pro.edu_analysis.validator.AnalystInsertValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analysts")
@RequiredArgsConstructor
public class AnalystRestController {

    private final IAnalystSevice analystSevice;
    private final AnalystInsertValidator analystInsertValidator;

    @PostMapping
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

    @PostMapping("/{uuid}/identity-file")
    public ResponseEntity<Void> uploadIdentityFile(
            @PathVariable UUID uuid,
            @RequestParam("identityFile") MultipartFile identityFile
            ) throws EntityNotFoundException, FileUploadException {

        analystSevice.saveIdentityNumberFile(uuid, identityFile);
        return ResponseEntity
                .noContent()
                .build();

    }

    @PutMapping("/{uuid}")
    public ResponseEntity<AnalystReadOnlyDTO> updateAnalyst(
            @PathVariable UUID uuid,
            @Valid @RequestBody AnalystUpdateDTO updateDTO,
            BindingResult bindingResult)
            throws EntityNotFoundException, EntityInvalidArgumentException, EntityAlreadyExistsException, ValidationException {

        //analystUpdateValidator.validate(analystUpdateValidator, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Analyst", "Invalid analyst data", bindingResult);
        }

        AnalystReadOnlyDTO analystReadOnlyDTO0 = analystSevice.updateAnalyst(updateDTO);
        return ResponseEntity.ok(analystReadOnlyDTO0);
    }

    @GetMapping
    public ResponseEntity<Page<AnalystReadOnlyDTO>> getFilteredAndPaginatedAnalysts(
            @PageableDefault(page = 0, size = 5)Pageable pageable,
            @ModelAttribute AnalystFilters filters
            ) throws EntityNotFoundException {

        Page<AnalystReadOnlyDTO> paginatedDTO = analystSevice.getPaginatedAnalystsFiltered(pageable, filters);
        return ResponseEntity.ok(paginatedDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AnalystReadOnlyDTO> getAnalystByUuid(
            @PathVariable UUID uuid
    ) throws EntityNotFoundException {

        AnalystReadOnlyDTO analystReadOnlyDTO = analystSevice.getAnalystByUuidAndDeletedFalse(uuid);
        return ResponseEntity.ok(analystReadOnlyDTO);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<AnalystReadOnlyDTO> deleteAnalyst(
            @PathVariable UUID uuid)
            throws EntityNotFoundException {

        AnalystReadOnlyDTO analystReadOnlyDTO = analystSevice.deleteAnalystByUuid(uuid);
        return ResponseEntity.ok(analystReadOnlyDTO);
    }
}
