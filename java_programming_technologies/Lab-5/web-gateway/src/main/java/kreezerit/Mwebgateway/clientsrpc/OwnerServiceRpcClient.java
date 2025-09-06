package kreezerit.Mwebgateway.clientsrpc;

import kreezerit.Mcommon.dto.owner.OwnerDto;
import kreezerit.Mcommon.dto.rpc.owner.OwnerRequest;
import kreezerit.Mcommon.dto.rpc.owner.OwnerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OwnerServiceRpcClient {

    private final RabbitTemplate rabbitTemplate;

    public Long getOwnerIdByUsername(String username) {
        OwnerRequest request = new OwnerRequest(null, username, null);

        OwnerResponse response = (OwnerResponse) rabbitTemplate.convertSendAndReceive(
                "",
                "rpc.owner.id.by.username",
                request
        );

        return response != null ? response.getId() : null;
    }


    public OwnerResponse createOwnerForUsername(String username) {
        System.out.println("Sending request to create owner:" + username);
        OwnerRequest request = new OwnerRequest(null, username, null);

        return (OwnerResponse) rabbitTemplate.convertSendAndReceive(
                "",
                "rpc.owner.create.for.user",
                request
        );
    }

    public List<OwnerDto> getAllOwners() {
        return (List<OwnerDto>) rabbitTemplate.convertSendAndReceive("", "rpc.owner.getAll", "");
    }

    public Optional<OwnerDto> getOwnerById(Long id) {
        return Optional.ofNullable((OwnerDto) rabbitTemplate.convertSendAndReceive("", "rpc.owner.getById", id));
    }

    public OwnerDto createOwner(OwnerDto dto) {
        return (OwnerDto) rabbitTemplate.convertSendAndReceive("", "rpc.owner.create", dto);
    }

    public OwnerDto updateOwner(Long id, OwnerDto dto) {
        OwnerRequest request = new OwnerRequest(id, dto.getName(), dto.getBirthDate());
        return (OwnerDto) rabbitTemplate.convertSendAndReceive("", "rpc.owner.update", request);
    }

    public void deleteOwnerById(Long id) {
        rabbitTemplate.convertSendAndReceive("", "rpc.owner.delete", id);
    }

    public void deleteAllOwners() {
        rabbitTemplate.convertSendAndReceive("", "rpc.owner.deleteAll", "");
    }

    public OwnerDto getOwnersByName(String name) {
        OwnerRequest request = new OwnerRequest(null, name, null);

        return (OwnerDto) rabbitTemplate.convertSendAndReceive("", "rpc.owner.search.by.name", request);
    }
}
