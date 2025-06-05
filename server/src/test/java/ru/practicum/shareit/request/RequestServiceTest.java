package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestShortDto;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    private final RequestService requestService;

    @Test
    void postRequestTest() {
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("request_test_description")
                .build();

        RequestShortDto requestShortDto = requestService.postRequest(requestCreateDto, 1);

        Assertions.assertNotNull(requestShortDto);
        Assertions.assertEquals(2, requestShortDto.getId());
        Assertions.assertEquals("request_test_description", requestShortDto.getDescription());
    }

    @Test
    void postRequestWithWrongUserIdTest() {
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("request_test_description")
                .build();

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> requestService.postRequest(requestCreateDto, 100)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 100 not found.");
    }

    @Test
    void getRequestByIdTest() {
        RequestDto requestDto = requestService.getRequestById(1);

        Assertions.assertNotNull(requestDto);
        Assertions.assertEquals("request_description", requestDto.getDescription());
    }

    @Test
    void getRequestByWrongIdTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(100)
                );

        Assertions.assertEquals(e.getMessage(), "Request with id = 100 not found.");
    }

    @Test
    void getRequestsTest() {
        List<RequestDto> requestDtoList = requestService.getRequests(1);

        Assertions.assertNotNull(requestDtoList);
        Assertions.assertEquals(1, requestDtoList.size());
    }

    @Test
    void getAllRequestsTest() {
        List<RequestDto> requestDtoList = requestService.getAllRequests();

        Assertions.assertNotNull(requestDtoList);
        Assertions.assertEquals(1, requestDtoList.size());
    }
}
