package gr.pants.pro.edu_analysis.service;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.core.exceptions.FileUploadException;
import gr.pants.pro.edu_analysis.core.filters.AnalystFilters;
import gr.pants.pro.edu_analysis.dto.AnalystInsertDTO;
import gr.pants.pro.edu_analysis.dto.AnalystReadOnlyDTO;
import gr.pants.pro.edu_analysis.dto.AnalystUpdateDTO;
import gr.pants.pro.edu_analysis.mapper.Mapper;
import gr.pants.pro.edu_analysis.model.*;
import gr.pants.pro.edu_analysis.model.static_data.Firm;
import gr.pants.pro.edu_analysis.repository.*;
import gr.pants.pro.edu_analysis.specification.AnalystSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalystService implements IAnalystSevice{

    private final AnalystRepository analystRepository;
    private final FirmRepository firmRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonalInfoRepository personalInfoRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional(rollbackFor = {EntityInvalidArgumentException.class, EntityAlreadyExistsException.class})
    public AnalystReadOnlyDTO saveAnalyst(AnalystInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {

            if (insertDTO.email() != null && analystRepository.findByEmail(insertDTO.email()).isPresent()) {
                throw new EntityAlreadyExistsException("Analyst", "Analyst with email=" + insertDTO.email() + " already exists.");
            }

            if (personalInfoRepository.findByIdentityNumber(insertDTO.personalInfoInsertDTO().identityNumber()).isPresent()) {
                throw new EntityAlreadyExistsException("IdentityNumber", "Analyst with identity number=" + insertDTO.personalInfoInsertDTO().identityNumber() + " already exists.");
            }

            if (userRepository.findByUsername(insertDTO.userInsertDTO().username()).isPresent()) {
                throw new EntityAlreadyExistsException("Username", "User with username=" + insertDTO.userInsertDTO().username() + " already exists.");
            }

            Firm firm = firmRepository.findById(insertDTO.firmId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Firm", "Firm id=" + insertDTO.firmId() + " invalid."));

            final Long analystRole = 3L;
            Role role = roleRepository.findById(analystRole)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Role", "Role id=" + analystRole + " invalid"));

            Analyst analyst = mapper.toAnalystEntity(insertDTO);
            User user = analyst.getUser();
            user.setPassword(passwordEncoder.encode(insertDTO.userInsertDTO().password()));
            firm.addAnalyst(analyst);
            role.addUser(user);

            analystRepository.save(analyst);
            log.info("Analyst with email={} saved successfully.", insertDTO.email());
            return mapper.toAnalystReadOnlyDTO(analyst);

        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed for Analyst with email={}. Analyst already exists.", insertDTO.email(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Invalid argument for Analyst with email={} and Firm id={}. Save failed.", insertDTO.email(), insertDTO.firmId(), e);
            throw e;
        }

    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_TEACHER')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class, EntityNotFoundException.class})
    public AnalystReadOnlyDTO updateAnalyst(AnalystUpdateDTO updateDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {

        try {

            Analyst analyst = analystRepository.findByUuid(updateDTO.uuid())
                    .orElseThrow(() -> new EntityNotFoundException("Analyst", "Analyst with uuid=" + updateDTO.uuid() + " not found."));

            analyst.setFirstname(updateDTO.firstname());
            analyst.setLastname(updateDTO.lastname());

            if (!analyst.getEmail().equals(updateDTO.email())) {
                if (analystRepository.findByEmail(updateDTO.email()).isPresent()) {
                    throw new EntityAlreadyExistsException("Analyst", "An Analyst with email=" + updateDTO.email() + " already exists");
                }
                analyst.setEmail(updateDTO.email());
            }

            if (!analyst.getPersonalInfo().getIdentityNumber().equals(updateDTO.personalInfoUpdateDTO().identityNumber()) &&
                personalInfoRepository.findByIdentityNumber(updateDTO.personalInfoUpdateDTO().identityNumber()).isPresent()) {
                throw new EntityAlreadyExistsException("Analyst", "An Analyst with identity number=" + updateDTO.personalInfoUpdateDTO().identityNumber() + " already exists");
            }
            analyst.getPersonalInfo().setIdentityNumber(updateDTO.personalInfoUpdateDTO().identityNumber());
            analyst.getPersonalInfo().setPlaceOfBirth(updateDTO.personalInfoUpdateDTO().placeOfBirth());
            analyst.getPersonalInfo().setMunicipalityOfRegistration(updateDTO.personalInfoUpdateDTO().municipalityOfRegistration());

            if (!Objects.equals(updateDTO.firmId(), analyst.getFirm().getId())) {
                Firm firm = firmRepository.findById(updateDTO.firmId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Firm", "Firm with id=" + updateDTO.firmId() + " invalid."));
                Firm oldFirm = analyst.getFirm();
                if (oldFirm != null) {
                    oldFirm.removeAnalyst(analyst);
                }
                firm.addAnalyst(analyst);
            }

            if (!analyst.getUser().getUsername().equals(updateDTO.userUpdateDTO().username())) {
                if (userRepository.findByUsername(updateDTO.userUpdateDTO().username()).isPresent()) {
                    throw new EntityAlreadyExistsException("User", "A user with username=" + updateDTO.userUpdateDTO().username() + " already exists");
                }
            }
            analyst.getUser().setUsername(updateDTO.userUpdateDTO().username());
            analyst.getUser().setPassword(passwordEncoder.encode(updateDTO.userUpdateDTO().password()));

            log.info("Analyst with uuid={} updated successfully.", updateDTO.uuid());
            return mapper.toAnalystReadOnlyDTO(analyst);
        } catch (EntityNotFoundException e) {
            log.error("Update failed for analyst with uuid={}. Analyst not found", updateDTO.uuid(), e);
            throw e;
        } catch (EntityAlreadyExistsException e) {
            log.error("Update failed for analyst with uuid={}. Analyst with email={} already exists", updateDTO.uuid(), updateDTO.email(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Update failed for analyst with uuid={}. Firm id={} is invalid", updateDTO.uuid(), updateDTO.firmId(), e);
            throw e;
        }

    }

    @Override
    @Retryable(
            retryFor = { IOException.class, HttpServerErrorException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2, maxDelay = 10000)
    )
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void saveIdentityNumberFile(UUID uuid, MultipartFile identityFile)
            throws FileUploadException, EntityNotFoundException {
        try {
            String originalFilename = identityFile.getOriginalFilename();
            String savedName = UUID.randomUUID() + getFileExtension(originalFilename);

            String uploadDirectory = uploadDir;
            Path filePath = Paths.get(uploadDirectory + savedName);

            Files.createDirectories(filePath.getParent());
            //        Files.write(filePath, amkaFile.getBytes());
            identityFile.transferTo(filePath);  // safe for large files, more efficient

            Attachment attachment = new Attachment();
            attachment.setFilename(originalFilename);
            attachment.setSavedName(savedName);
            attachment.setFilePath(filePath.toString());

            Tika tika = new Tika();
            String contentType = tika.detect(identityFile.getBytes());
            attachment.setContentType(contentType);
            attachment.setExtension(getFileExtension(originalFilename));

            Analyst analyst = analystRepository.findByUuid(uuid).orElseThrow(()
                    -> new EntityNotFoundException("Analyst", "Analyst with uuid=" + uuid + " not found."));

            PersonalInfo personalInfo = analyst.getPersonalInfo();

            if (personalInfo.getIdentityFile() != null) {
                Files.deleteIfExists(Path.of(personalInfo.getIdentityFile().getFilePath()));
                personalInfo.removeIdentityFile();  // orphanRemoval handles DB deletion
            }

            personalInfo.addIdentityFile(attachment);
            analystRepository.save(analyst);
            log.info("Attachment for analyst with identity number={} saved", personalInfo.getIdentityNumber());
        } catch (EntityNotFoundException e) {
            log.error("Attachment for analyst with uuid={} not found", uuid, e);
            throw e;
        } catch (IOException | HttpServerErrorException e) {
            log.error("Error saving attachment for analyst with uuid={}", uuid, e);
            throw new FileUploadException("File", "Error saving attachment for analyst with identity number=" + uuid);
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }


    @Override
    public AnalystReadOnlyDTO deleteAnalystByUuid(UUID uuid)
            throws EntityNotFoundException {
        return null;
    }

    @Override
    public AnalystReadOnlyDTO getAnalystByUuid(UUID uuid)
            throws EntityNotFoundException {
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_ANALYST') or (hasAuthority('VIEW_ONLY_ANALYST') and @securityService.isOwnAnalystProfile(#uuid, authentication))")
    @Transactional(readOnly = true)
    public AnalystReadOnlyDTO getAnalystByUuidAndDeletedFalse(UUID uuid) throws EntityNotFoundException {

        try {
            Analyst analyst = analystRepository.findByUuidAndDeletedFalse(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Analyst", "Analyst with uuid=" + uuid + " not found."));
            log.debug("Get non-deleted analyst by uuid={} returned successfully.", uuid);
            return mapper.toAnalystReadOnlyDTO(analyst);
        } catch (EntityNotFoundException e) {
            log.error("Get analyst by uuid={} failed.", uuid);
            throw e;
        }
    }

    @Override
    public Page<AnalystReadOnlyDTO> getPaginatedAnalysts(Pageable pageable) {
        return null;
    }

    @Override
    public Page<AnalystReadOnlyDTO> getPaginatedAnalystsByDeletedFalse(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAnalystExists(UUID uuid) {
        return analystRepository.findByUuid(uuid).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAnalystExistsByEmail(String email) {
        return analystRepository.findByEmail(email).isPresent();
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_ANALYSTS')")
    @Transactional(readOnly = true)
    public Page<AnalystReadOnlyDTO> getPaginatedAnalystsFiltered(Pageable pageable, AnalystFilters filters)
            throws EntityNotFoundException {
        try {
            if (filters.getUuid() != null) {
                Analyst analyst = analystRepository.findByUuid(filters.getUuid())
                        .orElseThrow(() -> new EntityNotFoundException("Analyst", "uuid=" + filters.getUuid() + " not found"));
                return singleResultPage(analyst, pageable);
            }

            if (filters.getEmail() != null) {
                Analyst analyst = analystRepository.findByEmail(filters.getEmail())
                        .orElseThrow(() -> new EntityNotFoundException("Analyst", "email=" + filters.getEmail() + " not found"));
                return singleResultPage(analyst, pageable);
            }

            if (filters.getEmail() != null) {
                Analyst analyst = analystRepository.findByPersonalInfo_IdentityNumber(filters.getIdentityNumber())
                        .orElseThrow(() -> new EntityNotFoundException("Analyst", "identity-number=" + filters.getIdentityNumber() + " not found"));
                return singleResultPage(analyst, pageable);
            }

            var filtered = analystRepository.findAll(AnalystSpecification.build(filters), pageable);

            log.debug("Filtered and paginated analysts were returned successfully with page={} and size={}", pageable.getPageNumber(),
                    pageable.getPageSize());
            return filtered.map(mapper::toAnalystReadOnlyDTO);
        } catch (EntityNotFoundException e) {
            log.error("Filtered and paginated analysts were not found", e);
            throw e;
        }
    }

    private Page<AnalystReadOnlyDTO> singleResultPage(Analyst analyst, Pageable pageable) {
        return new PageImpl<>(
                List.of(mapper.toAnalystReadOnlyDTO(analyst)),
                pageable,
                1
        );
    }
}
