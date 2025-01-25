package uz.muydinov.secureCap.service;

import uz.muydinov.secureCap.domain.User;
import uz.muydinov.secureCap.dto.UserDTO;

public interface UserService {
    UserDTO createUser(User user);
}
