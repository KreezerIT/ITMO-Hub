package kreezerit.Mwebgateway.service;

import kreezerit.Mcommon.dto.rpc.owner.OwnerResponse;
import kreezerit.Mwebgateway.clientsrpc.OwnerServiceRpcClient;
import kreezerit.Mwebgateway.dto.CreateUserRequest;
import kreezerit.Mwebgateway.dto.UserDto;
import kreezerit.Mwebgateway.entity.User;
import kreezerit.Mwebgateway.util.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OwnerServiceRpcClient ownerRpcClient;

    public UserDto registerUserWithOwner(CreateUserRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        try {
            OwnerResponse response = ownerRpcClient.createOwnerForUsername(request.getUsername());

            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(Set.of(RoleType.ROLE_USER))
                    .owner_id(response.getId())
                    .build();

            userService.save(user);

            return userToDto(user);

        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать owner (AuthService)", e);
        }
    }


    public Map<String, String> logout() {
        return Map.of("message", "Logged out successfully. Remove token");
    }

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}
