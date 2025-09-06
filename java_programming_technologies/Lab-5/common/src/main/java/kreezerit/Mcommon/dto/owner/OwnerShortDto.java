package kreezerit.Mcommon.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerShortDto {
    private Long id;
    private String name;
    public LocalDate birthDate;
}
