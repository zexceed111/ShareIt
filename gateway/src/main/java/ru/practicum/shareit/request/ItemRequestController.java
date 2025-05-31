package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;



@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    public Object postRequest(@Valid
                                              @RequestBody
                                              @NotNull(message = "the field cannot be empty")
                                              RequestCreateDto requestCreateDto,
                                              @RequestHeader("X-Sharer-User-Id")
                                              @NotNull(message = "the field cannot be empty")
                                              @Positive(message = "user id must be positive")
                                              Long userId) {
        log.info("");
        return requestClient.postRequest(requestCreateDto, userId);
    }

    @GetMapping
    public Object getRequests(@RequestHeader("X-Sharer-User-Id")
                                                    @NotNull(message = "the field cannot be empty")
                                                    @Positive(message = "user id must be positive")
                                                    Long userId) {
        log.info("");
        return requestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        log.info("");
        return ResponseEntity.ok(requestClient.getAllRequests());
    }

    @GetMapping("/{requestId}")
    public Object getRequestById(@PathVariable
                                                 @NotNull(message = "the field cannot be empty")
                                                 @Positive(message = "user id must be positive")
                                                 Long requestId) {
        log.info("");
        return requestClient.getRequestById(requestId);
    }
}
