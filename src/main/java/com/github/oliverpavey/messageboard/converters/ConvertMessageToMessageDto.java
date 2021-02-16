package com.github.oliverpavey.messageboard.converters;

import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ConvertMessageToMessageDto implements Converter<Message, MessageDto> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public MessageDto convert(@NonNull Message source) {
        return modelMapper.map(source, MessageDto.class);
    }
}
