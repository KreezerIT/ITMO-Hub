package security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import security.dao.UserRepository;
import security.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}