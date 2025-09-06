import appconfig.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.dto.OwnerShortDto;
import model.dto.PetDto;
import model.entity.Owner;
import model.entity.Pet;
import model.util.Mappers.OwnerMapper;
import model.util.Mappers.PetMapper;
import model.util.PetColor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import service.OwnerService;
import service.PetService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean
    private OwnerService ownerService;

    @Test
    public void testGetPets() throws Exception {
        List<Pet> mockPets = List.of(Pet.builder().id(1L).name("Buddy").build());

        Mockito.when(petService.findAll()).thenReturn(mockPets);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreatePet() throws Exception {
        PetDto petDto = new PetDto(1L, "Buddy", LocalDate.of(2020, 1, 1), "Bulldog", PetColor.BLACK, null, 10, List.of());
        Pet pet = PetMapper.fromDto(petDto);

        Owner mockOwner = Owner.builder().id(1L).name("John").build();
        petDto.setOwner(OwnerMapper.toShortDto(mockOwner));

        Mockito.when(ownerService.findById(1L)).thenReturn(Optional.of(mockOwner));
        Mockito.when(petService.save(any(Pet.class))).thenReturn(pet);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = objectMapper.writeValueAsString(petDto);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePet() throws Exception {
        PetDto petDto = new PetDto(1L, "UpdatedName", LocalDate.of(2019, 5, 5), "UpdatedBreed", PetColor.WHITE, null, 12, List.of());
        petDto.setOwner(new OwnerShortDto(1L, "Imya", LocalDate.now()));

        Mockito.when(petService.updatePet(eq(1L), any(PetDto.class))).thenReturn(petDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = objectMapper.writeValueAsString(petDto);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPetById() throws Exception {
        Pet pet = Pet.builder().id(1L).name("Buddy").build();

        Mockito.when(petService.findById(1L)).thenReturn(pet);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeletePetById() throws Exception {
        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetPetsByName() throws Exception {
        List<Pet> mockPets = List.of(Pet.builder().id(1L).name("Buddy").build());

        Mockito.when(petService.getByName("Buddy")).thenReturn(mockPets);

        mockMvc.perform(get("/api/pets/search/Buddy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Buddy"));
    }

    @Test
    public void testSearchPets() throws Exception {
        Page<PetDto> page = new PageImpl<>(List.of(
                new PetDto(1L, "Puggy", LocalDate.of(2020, 3, 1), "Pug", PetColor.WHITE, null, 7, List.of())
        ));

        Mockito.when(petService.searchPets(PetColor.WHITE, "Pug", 0, 5)).thenReturn(page);

        mockMvc.perform(get("/api/pets/search")
                        .param("color", "WHITE")
                        .param("breed", "Pug"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].breed").value("Pug"));
    }
}
