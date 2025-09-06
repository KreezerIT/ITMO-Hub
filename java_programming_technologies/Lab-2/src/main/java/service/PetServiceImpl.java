package service;

import dao.DaoWithFriendship;
import entity.Pet;

import java.util.List;
import java.util.Map;

public class PetServiceImpl implements EntityService<Pet> {

    private final DaoWithFriendship<Pet> petDao;

    public PetServiceImpl(DaoWithFriendship<Pet> petDao) {
        this.petDao = petDao;
    }

    public Pet save(Pet pet) {
        return petDao.save(pet);
    }

    public void deleteById(long id) {
        petDao.deleteById(id);
    }

    public void deleteByEntity(Pet pet) {
        petDao.deleteByEntity(pet);
    }

    public void deleteAll() {
        petDao.deleteAll();
    }

    public Pet update(Pet pet) {
        return petDao.update(pet);
    }

    public Pet getById(long id) {
        return petDao.getById(id);
    }

    public List<Pet> getAll() {
        return petDao.getAll();
    }

    public List<Pet> getByName(String name) {
        return petDao.getByName(name);
    }

    public List<Pet> getFriendsOf(Pet pet) {
        return petDao.getFriendsOf(pet);
    }

    public void createPetFriendship(Pet pet1, Pet pet2) {
        petDao.createFriendship(pet1, pet2);
    }

    public Map<Long, List<Pet>> getAllPetsFriends() {
        return petDao.getAllFriends();
    }
}