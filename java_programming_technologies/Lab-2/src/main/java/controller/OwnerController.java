package controller;

import entity.Owner;
import service.OwnerServiceImpl;

import java.time.LocalDate;
import java.util.List;

public class OwnerController {

    private final OwnerServiceImpl ownerService;

    public OwnerController(OwnerServiceImpl ownerService) {
        this.ownerService = ownerService;
    }

    public void createOwner(String name, String birthDate) {
        Owner newOwner = new Owner(null, name, LocalDate.parse(birthDate), null);
        ownerService.save(newOwner);
    }

    public Owner updateOwner(long id, String name, String birthDate) {
        Owner ownerToUpdate = ownerService.getById(id);
        if (ownerToUpdate != null) {
            ownerToUpdate.setName(name);
            ownerToUpdate.setBirthDate(LocalDate.parse(birthDate));
            return ownerService.update(ownerToUpdate);
        }
        return null;
    }

    public void deleteOwnerById(long id) {
        ownerService.deleteById(id);
    }

    public void deleteAll() {
        ownerService.deleteAll();
    }

    public void deleteByEntity(Owner owner) {
        ownerService.deleteByEntity(owner);
    }

    public Owner getOwnerById(long id) {
        return ownerService.getById(id);
    }

    public List<Owner> getAllOwners() {
        return ownerService.getAll();
    }

    public List<Owner> getOwnersByName(String name) {
        return ownerService.getByName(name);
    }
}
