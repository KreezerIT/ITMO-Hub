package web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import model.dto.PetDto;
import model.entity.Pet;
import model.util.Mappers.PetMapper;
import model.util.PetColor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.PetService;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/pets")
@Validated
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        PetDto savedPet = petService.createPet(petDto);
        return ResponseEntity.ok(savedPet);
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        PetDto updated = petService.updatePet(id, petDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        Pet pet = petService.findById(id);
        if (pet != null) {
            return ResponseEntity.ok(PetMapper.toDto(pet));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<PetDto> getAllPets() {
        List<Pet> pets = petService.findAll();
        return PetMapper.toDtoList(pets);
    }

    @GetMapping("/search/{name}")
    public List<PetDto> getPetsByName(@PathVariable String name) {
        List<Pet> pets = petService.getByName(name);
        return PetMapper.toDtoList(pets);
    }

    @GetMapping("/search")
    public Page<PetDto> searchPets(
            @RequestParam(required = false) PetColor color,
            @RequestParam(required = false) String breed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (breed != null && breed.isEmpty()) {
            breed = null;
        }

        return petService.searchPets(color, breed, page, size);
    }

    @GetMapping("/{id}/friends")
    public List<PetDto> getFriendsOf(@PathVariable Long id) {
        return petService.getFriendsOf(id);
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @PostMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> createPetFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        boolean created = petService.createPetFriendship(id, friendId);
        return created ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/friends")
    public Map<Long, List<PetDto>> getAllPetsFriends() {
        return petService.getAllPetsFriendsDto();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPets() {
        petService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
