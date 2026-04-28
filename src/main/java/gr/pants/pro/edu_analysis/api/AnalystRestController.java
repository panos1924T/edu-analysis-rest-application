package gr.pants.pro.edu_analysis.api;

import gr.pants.pro.edu_analysis.core.exceptions.*;
import gr.pants.pro.edu_analysis.core.filters.AnalystFilters;
import gr.pants.pro.edu_analysis.dto.*;
import gr.pants.pro.edu_analysis.service.IAnalystSevice;
import gr.pants.pro.edu_analysis.validator.AnalystInsertValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Save an analyst",
            description = "Registers a new analyst in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Analyst created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalystReadOnlyDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "409", description = "Analyst already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponseDTO.class))
                    )
            }
    )
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

    @Operation(
            summary = "Upload identity file for an analyst",
            description = "Uploads an analyst's identity document file. Replaces existing file if present."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "File uploaded successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Analyst not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "File upload failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
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

    @Operation(summary = "Update an analyst")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Analyst updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalystReadOnlyDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "Analyst already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Analyst not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401", description = "Not Authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access Denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
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

    @Operation(summary = "Get all analysts paginated and filtered")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Analysts returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access Denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<AnalystReadOnlyDTO>> getFilteredAndPaginatedAnalysts(
            @PageableDefault(page = 0, size = 5)Pageable pageable,
            @ModelAttribute AnalystFilters filters
            ) throws EntityNotFoundException {

        Page<AnalystReadOnlyDTO> paginatedDTO = analystSevice.getPaginatedAnalystsFiltered(pageable, filters);
        return ResponseEntity.ok(paginatedDTO);
    }

    @Operation(summary = "Get one analyst by uuid")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Analyst returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalystReadOnlyDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Analyst not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401", description = "Not Authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access Denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
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
