package uz.muydinov.secureCap.dtoMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uz.muydinov.secureCap.domain.User;
import uz.muydinov.secureCap.dto.UserDTO;

@Component
public class UserDTOMapper {
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
