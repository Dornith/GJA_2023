package vut.fit.gja2023.systemmanager.errorhandling.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vut.fit.gja2023.systemmanager.errorhandling.error.RestError;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseRestException;

import java.util.Locale;

@Mapper(componentModel = "spring", uses = RestErrorMessageMapper.class)
public abstract class RestErrorMapper {

    @Mapping(target = "error", source = "ex")
    @Mapping(target = "timestamp", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "uuid", expression = "java(UUID.randomUUID())")
    public abstract RestError map(BaseRestException ex, @Context Locale locale);

}