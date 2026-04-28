package gr.pants.pro.edu_analysis.repository;

import gr.pants.pro.edu_analysis.model.Analyst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AnalystRepository extends JpaRepository<Analyst, Long>,
        JpaSpecificationExecutor<Analyst> {

    Optional<Analyst> findByUuid(UUID uuid);
    Optional<Analyst> findByUuidAndDeletedFalse(UUID uuid);

    Optional<Analyst> findByEmail(String email);
    Optional<Analyst> findByEmailAndDeletedFalse(String email);

    Optional<Analyst> findByPersonalInfo_IdentityNumber(String identityNumber);

    @EntityGraph(attributePaths = {"personalInfo", "firm"})
    Page<Analyst> findByDeletedFalse(Pageable pageable);

    boolean existsByUuidAndUser_Uuid(UUID analystUuid, UUID userUuid);
}
