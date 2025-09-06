package kreezerit.Mwebgateway.clientsrpc;

import kreezerit.Mcommon.dto.pet.PetDto;
import kreezerit.Mcommon.dto.rpc.PageResponse;
import kreezerit.Mcommon.dto.rpc.pet.PetRequest;
import kreezerit.Mcommon.dto.rpc.pet.PetSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PetServiceRpcClient {

    private final RabbitTemplate rabbitTemplate;

    public PetDto createPet(PetDto petDto) {
        return (PetDto) rabbitTemplate.convertSendAndReceive("", "rpc.pet.create", petDto);
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        PetRequest request = new PetRequest(id, petDto);
        return (PetDto) rabbitTemplate.convertSendAndReceive("", "rpc.pet.update", request);
    }

    public void deletePet(Long id) {
        rabbitTemplate.convertSendAndReceive("", "rpc.pet.delete", id);
    }

    public PetDto getPetById(Long id) {
        return (PetDto) rabbitTemplate.convertSendAndReceive("", "rpc.pet.getById", id);
    }

    public List<PetDto> getAllPets() {
        return (List<PetDto>) rabbitTemplate.convertSendAndReceive("", "rpc.pet.getAll", "");
    }

    public List<PetDto> getPetsByName(String name) {
        return (List<PetDto>) rabbitTemplate.convertSendAndReceive("", "rpc.pet.search.by.name", name);
    }

    public PageResponse<PetDto> searchPets(PetSearchRequest request) {
        return (PageResponse<PetDto>) rabbitTemplate.convertSendAndReceive("", "rpc.pet.search", request);
    }

    public List<PetDto> getFriendsOf(Long id) {
        return (List<PetDto>) rabbitTemplate.convertSendAndReceive("", "rpc.pet.friends.of", id);
    }

    public Map<Long, List<PetDto>> getAllPetsFriends() {
        return (Map<Long, List<PetDto>>) rabbitTemplate.convertSendAndReceive("", "rpc.pet.friends.all", "");
    }

    public void deleteAllPets() {
        rabbitTemplate.convertSendAndReceive("", "rpc.pet.deleteAll", "");
    }

    public Long getOwnerIdByPetId(Long petId) {
        return (Long) rabbitTemplate.convertSendAndReceive("", "rpc.pet.ownerId.byPetId", petId);
    }
}

