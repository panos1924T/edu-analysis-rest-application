package gr.pants.pro.edu_analysis.service;

import gr.pants.pro.edu_analysis.core.exceptions.EntityAlreadyExistsException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityInvalidArgumentException;
import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.core.exceptions.FileUploadException;
import gr.pants.pro.edu_analysis.dto.AnalystInsertDTO;
import gr.pants.pro.edu_analysis.dto.AnalystReadOnlyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IAnalystSevice {

    AnalystReadOnlyDTO saveAnalyst(AnalystInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    void saveIdentityNumberFile(UUID uuid, MultipartFile identityFile)
            throws FileUploadException, EntityNotFoundException;

//    AnalystReadOnlyDTO updateAnalyst(AnalystUpdateDTO updateDTO)
//            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    AnalystReadOnlyDTO deleteAnalystByUuid(UUID uuid)
            throws EntityNotFoundException;

    AnalystReadOnlyDTO getAnalystByUuid(UUID uuid)
            throws EntityNotFoundException;

    Page<AnalystReadOnlyDTO> getPaginatedAnalysts(Pageable pageable);
    Page<AnalystReadOnlyDTO> getPaginatedAnalystsByDeletedFalse(Pageable pageable);

    boolean isAnalystExists(UUID uuid);
    boolean isAnalystExistsByEmail(String email);
}
