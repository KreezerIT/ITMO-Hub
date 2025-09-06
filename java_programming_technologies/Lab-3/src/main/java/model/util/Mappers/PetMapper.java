package model.util.Mappers;

import model.dto.PetDto;
import model.dto.PetShortDto;
import model.entity.Pet;

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
                pet.getOwner() != null ? OwnerMapper.toShortDto(pet.getOwner()) : null,
                pet.getTailLength(),
                pet.getFriends() != null
                        ? pet.getFriends().stream()
                        .map(friend -> new PetShortDto(friend.getId(), friend.getName()))
                        .toList()
                        : List.of()
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

