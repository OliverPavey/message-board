package com.github.oliverpavey.messageboard.api;

import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dto.BoardDto;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import com.google.common.collect.Lists;
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
public class ApiControllerV1Board {

    ConversionService conversionService;
    BoardRepo boardRepo;

    public ApiControllerV1Board(ConversionService conversionService,
                                BoardRepo boardRepo) {

        this.conversionService = conversionService;
        this.boardRepo = boardRepo;
    }

    @GetMapping("/boards")
    List<BoardDto> getBoards() {

        return Lists.newArrayList(boardRepo.findAll()).stream()
                .map(board -> conversionService.convert(board, BoardDto.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @GetMapping("/board/{id}")
    BoardDto getBoard(@PathVariable long id) {

        final Board board = boardRepo.findById(id).orElseThrow(
                () -> new ApiExceptions.DataNotFound("Cannot get board as no board found with supplied id."));
        return conversionService.convert(board, BoardDto.class);
    }

    @PostMapping("/board")
    ResponseEntity<Void> postBoard(@RequestBody BoardDto boardDto) {

        if (boardDto.getId() != 0L)
            throw new ApiExceptions.BadRequest("Cannot create new board with supplied id.");
        final Board board = checkBoardNotNull(conversionService.convert(boardDto, Board.class));
        boardRepo.save(board);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/board")
    ResponseEntity<Void> putBoard(@RequestBody BoardDto boardDto) {

        if (!boardRepo.existsById(boardDto.getId()))
            throw new ApiExceptions.BadRequest("Cannot update board for id which does not already exist.");
        final Board board = checkBoardNotNull(conversionService.convert(boardDto, Board.class));
        boardRepo.save(board);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    Board checkBoardNotNull(Board board) {
        if (board == null)
            throw new ApiExceptions.DataInvalid("Could not convert received board data.");
        return board;
    }
}
