package web.controller;

import model.dto.PetDto;
import model.entity.Pet;
import model.util.Mappers.PetMapper;
import model.util.PetColor;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.PetService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@Validated
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        PetDto savedPet = petService.createPet(petDto);
        return ResponseEntity.ok(savedPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        PetDto updated = petService.updatePet(id, petDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

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

    @PostMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> createPetFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        boolean created = petService.createPetFriendship(id, friendId);
        return created ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/friends")
    public Map<Long, List<PetDto>> getAllPetsFriends() {
        return petService.getAllPetsFriendsDto();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPets() {
        petService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
