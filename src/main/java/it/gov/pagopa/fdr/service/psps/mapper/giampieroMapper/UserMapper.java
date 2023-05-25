package it.gov.pagopa.fdr.service.psps.mapper.giampieroMapper;
import it.gov.pagopa.fdr.rest.giampiero.request.User;
import it.gov.pagopa.fdr.service.dto.giampieroDto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.JAKARTA)
public interface UserMapper {
     UserDto toUserDto (User utente);
     @Mapping(source = "nascita", target = "eta", qualifiedByName = "nascitaToEta")
     it.gov.pagopa.fdr.repository.giampiero.User toUser (UserDto utente);
     @Mapping(source = "nascita", target = "eta", qualifiedByName = "nascitaToEta")
     void updateUser(UserDto userDto, @MappingTarget it.gov.pagopa.fdr.repository.giampiero.User user);


     //it.gov.pagopa.fdr.repository.giampiero.User toEta (UserDto userDto);

     @Named("nascitaToEta")
     default long nascitaToEta(LocalDate nascita) {
          return ChronoUnit.YEARS.between(nascita,LocalDate.now());
     }
}
