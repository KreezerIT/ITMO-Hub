package web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import model.dto.OwnerDto;
import model.util.Mappers.OwnerMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import service.OwnerService;

import java.util.Collections;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/owners")
@Validated
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        List<OwnerDto> owners = ownerService.findAll().stream()
                .map(OwnerMapper::toDto)
                .toList();

        if (owners.isEmpty()) {
            return ResponseEntity.ok().body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(owners);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable Long id) {
        return ownerService.findById(id)
                .map(owner -> ResponseEntity.ok(OwnerMapper.toDto(owner)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<OwnerDto>> getOwnersByName(@PathVariable String name) {
        List<OwnerDto> result = ownerService.findByName(name).stream()
                .map(OwnerMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto dto) {
        OwnerDto savedDto = ownerService.createOwner(dto);
        return ResponseEntity.ok(savedDto);
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable Long id, @RequestBody OwnerDto dto) {
        OwnerDto updated = ownerService.updateOwner(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllOwners() {
        ownerService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}