package kreezerit.service;

import kreezerit.Mcommon.dto.pet.PetDto;
import kreezerit.entity.Pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import kreezerit.repository.PetRepository;
import kreezerit.Mcommon.util.PetColor;
import kreezerit.util.PetMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void deleteById(long id) {
        petRepository.deleteById(id);
    }

    public void deleteByEntity(Pet pet) {
        petRepository.deleteById(pet.getId());
    }

    public void deleteAll() {
        petRepository.deleteAll();
    }

    public Pet findById(long id) {
        return petRepository.getReferenceById(id);
    }

    public List<Pet> findAll() {
        return petRepository.findAllWithOwnerAndFriends();
    }

    public List<Pet> getByName(String name) {
        return petRepository.getPetByName(name);
    }

    public List<PetDto> getFriendsOf(Long id) {
        Pet pet = findById(id);
        if (pet != null) {
            Set<Pet> friends = pet.getFriends();
            return PetMapper.toDtoList(friends);
        }
        return List.of();
    }

    public boolean createPetFriendship(Long id1, Long id2) {
        Pet pet1 = findById(id1);
        Pet pet2 = findById(id2);
        if (pet1 != null && pet2 != null) {
            pet1.getFriends().add(pet2);
            pet2.getFriends().add(pet1);
            petRepository.save(pet1);
            petRepository.save(pet2);
            return true;
        }
        return false;
    }

    public Map<Long, Set<Pet>> getAllPetsFriends() {
        List<Pet> allPets = petRepository.getAllPetsFriends();

        Map<Long, Set<Pet>> petsFriendsMap = new HashMap<>();
        for (Pet pet : allPets) {
            petsFriendsMap.put(pet.getId(), pet.getFriends());
        }

        return petsFriendsMap;
    }

    public Map<Long, List<PetDto>> getAllPetsFriendsDto() {
        Map<Long, Set<Pet>> map = getAllPetsFriends();
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> PetMapper.toDtoList(e.getValue())
                ));
    }

    public Page<PetDto> searchPets(PetColor color, String breed, int page, int size) {
        List<Pet> pets = petRepository.searchWithFetch(color, breed);
        long count = petRepository.countSearch(color, breed);

        List<PetDto> content = pets.stream()
                .skip((long) page * size)
                .limit(size)
                .map(PetMapper::toDto)
                .toList();

        return new PageImpl<>(content, PageRequest.of(page, size), count);
    }

    public PetDto createPet(PetDto petDto) {
        Pet pet = PetMapper.fromDto(petDto);

        pet.setOwnerId(petDto.getOwnerId());

        Pet saved = petRepository.save(pet);
        return PetMapper.toDto(saved);
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        Pet petToUpdate = findById(id);
        if (petToUpdate == null) return null;

        if (petDto.getId() != null) {
            petToUpdate.setId(petDto.getId());
        }

        if (petDto.getName() != null) {
            petToUpdate.setName(petDto.getName());
        }

        if (petDto.getBirthDate() != null) {
            petToUpdate.setBirthDate(petDto.getBirthDate());
        }

        if (petDto.getBreed() != null) {
            petToUpdate.setBreed(petDto.getBreed());
        }

        if (petDto.getColor() != null) {
            petToUpdate.setColor(petDto.getColor());
        }

        if (petDto.getTailLength() != 0) {
            petToUpdate.setTailLength(petDto.getTailLength());
        }

        if (petDto.getFriends() != null) {
            petToUpdate.setFriends(PetMapper.fromShortDtoList(petDto.getFriends()));
        }

        if (petDto.getOwnerId() != null) {
            petToUpdate.setOwnerId(petDto.getOwnerId());
        }

        Pet saved = save(petToUpdate);
        return PetMapper.toDto(saved);
    }

    public Long getOwnerIdByPetId(Long petId) {
        return petRepository.findOwnerIdByPetId(petId);
    }

    public PetDto getPetDtoById(Long id) {
        Pet pet = findById(id);
        return pet != null ? PetMapper.toDto(pet) : null;
    }

    public List<PetDto> getAllDtos() {
        return PetMapper.toDtoList(findAll());
    }
}
