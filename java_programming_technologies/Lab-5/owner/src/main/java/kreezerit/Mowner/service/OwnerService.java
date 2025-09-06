package kreezerit.Mowner.service;

import kreezerit.Mcommon.dto.owner.OwnerDto;
import kreezerit.Mowner.entity.Owner;
import kreezerit.Mowner.repository.OwnerRepository;
import kreezerit.Mowner.util.OwnerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    public List<Owner> findAll() {
        return ownerRepository.findAllWithoutPets();
    }

    public Owner findByName(String name) {
        return ownerRepository.getOwnerByName(name)
                .orElseThrow(() -> new RuntimeException("Owner not found: " + name));
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
        Owner saved = save(owner);
        return OwnerMapper.toDto(saved);
    }

    public OwnerDto updateOwner(Long id, OwnerDto dto) {
        Optional<Owner> existingOpt = ownerRepository.findById(id);
        if (existingOpt.isPresent()) {
            Owner existing = existingOpt.get();
            existing.setName(dto.getName());
            existing.setBirthDate(dto.getBirthDate());
            Owner updated = save(existing);
            return OwnerMapper.toDto(updated);
        }
        return null;
    }
}
