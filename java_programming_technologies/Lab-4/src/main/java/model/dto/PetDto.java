package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import model.util.PetColor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long id;

    private String name;

    private LocalDate birthDate;

    private String breed;

    private PetColor color;

    private OwnerShortDto owner;

    private int tailLength;

    private List<PetShortDto> friends;
}
