package gr.pants.pro.edu_analysis.mapper;

import gr.pants.pro.edu_analysis.dto.UserInsertDTO;
import gr.pants.pro.edu_analysis.dto.UserReadOnlyDTO;
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
}
