package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestShortDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestShortDto postRequest(RequestCreateDto requestCreateDto, long userId) {
        Request newRequest = RequestMapper.toModel(requestCreateDto);
        newRequest.setRequestor(checkAndGetUserById(userId));
        return RequestMapper.toShortDto(requestRepository.save(newRequest));
    }

    public RequestDto getRequestById(long requestId) {
        return RequestMapper.toDto(checkAndGetRequestById(requestId));
    }

    public List<RequestDto> getRequests(long userId) {
        checkAndGetUserById(userId);
        List<Request> userRequestList = requestRepository.getAllByRequestorId(userId);
        return userRequestList.stream()
                .map(RequestMapper::toDto)
                .sorted(Comparator.comparing(RequestDto::getCreated).reversed())
                .toList();
    }

    public List<RequestDto> getAllRequests() {
        List<Request> requestList =  requestRepository.findAll();
        return requestList.stream()
                .map(RequestMapper::toDto)
                .sorted(Comparator.comparing(RequestDto::getCreated).reversed())
                .toList();
    }

    private User checkAndGetUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id = " + userId + " not found.");
        }
        return userOptional.get();
    }

    private Request checkAndGetRequestById(long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            throw new NoSuchElementException("Request with id = " + requestId + " not found.");
        }
        return requestOptional.get();
    }
}
