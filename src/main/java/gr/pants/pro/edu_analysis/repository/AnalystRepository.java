package gr.pants.pro.edu_analysis.repository;

import gr.pants.pro.edu_analysis.dto.AnalystStatusReportView;
import gr.pants.pro.edu_analysis.model.Analyst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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

    @Query(value = """
        SELECT
            f.name AS firm,
            a.firstname,
            a.lastname,
            pi.identity_number AS idNumber,
            CASE WHEN a.deleted = true THEN 'DELETED' ELSE 'ACTIVE' END AS status,
            CASE 
                WHEN a.created_at > '2025-01-01' THEN 'NEW'
                WHEN a.created_at > '2023-01-01' THEN 'INTERMEDIATE'
                WHEN a.created_at > '2020-01-01' THEN 'EXPERIENCED'
                ELSE 'VETERAN'
            END AS experience
        FROM analysts a
        JOIN personal_information pi ON a.personal_info_id = pi.id
        JOIN firms f ON a.firm_id = f.id
        WHERE a.deleted = false
        ORDER BY a.deleted DESC, f.name
        """, nativeQuery = true)
    List<AnalystStatusReportView> findAllAnalystsReport();
}
