package gr.pants.pro.edu_analysis.service;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.core.exceptions.FileUploadException;
import gr.pants.pro.edu_analysis.dto.AnalystInsertDTO;
import gr.pants.pro.edu_analysis.dto.AnalystReadOnlyDTO;
import gr.pants.pro.edu_analysis.mapper.Mapper;
import gr.pants.pro.edu_analysis.model.Analyst;
import gr.pants.pro.edu_analysis.model.Role;
import gr.pants.pro.edu_analysis.model.User;
import gr.pants.pro.edu_analysis.model.static_data.Firm;
import gr.pants.pro.edu_analysis.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
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
    public void saveIdentityNumberFile(UUID uuid, MultipartFile identityNumberFile)
            throws FileUploadException, EntityNotFoundException {

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
    public Page<AnalystReadOnlyDTO> getPaginatedAnalysts(Pageable pageable) {
        return null;
    }

    @Override
    public Page<AnalystReadOnlyDTO> getPaginatedAnalystsByDeletedFalse(Pageable pageable) {
        return null;
    }

    @Override
    public Boolean isAnalystExists(UUID uuid) {
        return null;
    }
}
