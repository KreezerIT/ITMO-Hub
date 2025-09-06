
package kreezerit.Mcommon.dto.rpc.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRequest {
    private Long id;

    private String name;

    private LocalDate birthDate;
}
