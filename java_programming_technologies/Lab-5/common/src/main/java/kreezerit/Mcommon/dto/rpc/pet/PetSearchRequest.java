package kreezerit.Mcommon.dto.rpc.pet;

import kreezerit.Mcommon.util.PetColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetSearchRequest {
    private PetColor color;
    private String breed;
    private int page;
    private int size;
}