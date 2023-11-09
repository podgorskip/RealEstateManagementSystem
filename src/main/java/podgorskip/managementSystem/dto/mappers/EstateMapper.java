package podgorskip.managementSystem.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import podgorskip.managementSystem.dto.EstateDTO;
import podgorskip.managementSystem.jpa.entities.Estate;

@Mapper
public interface EstateMapper {
    EstateMapper INSTANCE = Mappers.getMapper(EstateMapper.class);

    @Mapping(source = "estate.agent.firstName", target = "agentFirstName")
    @Mapping(source = "estate.agent.lastName", target = "agentLastName")
    @Mapping(source = "estate.agent.phoneNumber", target = "agentPhoneNumber")
    @Mapping(source = "estate.agent.email", target = "agentEmail")
    EstateDTO convert(Estate estate);
}


