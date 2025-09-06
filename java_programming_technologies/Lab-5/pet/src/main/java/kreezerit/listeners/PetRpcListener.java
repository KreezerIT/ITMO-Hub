package kreezerit.listeners;

import kreezerit.Mcommon.dto.pet.PetDto;
import kreezerit.Mcommon.dto.rpc.PageResponse;
import kreezerit.Mcommon.dto.rpc.pet.PetRequest;
import kreezerit.Mcommon.dto.rpc.pet.PetSearchRequest;
import kreezerit.service.PetService;
import kreezerit.util.PetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PetRpcListener {

    private final PetService petService;

    @RabbitListener(queues = "rpc.pet.create")
    public PetDto handleCreate(PetDto dto) {
        return petService.createPet(dto);
    }

    @RabbitListener(queues = "rpc.pet.update")
    public PetDto handleUpdate(PetRequest request) {
        return petService.updatePet(request.getId(), request.getDto());
    }

    @RabbitListener(queues = "rpc.pet.delete")
    public void handleDelete(Long id) {
        petService.deleteById(id);
    }

    @RabbitListener(queues = "rpc.pet.getById")
    public PetDto handleGetById(Long id) {
        return petService.getPetDtoById(id);
    }

    @RabbitListener(queues = "rpc.pet.getAll")
    public List<PetDto> handleGetAll() {
        return petService.getAllDtos();
    }

    @RabbitListener(queues = "rpc.pet.search.by.name")
    public List<PetDto> handleSearchByName(String name) {
        return petService.getByName(name).stream()
                .map(PetMapper::toDto)
                .toList();
    }

    @RabbitListener(queues = "rpc.pet.search")
    public PageResponse<PetDto> handleSearch(PetSearchRequest request) {
        return PageResponse.of(petService.searchPets(request.getColor(), request.getBreed(), request.getPage(), request.getSize()));
    }

    @RabbitListener(queues = "rpc.pet.friends.of")
    public List<PetDto> handleFriendsOf(Long id) {
        return petService.getFriendsOf(id);
    }

    @RabbitListener(queues = "rpc.pet.friends.all")
    public Map<Long, List<PetDto>> handleAllFriends() {
        return petService.getAllPetsFriendsDto();
    }

    @RabbitListener(queues = "rpc.pet.deleteAll")
    public void handleDeleteAll() {
        petService.deleteAll();
    }

    @RabbitListener(queues = "rpc.pet.ownerId.byPetId")
    public Long handleGetOwnerIdByPetId(Long petId) {
        return petService.getOwnerIdByPetId(petId);
    }
}
