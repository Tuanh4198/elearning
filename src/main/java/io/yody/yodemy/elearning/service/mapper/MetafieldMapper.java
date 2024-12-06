package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.service.business.MetafieldBO;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetafieldMapper extends EntityMapper<MetafieldDTO, MetafieldEntity> {
    MetafieldMapper INSTANCE = Mappers.getMapper(MetafieldMapper.class);
    List<MetafieldEntity> dtoToEntities(List<MetafieldDTO> dtos);
    List<MetafieldEntity> bosToEntities(List<MetafieldBO> bos);
    List<MetafieldBO> entitiesToBos(List<MetafieldEntity> entities);
    List<MetafieldBO> dtosToBos(List<MetafieldDTO> metafieldDTOS);
    MetafieldEntity boToEntity(MetafieldBO metafieldBO);
    List<MetafieldDTO> entitiesToDtos(List<MetafieldEntity> entities);
}
