package controller;

import entity.Owner;
import entity.Pet;
import service.OwnerServiceImpl;
import service.PetServiceImpl;
import util.PetColor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PetController {

    private final PetServiceImpl petService;
    private final OwnerServiceImpl ownerService;

    public PetController(PetServiceImpl petService, OwnerServiceImpl ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;
    }

    public Pet createPet(String name, String birthDate, String breed, PetColor color, long ownerId) {
        Pet newPet = new Pet();
        newPet.setName(name);
        newPet.setBirthDate(LocalDate.parse(birthDate));
        newPet.setBreed(breed);
        newPet.setColor(color);

        Owner owner = ownerService.getById(ownerId);
        if (owner != null) {
            newPet.setOwner(owner);
            return petService.save(newPet);
        }
        return null;
    }

    public Pet updatePet(long id, String name, String birthDate, String breed, PetColor color, long ownerId) {
        Pet petToUpdate = petService.getById(id);
        if (petToUpdate != null) {
            petToUpdate.setName(name);
            if (!Objects.equals(birthDate, "")) {
                petToUpdate.setBirthDate(LocalDate.parse(birthDate));
            }
            petToUpdate.setBreed(breed);
            petToUpdate.setColor(color);

            Owner owner = ownerService.getById(ownerId);
            if (owner != null) {
                petToUpdate.setOwner(owner);
                return petService.update(petToUpdate);
            }
        }
        return null;
    }

    public void deletePetById(long id) {
        petService.deleteById(id);
    }

    public void deletePetByEntity(Pet pet) {
        petService.deleteByEntity(pet);
    }

    public List<Pet> getPetsByName(String name) {
        return petService.getByName(name);
    }

    public void deleteAll() {
        petService.deleteAll();
    }

    public Pet getPetById(long id) {
        return petService.getById(id);
    }

    public List<Pet> getAllPets() {
        return petService.getAll();
    }

    public List<Pet> getFriendsOf(Pet pet) {
        return petService.getFriendsOf(pet);
    }

    public void createPetFriendship(Pet pet1, Pet pet2) {
        petService.createPetFriendship(pet1, pet2);
    }

    public Map<Long, List<Pet>> getAllPetsFriends() {
        return petService.getAllPetsFriends();
    }

}
