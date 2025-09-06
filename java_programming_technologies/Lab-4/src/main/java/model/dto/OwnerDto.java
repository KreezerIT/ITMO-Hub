package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDto {
    public Long id;

    public String name;

    public LocalDate birthDate;

    public Set<PetShortDto> pets;
}
