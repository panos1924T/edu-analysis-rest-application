package gr.pants.pro.edu_analysis.mapper;

import gr.pants.pro.edu_analysis.dto.*;
import gr.pants.pro.edu_analysis.model.Analyst;
import gr.pants.pro.edu_analysis.model.PersonalInfo;
import gr.pants.pro.edu_analysis.model.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public User toUserEntity(UserInsertDTO userInsertDTO) {
        return new User(userInsertDTO.username(), userInsertDTO.password());
    }

    public UserReadOnlyDTO toReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getUuid().toString(), user.getUsername(), user.getRole().getName());
    }

    public Analyst toAnalystEntity(AnalystInsertDTO analystInsertDTO) {
        Analyst analyst = new Analyst();
        analyst.setFirstname(analystInsertDTO.firstname());
        analyst.setLastname(analystInsertDTO.lastname());
        analyst.setEmail(analystInsertDTO.email());

        UserInsertDTO userDTO = analystInsertDTO.userInsertDTO();
        User user = new User();
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        analyst.addUser(user);

        PersonalInfoInsertDTO personalInfoInsertDTO = analystInsertDTO.personalInfoInsertDTO();
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setIdentityNumber(personalInfoInsertDTO.identityNumber());
        personalInfo.setPlaceOfBirth(personalInfoInsertDTO.placeOfBirth());
        personalInfo.setMunicipalityOfRegistration(personalInfoInsertDTO.municipalityOfRegistration());
        analyst.setPersonalInfo(personalInfo);

        return analyst;
    }

    public AnalystReadOnlyDTO toAnalystReadOnlyDTO(Analyst analyst) {
        return new  AnalystReadOnlyDTO(
                analyst.getUuid().toString(),
                analyst.getFirstname(),
                analyst.getLastname(),
                analyst.getEmail(),
                analyst.getFirm().getName());
    }

}
