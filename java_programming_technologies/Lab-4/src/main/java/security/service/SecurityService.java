package security.service;

import model.dao.PetRepository;
import model.entity.Pet;
import org.springframework.security.core.Authentication;
import security.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import security.dao.UserRepository;

@Service("securityService")
public class SecurityService {

    private final PetRepository petRepository;

    private final UserRepository userRepository;

    public SecurityService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwnerOfPet(Long petId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || user.getOwner() == null) return false;

        Pet pet = petRepository.findById(petId).orElse(null);
        return pet != null && pet.getOwner().getId().equals(user.getOwner().getId());
    }

    public boolean isOwner(Long ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null && user.getOwner() != null && user.getOwner().getId().equals(ownerId);
    }

}
