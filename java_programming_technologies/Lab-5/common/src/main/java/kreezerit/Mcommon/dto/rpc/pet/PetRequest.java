package kreezerit.Mcommon.dto.rpc.pet;

import kreezerit.Mcommon.dto.pet.PetDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetRequest {
    private Long id;
    private PetDto dto;
}
