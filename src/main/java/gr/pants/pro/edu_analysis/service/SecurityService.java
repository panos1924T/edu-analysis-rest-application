package gr.pants.pro.edu_analysis.service;

import gr.pants.pro.edu_analysis.model.User;
import gr.pants.pro.edu_analysis.repository.AnalystRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@Slf4j
public class SecurityService {

    @Autowired
    private AnalystRepository analystRepository;

    public boolean isOwnAnalystProfile(UUID analystUuid, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        // Find the teacher record and check if its user uuid matches the logged-in user
        return analystRepository.existsByUuidAndUser_Uuid(analystUuid, principal.getUuid());
    }
}
