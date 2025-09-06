package kreezerit.Mcommon.dto.pet;

import com.fasterxml.jackson.annotation.JsonFormat;
import kreezerit.Mcommon.util.PetColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String breed;

    private PetColor color;

    private int tailLength;

    private List<PetShortDto> friends;

    private Long ownerId;
}
