package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import io.yody.yodemy.elearning.service.dto.QuizzCategoryDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link QuizzCategoryEntity} and its DTO {@link QuizzCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizzCategoryMapper extends EntityMapper<QuizzCategoryDTO, QuizzCategoryEntity> {
    QuizzCategoryMapper INSTANCE = Mappers.getMapper(QuizzCategoryMapper.class);

    QuizzCategoryEntity requestToEntity(QuizzCategoryRequest quizzCategoryRequest);
}
