package podgorskip.managementSystem.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import podgorskip.managementSystem.jpa.entities.Agent;

@Mapper
public interface AgentMapper {
    AgentMapper INSTANCE = Mappers.getMapper(AgentMapper.class);

    AgentDTO convert(Agent agent);
}
