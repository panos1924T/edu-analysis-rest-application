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
            r.name AS periochi,
            t.firstname AS onoma,
            t.lastname AS eponymo,
            pi.identity_number AS identity_number,
            CASE WHEN t.deleted = 1 THEN 'ΔΙΕΓΡΑΜΜΕΝΟΣ' ELSE 'ΕΝΕΡΓΟΣ' END AS katastasi,
            CASE 
                WHEN t.created_at > '2025-01-01' THEN 'ΝΕΟΣ'
                WHEN t.created_at > '2023-01-01' THEN 'ΜΕΣΑΙΟΣ'
                WHEN t.created_at > '2020-01-01' THEN 'ΕΜΠΕΙΡΟΣ'
                ELSE 'ΠΑΛΙΟΣ'
            END AS empeiria
        FROM analysts t
        JOIN personal_information pi ON t.personal_info_id = pi.id
        JOIN firms r ON t.region_id = r.id
        WHERE t.deleted = 0
        ORDER BY t.deleted DESC, r.name
        """, nativeQuery = true)
    List<AnalystStatusReportView> findAllAnalystsReport();
}
