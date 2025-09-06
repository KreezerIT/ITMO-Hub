import appconfig.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.dto.OwnerDto;
import model.entity.Owner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import service.OwnerService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Test
    public void testGetAllOwners() throws Exception {
        OwnerDto ownerDto = new OwnerDto(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet());
        List<Owner> mockOwners = List.of(new Owner(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet()));

        Mockito.when(ownerService.findAll()).thenReturn(mockOwners);

        mockMvc.perform(get("/api/owners").with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockOwners.size()))
                .andExpect(jsonPath("$[0].name").value(ownerDto.getName()));
    }

    @Test
    public void testGetOwnerById() throws Exception {
        OwnerDto ownerDto = new OwnerDto(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet());
        Owner mockOwner = new Owner(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet());

        Mockito.when(ownerService.findById(1L)).thenReturn(Optional.of(mockOwner));

        mockMvc.perform(get("/api/owners/{id}", 1L).with(user("John").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(ownerDto.getName()));
    }

    @Test
    public void testGetOwnerByIdNotFound() throws Exception {
        Mockito.when(ownerService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/owners/{id}", 1L).with(user("testuser").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOwner() throws Exception {
        OwnerDto inputDto = new OwnerDto(null, "John", LocalDate.of(1985, 5, 15), Collections.emptySet());
        OwnerDto savedDto = new OwnerDto(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet());

        Mockito.when(ownerService.createOwner(any(OwnerDto.class))).thenReturn(savedDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post("/api/owners")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.birthDate").value("1985-05-15"))
                .andExpect(jsonPath("$.pets").isArray());
    }

    @Test
    public void testUpdateOwner() throws Exception {
        OwnerDto inputDto = new OwnerDto(1L, "Updated John", LocalDate.of(1985, 5, 15), Collections.emptySet());

        OwnerDto updatedDto = new OwnerDto(1L, "Updated John", LocalDate.of(1985, 5, 15), Collections.emptySet());

        Mockito.when(ownerService.updateOwner(eq(1L), any(OwnerDto.class))).thenReturn(updatedDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(put("/api/owners/{id}", 1L)
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated John"))
                .andExpect(jsonPath("$.birthDate").value("1985-05-15"))
                .andExpect(jsonPath("$.pets").isArray());
    }

    @Test
    public void testUpdateOwnerNotFound() throws Exception {
        OwnerDto ownerDto = new OwnerDto(1L, "John Updated", LocalDate.of(1985, 5, 15), Collections.emptySet());

        Mockito.when(ownerService.findById(1L)).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = objectMapper.writeValueAsString(ownerDto);

        mockMvc.perform(put("/api/owners/{id}", 1L)
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOwner() throws Exception {
        mockMvc.perform(delete("/api/owners/{id}", 1L)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());

        Mockito.verify(ownerService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAllOwners() throws Exception {
        mockMvc.perform(delete("/api/owners")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());

        Mockito.verify(ownerService, Mockito.times(1)).deleteAll();
    }

    @Test
    public void testGetOwnersByName() throws Exception {
        OwnerDto ownerDto = new OwnerDto(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet());
        List<Owner> mockOwners = List.of(new Owner(1L, "John", LocalDate.of(1985, 5, 15), Collections.emptySet()));

        Mockito.when(ownerService.findByName("John")).thenReturn(mockOwners);

        mockMvc.perform(get("/api/owners/search/John").with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockOwners.size()))
                .andExpect(jsonPath("$[0].name").value(ownerDto.getName()));
    }
}
