package security.service;

import lombok.RequiredArgsConstructor;
import model.entity.Owner;
import model.util.RoleType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dto.CreateUserRequest;
import security.dto.UserDto;
import security.entity.User;
import service.OwnerService;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final OwnerService ownerService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto registerUserWithOwner(CreateUserRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Owner owner = ownerService.createOwnerForUser(request.getUsername());

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(RoleType.ROLE_USER))
                .owner(owner)
                .build();

        userService.save(user);
        return userToDto(user);
    }

    public Map<String, String> logout() {
        return Map.of("message", "Logged out successfully. Remove token");
    }

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}

