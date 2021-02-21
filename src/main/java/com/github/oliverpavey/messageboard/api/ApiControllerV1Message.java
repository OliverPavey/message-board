package com.github.oliverpavey.messageboard.api;

import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import com.github.oliverpavey.messageboard.repo.MessageRepo;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messageboard/v1")
@IncludeInSwaggerUi
public class ApiControllerV1Message {

    ConversionService conversionService;
    BoardRepo boardRepo;
    MessageRepo messageRepo;

    public ApiControllerV1Message(ConversionService conversionService,
                                  BoardRepo boardRepo,
                                  MessageRepo messageRepo) {

        this.conversionService = conversionService;
        this.boardRepo = boardRepo;
        this.messageRepo = messageRepo;
    }

    @GetMapping("/board/{boardId}/messages")
    List<MessageDto> getMessages(@PathVariable long boardId) {

        if (!boardRepo.existsById(boardId))
            throw new ApiExceptions.DataNotFound("No board found with the given id.");
        return messageRepo.findByBoard_IdOrderByCreateDate(boardId).stream()
                .map(message -> conversionService.convert(message, MessageDto.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @GetMapping("/board/{boardId}/message/{id}")
    MessageDto getMessage(@PathVariable long boardId, @PathVariable long id) {

        final Message message = messageRepo.findById(id).orElseThrow(
                () -> new ApiExceptions.DataNotFound("Could not find message for given id."));
        if (message.getBoard().getId() != boardId)
            throw new ApiExceptions.DataNotFound("The message does not belong to the specified board.");
        return conversionService.convert(message, MessageDto.class);
    }

    @PostMapping("/message")
    ResponseEntity<Void> postMessage(@RequestBody MessageDto messageDto) {

        if (!boardRepo.existsById(messageDto.getBoardId()))
            throw new ApiExceptions.BadRequest("Must specify existing board when creating a new message.");
        if (messageDto.getId() != 0L)
            throw new ApiExceptions.BadRequest("Cannot create new message with supplied id.");
        final Message message = checkMessageNotNull(conversionService.convert(messageDto, Message.class));
        messageRepo.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/message")
    ResponseEntity<Void> putMessage(@RequestBody MessageDto messageDto) {

        final Message message = messageRepo.findById(messageDto.getId()).orElseThrow(
                () -> new ApiExceptions.BadRequest("Cannot find existing message by id to update."));
        if (message.getBoard().getId() != messageDto.getBoardId())
            throw new ApiExceptions.BadRequest("Cannot move message between message boards.");
        message.setMessage(messageDto.getMessage());
        messageRepo.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/board/{boardId}/message/{id}")
    ResponseEntity<Void> deleteMessage(@PathVariable long boardId, @PathVariable long id) {

        final Message message = messageRepo.findById(id).orElseThrow(
                () -> new ApiExceptions.DataNotFound("Could not find message to delete for given id."));
        if (message.getBoard().getId() != boardId)
            throw new ApiExceptions.DataNotFound("The message to delete does not belong to the specified board.");
        messageRepo.delete(message);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    Message checkMessageNotNull(Message message) {
        if (message == null)
            throw new ApiExceptions.DataInvalid("Could not convert received message data.");
        return message;
    }
}
