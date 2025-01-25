package uz.muydinov.secureCap.service.implementation;

import org.springframework.stereotype.Service;
import uz.muydinov.secureCap.domain.User;
import uz.muydinov.secureCap.dto.UserDTO;
import uz.muydinov.secureCap.dtoMapper.UserDTOMapper;
import uz.muydinov.secureCap.repository.UserRepository;
import uz.muydinov.secureCap.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    public UserServiceImpl(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }
}
