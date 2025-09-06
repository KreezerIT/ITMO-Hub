package kreezerit.util;


import kreezerit.Mcommon.dto.pet.PetDto;
import kreezerit.Mcommon.dto.pet.PetShortDto;
import kreezerit.entity.Pet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PetMapper {

    public static PetDto toDto(Pet pet) {
        return new PetDto(
                pet.getId(),
                pet.getName(),
                pet.getBirthDate(),
                pet.getBreed(),
                pet.getColor(),
                pet.getTailLength(),
                pet.getFriends() != null
                        ? pet.getFriends().stream()
                        .map(friend -> new PetShortDto(friend.getId(), friend.getName()))
                        .toList()
                        : List.of(),
                pet.getOwnerId()
                );
    }


    public static Pet fromDto(PetDto dto) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setName(dto.getName());
        pet.setBirthDate(dto.getBirthDate());
        pet.setBreed(dto.getBreed());
        pet.setColor(dto.getColor());
        pet.setTailLength(dto.getTailLength());

        return pet;
    }

    public static Set<Pet> fromShortDtoList(List<PetShortDto> list) {
        if (list == null) return Set.of();
        return list.stream()
                .map(shortDto -> {
                    Pet pet = new Pet();
                    pet.setId(shortDto.getId());
                    pet.setName(shortDto.getName());
                    return pet;
                })
                .collect(Collectors.toSet());
    }

    public static PetShortDto toShortDto(Pet pet) {
        return new PetShortDto(pet.getId(), pet.getName());
    }

    public static List<PetDto> toDtoList(Set<Pet> pets) {
        return pets.stream().map(PetMapper::toDto).toList();
    }

    public static List<PetDto> toDtoList(List<Pet> pets) {
        return pets.stream().map(PetMapper::toDto).toList();
    }


    public static Map<Long, List<PetDto>> toDtoMap(Map<Long, Set<Pet>> petsFriends) {
        return petsFriends.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> toDtoList(entry.getValue())));
    }
}

