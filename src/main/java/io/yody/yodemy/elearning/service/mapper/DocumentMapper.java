package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.service.business.DocumentBO;
import io.yody.yodemy.elearning.service.business.QuizzAnswerBO;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for the entity {@link DocumentEntity} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, DocumentEntity> {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
    List<DocumentEntity> bosToEntities(List<DocumentBO> documentBOS);
    List<DocumentBO> entitiesToBos(List<DocumentEntity> documentEntities);
    List<DocumentDTO> entitiesToDtos(List<DocumentEntity> documentEntities);
}
