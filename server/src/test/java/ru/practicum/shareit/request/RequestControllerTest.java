package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestShortDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long userId = 1L;
    private final Long requestId = 2L;

    @Test
    void postRequest() throws Exception {
        RequestCreateDto createDto = new RequestCreateDto();
        createDto.setDescription("Need a drill");

        RequestShortDto shortDto = new RequestShortDto();
        shortDto.setId(requestId);
        shortDto.setDescription("Need a drill");

        Mockito.when(requestService.postRequest(any(), eq(userId)))
                .thenReturn(shortDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    void getRequests() throws Exception {
        RequestDto dto = new RequestDto();
        dto.setId(requestId);
        dto.setDescription("Need a hammer");

        Mockito.when(requestService.getRequests(userId))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestId))
                .andExpect(jsonPath("$[0].description").value("Need a hammer"));
    }

    @Test
    void getAllRequests() throws Exception {
        RequestDto dto = new RequestDto();
        dto.setId(requestId);
        dto.setDescription("Need a ladder");

        Mockito.when(requestService.getAllRequests())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestId))
                .andExpect(jsonPath("$[0].description").value("Need a ladder"));
    }

    @Test
    void getRequestById() throws Exception {
        RequestDto dto = new RequestDto();
        dto.setId(requestId);
        dto.setDescription("Need a chainsaw");

        Mockito.when(requestService.getRequestById(requestId))
                .thenReturn(dto);

        mockMvc.perform(get("/requests/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.description").value("Need a chainsaw"));
    }
}

