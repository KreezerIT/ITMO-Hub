package kreezerit.Mwebgateway.service;

import kreezerit.Mwebgateway.entity.User;
import kreezerit.Mwebgateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


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

    public void deleteById(Long user_id) {
        userRepository.deleteById(user_id);
    }

    public Long getUserIdByName(String name) {
        Optional<User> user = userRepository.findByUsername(name);

        if (user.isPresent()){
            return user.get().getId();

        }
        return null;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public Long getOwnerIdByUsername(String username) {
        return getUserByUsername(username)
                .map(User::getOwner_id)
                .orElse(null);
    }
}