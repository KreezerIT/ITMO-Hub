package kreezerit.Mowner.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import kreezerit.Mcommon.dto.owner.OwnerDto;
import kreezerit.Mcommon.dto.rpc.owner.OwnerRequest;
import kreezerit.Mcommon.dto.rpc.owner.OwnerResponse;
import kreezerit.Mowner.service.OwnerService;
import kreezerit.Mowner.util.OwnerMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OwnerRpcListener {

    private final OwnerService ownerService;

    public OwnerRpcListener(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RabbitListener(queues = "rpc.owner.create.for.user")
    public OwnerResponse handleCreate(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OwnerRequest request = mapper.readValue(message.getBody(), OwnerRequest.class);

        if (request == null) {
            throw new IllegalArgumentException("User request is null");
        }

        OwnerDto dto = new OwnerDto();
        dto.setName(request.getName());
        Long ownerId = ownerService.createOwner(dto).getId();
        return new OwnerResponse(ownerId);
    }

    @RabbitListener(queues = "rpc.owner.getAll")
    public List<OwnerDto> handleGetAll() {
        return ownerService.findAll()
                .stream()
                .map(OwnerMapper::toDto)
                .toList();
    }

    @RabbitListener(queues = "rpc.owner.getById")
    public OwnerDto handleGetById(Long id) {
        return ownerService.findById(id)
                .map(OwnerMapper::toDto)
                .orElse(null);
    }

    @RabbitListener(queues = "rpc.owner.id.by.username")
    public OwnerResponse handleGetIdByUsername(OwnerRequest request) {
        OwnerDto owner = OwnerMapper.toDto(ownerService.findByName(request.getName()));

        return new OwnerResponse(owner.getId());
    }


    @RabbitListener(queues = "rpc.owner.create")
    public OwnerDto handleCreate(OwnerDto dto) {
        return ownerService.createOwner(dto);
    }

    @RabbitListener(queues = "rpc.owner.update")
    public OwnerDto handleUpdate(OwnerRequest request) {
        return ownerService.updateOwner(request.getId(), new OwnerDto(request.getId(), request.getName(), request.getBirthDate()));
    }

    @RabbitListener(queues = "rpc.owner.delete")
    public void handleDelete(Long id) {
        ownerService.deleteById(id);
    }

    @RabbitListener(queues = "rpc.owner.deleteAll")
    public void handleDeleteAll() {
        ownerService.deleteAll();
    }

    @RabbitListener(queues = "rpc.owner.search.by.name")
    public OwnerDto handleSearchByName(OwnerRequest request) {
        return OwnerMapper.toDto(ownerService.findByName(request.getName()));
    }
}

