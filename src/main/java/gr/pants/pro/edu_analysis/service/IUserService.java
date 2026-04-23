package gr.pants.pro.edu_analysis.service;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.dto.UserInsertDTO;
import gr.pants.pro.edu_analysis.dto.UserReadOnlyDTO;

import java.util.UUID;

public interface IUserService {

    UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    UserReadOnlyDTO getUserByUuid(UUID uuid)
            throws EntityNotFoundException;

    UserReadOnlyDTO getUserByUuidDeletedFalse(UUID uuid)
            throws EntityNotFoundException;
}
