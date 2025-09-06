package kreezerit.Mwebgateway.service;


import kreezerit.Mwebgateway.clientsrpc.OwnerServiceRpcClient;
import kreezerit.Mwebgateway.clientsrpc.PetServiceRpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service("securityService")
public class SecurityService {

    private final UserService userService;
    private final PetServiceRpcClient petRpcClient;

    public boolean isOwnerOfPet(Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        String username = auth.getName();
        Long ownerId = userService.getOwnerIdByUsername(username);
        if (ownerId == null) return false;

        Long petOwnerIdStr = petRpcClient.getOwnerIdByPetId(petId);
        if (petOwnerIdStr == null) return false;

        return ownerId.equals(petOwnerIdStr);
    }

    public boolean isOwner(Long ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        String username = auth.getName();
        Long actualUsersOwnerId = userService.getOwnerIdByUsername(username);

        return actualUsersOwnerId != null && actualUsersOwnerId.equals(ownerId);
    }
}
