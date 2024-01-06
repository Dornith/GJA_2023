package vut.fit.gja2023.systemmanager.errorhandling.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import vut.fit.gja2023.systemmanager.errorhandling.error.RestErrorMessage;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseRestException;

import java.util.Locale;


@Mapper(componentModel = "spring")
public abstract class RestErrorMessageMapper {

    @Autowired
    @Qualifier("errorMessageSource")
    protected MessageSource errorMessageSource;

    @Mapping(target = "message", source = "ex")
    public abstract RestErrorMessage map(BaseRestException ex, @Context Locale locale);

    protected String translateBaseException(BaseRestException ex, @Context Locale locale) {
        return errorMessageSource.getMessage(ex.getCode(), ex.getArgs(), locale);
    }

}
