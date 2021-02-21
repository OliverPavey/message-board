package com.github.oliverpavey.messageboard.converters;

import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ConvertMessageDtoToMessage implements Converter<MessageDto, Message> {

    final ModelMapper modelMapper;

    public ConvertMessageDtoToMessage(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Message convert(@NonNull MessageDto source) {
        return modelMapper.map(source, Message.class);
    }
}
