package kreezerit.Mwebgateway.controller;

import kreezerit.Mcommon.dto.pet.PetDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import kreezerit.Mcommon.dto.rpc.PageResponse;
import kreezerit.Mcommon.dto.rpc.pet.PetSearchRequest;
import kreezerit.Mwebgateway.clientsrpc.PetServiceRpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kreezerit.Mcommon.util.PetColor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PetController {

    private final PetServiceRpcClient petRpcClient;

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#petDto.ownerId)")
    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        return ResponseEntity.ok(petRpcClient.createPet(petDto));
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        PetDto updated = petRpcClient.updatePet(id, petDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petRpcClient.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfPet(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        PetDto dto = petRpcClient.getPetById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> getAllPets() {
        return ResponseEntity.ok(petRpcClient.getAllPets());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<PetDto>> getPetsByName(@PathVariable String name) {
        return ResponseEntity.ok(petRpcClient.getPetsByName(name));
    }

    @GetMapping("/search")
    public PageResponse<PetDto> searchPets(
            @RequestParam(required = false) PetColor color,
            @RequestParam(required = false) String breed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return petRpcClient.searchPets(new PetSearchRequest(color, breed, page, size));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<PetDto>> getFriendsOf(@PathVariable Long id) {
        return ResponseEntity.ok(petRpcClient.getFriendsOf(id));
    }

    @GetMapping("/friends")
    public ResponseEntity<Map<Long, List<PetDto>>> getAllPetsFriends() {
        return ResponseEntity.ok(petRpcClient.getAllPetsFriends());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPets() {
        petRpcClient.deleteAllPets();
        return ResponseEntity.noContent().build();
    }
}
