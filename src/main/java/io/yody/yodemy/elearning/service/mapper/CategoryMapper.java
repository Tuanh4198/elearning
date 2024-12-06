package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link CategoryEntity} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, CategoryEntity> {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
}
