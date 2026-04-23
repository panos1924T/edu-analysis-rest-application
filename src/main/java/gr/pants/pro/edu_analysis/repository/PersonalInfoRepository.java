package gr.pants.pro.edu_analysis.repository;

import gr.pants.pro.edu_analysis.model.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long>,
        JpaSpecificationExecutor<PersonalInfo> {

    Optional<PersonalInfo> findByIdentityNumber(String identityNumber);
}
