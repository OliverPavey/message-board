package com.github.oliverpavey.messageboard.gui;

import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dto.BoardDto;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import com.github.oliverpavey.messageboard.repo.MessageRepo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class GuiController {

    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    @Autowired
    BoardRepo boardRepo;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    ConversionService conversionService;

    List<BoardDto> boards() {
        return Lists.newArrayList(boardRepo.findAll()).stream()
                .map(board -> conversionService.convert(board, BoardDto.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    BoardDto board(Long id) {
        final Board board = boardRepo.findById(id).orElseThrow();
        return conversionService.convert(board, BoardDto.class);
    }

    List<MessageDto> messages(Long boardId) {
        return Lists.newArrayList(messageRepo.findByBoard_IdOrderByCreateDate(boardId)).stream()
                .map(message -> conversionService.convert(message, MessageDto.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @GetMapping({"/", "index.htm", "boards.html", "home.htm", "home.html"})
    public String home(Model model) {
        common(model);
        model.addAttribute("boards", boards());
        return "boards";
    }

    @GetMapping("/messages/{boardId}")
    public String messages(Model model, @PathVariable("boardId") Long boardId) {
        common(model);
        model.addAttribute("board", board(boardId));
        model.addAttribute("messages", messageRepo.findByBoard_IdOrderByCreateDate(boardId));
        return "messages";
    }

    private void common(Model model) {
        model.addAttribute("fetchTime", sdf.format(new Date()));
    }
}
