package model.util.Mappers;

import model.dto.OwnerDto;
import model.dto.OwnerShortDto;
import model.entity.Owner;

import java.util.Set;
import java.util.stream.Collectors;

public class OwnerMapper {

    public static OwnerDto toDto(Owner owner) {
        return new OwnerDto(
                owner.getId(),
                owner.getName(),
                owner.getBirthDate(),
                owner.getPets() != null
                        ? owner.getPets().stream()
                        .map(PetMapper::toShortDto)
                        .collect(Collectors.toSet())
                        : Set.of()
        );
    }


    public static Owner fromDto(OwnerDto dto) {
        return Owner.builder()
                .id(dto.getId())
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .build();
    }

    public static OwnerShortDto toShortDto(Owner owner) {
        return new OwnerShortDto(owner.getId(), owner.getName(), owner.getBirthDate());
    }
}

