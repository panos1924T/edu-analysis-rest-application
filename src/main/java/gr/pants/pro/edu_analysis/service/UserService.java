package gr.pants.pro.edu_analysis.service;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.dto.UserInsertDTO;
import gr.pants.pro.edu_analysis.dto.UserReadOnlyDTO;
import gr.pants.pro.edu_analysis.mapper.Mapper;
import gr.pants.pro.edu_analysis.model.Role;
import gr.pants.pro.edu_analysis.model.User;
import gr.pants.pro.edu_analysis.repository.RoleRepository;
import gr.pants.pro.edu_analysis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j //TODO add logging
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final Mapper mapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {

            if (userRepository.findByUsername(userInsertDTO.username()).isPresent()) {
                throw new EntityAlreadyExistsException("User", "User with username=" + userInsertDTO.username() + " already exists.");
            }
            User user = mapper.toUserEntity(userInsertDTO);
            user.setPassword(passwordEncoder.encode(userInsertDTO.password()));

            Role role = roleRepository.findById(userInsertDTO.roleId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Role", "Role with id=" + userInsertDTO.roleId() + " invalid."));
            role.addUser(user);

            userRepository.save(user);
            log.info("User with username={} saved successfully!", userInsertDTO.username());
            return mapper.toReadOnlyDTO(user);

        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed! User with username={} already exists.", userInsertDTO.username());
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Save failed! Invalid arguments for user with username={}", userInsertDTO.username());
            throw e;
        }
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUuid(UUID uuid) throws EntityNotFoundException {

        try {

            User user = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User with uuid=" + uuid + " not found."));
            log.debug("User with uuid={} found successfully!", uuid);

            return mapper.toReadOnlyDTO(user);

        } catch (EntityNotFoundException e) {
            log.error("Get failed! User with uuid={} not found.", uuid);
            throw e;
        }

    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException {

        try {

            User user = userRepository.findByUuidAndDeletedFalse(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User with uuid=" + uuid + " not found."));
            log.debug("User with uuid={} found successfully!", uuid);

            return mapper.toReadOnlyDTO(user);

        } catch (EntityNotFoundException e) {
            log.error("Get failed! User with uuid={} not found.", uuid);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
