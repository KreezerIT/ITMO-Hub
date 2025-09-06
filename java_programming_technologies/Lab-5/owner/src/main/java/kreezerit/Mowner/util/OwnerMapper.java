package kreezerit.Mowner.util;

import kreezerit.Mcommon.dto.owner.OwnerDto;
import kreezerit.Mcommon.dto.owner.OwnerShortDto;
import kreezerit.Mowner.entity.Owner;


public class OwnerMapper {

    public static OwnerDto toDto(Owner owner) {
        return new OwnerDto(
                owner.getId(),
                owner.getName(),
                owner.getBirthDate()
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

