package kreezerit.Mwebgateway.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kreezerit.Mcommon.dto.owner.OwnerDto;
import kreezerit.Mwebgateway.clientsrpc.OwnerServiceRpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/owners")
@Validated
public class OwnerController {

    private final OwnerServiceRpcClient ownerRpcClient;

    public OwnerController(OwnerServiceRpcClient ownerRpcClient) {
        this.ownerRpcClient = ownerRpcClient;
    }

    @GetMapping
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        return ResponseEntity.ok(ownerRpcClient.getAllOwners());
    }

   @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable Long id) {
        return ownerRpcClient.getOwnerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<OwnerDto> getOwnersByName(@PathVariable String name) {
        return ResponseEntity.ok(ownerRpcClient.getOwnersByName(name));
    }

    @PostMapping
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto dto) {
        return ResponseEntity.ok(ownerRpcClient.createOwner(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable Long id, @RequestBody OwnerDto dto) {
        OwnerDto updated = ownerRpcClient.updateOwner(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerRpcClient.deleteOwnerById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllOwners() {
        ownerRpcClient.deleteAllOwners();
        return ResponseEntity.noContent().build();
    }
}