package service;

import model.dao.OwnerRepository;
import model.dto.OwnerDto;
import model.entity.Owner;
import model.util.Mappers.OwnerMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    public List<Owner> findAll() {
        return ownerRepository.findAllWithPets();
    }

    public List<Owner> findByName(String name) {
        return ownerRepository.findOwnerByName(name);
    }

    public Optional<Owner> findById(Long id) {
        return ownerRepository.findById(id);
    }

    public void deleteById(Long id) {
        ownerRepository.deleteById(id);
    }

    public void deleteAll() {
        ownerRepository.deleteAll();
    }

    public OwnerDto createOwner(OwnerDto dto) {
        Owner owner = OwnerMapper.fromDto(dto);
        Owner saved = ownerRepository.save(owner);
        return OwnerMapper.toDto(saved);
    }

    public OwnerDto updateOwner(Long id, OwnerDto dto) {
        Optional<Owner> existingOpt = ownerRepository.findById(id);
        if (existingOpt.isPresent()) {
            Owner existing = existingOpt.get();
            existing.setName(dto.getName());
            existing.setBirthDate(dto.getBirthDate());
            Owner updated = ownerRepository.save(existing);
            return OwnerMapper.toDto(updated);
        }
        return null;
    }
}
