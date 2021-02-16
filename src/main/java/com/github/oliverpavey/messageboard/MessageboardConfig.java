package com.github.oliverpavey.messageboard;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.oliverpavey.messageboard.api.IncludeInSwaggerUi;
import com.github.oliverpavey.messageboard.converters.ConvertBoardDtoToBoard;
import com.github.oliverpavey.messageboard.converters.ConvertBoardToBoardDto;
import com.github.oliverpavey.messageboard.converters.ConvertMessageDtoToMessage;
import com.github.oliverpavey.messageboard.converters.ConvertMessageToMessageDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;

@Configuration
@EnableSwagger2
public class MessageboardConfig {

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name = "conversionService")
    ConversionService conversionRegistrations(
            ConvertMessageDtoToMessage convertMessageDtoToMessage,
            ConvertMessageToMessageDto convertMessageToMessageDto,
            ConvertBoardDtoToBoard convertBoardDtoToBoard,
            ConvertBoardToBoardDto convertBoardToBoardDto
    ) {

        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(Set.of(
                convertMessageDtoToMessage,
                convertMessageToMessageDto,
                convertBoardDtoToBoard,
                convertBoardToBoardDto));

        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(IncludeInSwaggerUi.class))
                .paths(PathSelectors.any())
                .build();
    }
}
