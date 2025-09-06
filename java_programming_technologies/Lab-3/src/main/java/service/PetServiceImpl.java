package service;

import model.dao.PetRepository;
import model.dto.PetDto;
import model.entity.Pet;
import model.util.Mappers.PetMapper;
import model.util.PetColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class PetServiceImpl {

    private final PetRepository petDao;

    public PetServiceImpl(PetRepository petDao) {
        this.petDao = petDao;
    }

    public Pet save(Pet pet) {
        return petDao.save(pet);
    }

    public void deleteById(long id) {
        petDao.deleteById(id);
    }

    public void deleteByEntity(Pet pet) {
        petDao.deleteById(pet.getId());
    }

    public void deleteAll() {
        petDao.deleteAll();
    }

    public Pet findById(long id) {
        return petDao.getReferenceById(id);
    }

    public List<Pet> findAll() {
        return petDao.findAllWithOwnerAndFriends();
    }

    public List<Pet> getByName(String name) {
        return petDao.getPetByName(name);
    }

    public Set<Pet> getFriendsOf(Pet pet) {
        return new HashSet<>(petDao.findAllFriendsWithOwnerAndFriends(pet.getId()));
    }

    public void createPetFriendship(Pet pet1, Pet pet2) {
        petDao.createFriendship(pet1.getId(), pet2.getId());
    }

    public Map<Long, Set<Pet>> getAllPetsFriends() {
        List<Pet> allPets = petDao.getAllPetsFriends();

        Map<Long, Set<Pet>> petsFriendsMap = new HashMap<>();
        for (Pet pet : allPets) {
            petsFriendsMap.put(pet.getId(), pet.getFriends());
        }

        return petsFriendsMap;
    }

    public Page<PetDto> searchPets(PetColor color, String breed, int page, int size) {
        List<Pet> pets = petDao.searchWithFetch(color, breed);
        long count = petDao.countSearch(color, breed);

        List<PetDto> content = pets.stream()
                .skip((long) page * size)
                .limit(size)
                .map(PetMapper::toDto)
                .toList();

        return new PageImpl<>(content, PageRequest.of(page, size), count);
    }

}