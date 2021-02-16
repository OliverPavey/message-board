package com.github.oliverpavey.messageboard.converters;

import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dto.BoardDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ConvertBoardToBoardDto implements Converter<Board, BoardDto> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public BoardDto convert(@NonNull Board source) {
        return modelMapper.map(source, BoardDto.class);
    }
}
