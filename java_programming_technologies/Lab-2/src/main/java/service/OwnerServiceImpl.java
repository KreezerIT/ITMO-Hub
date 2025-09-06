package service;

import dao.Dao;
import entity.Owner;

import java.util.List;

public class OwnerServiceImpl implements EntityService<Owner> {

    private final Dao<Owner> ownerDao;

    public OwnerServiceImpl(Dao<Owner> ownerDao) {
        this.ownerDao = ownerDao;
    }

    public Owner save(Owner owner) {
        return ownerDao.save(owner);
    }

    public void deleteById(long id) {
        ownerDao.deleteById(id);
    }

    public void deleteByEntity(Owner owner) {
        ownerDao.deleteByEntity(owner);
    }

    public void deleteAll() {
        ownerDao.deleteAll();
    }

    public Owner update(Owner owner) {
        return ownerDao.update(owner);
    }

    public Owner getById(long id) {
        return ownerDao.getById(id);
    }

    public List<Owner> getAll() {
        return ownerDao.getAll();
    }

    public List<Owner> getByName(String name) {
        return ownerDao.getByName(name);
    }
}