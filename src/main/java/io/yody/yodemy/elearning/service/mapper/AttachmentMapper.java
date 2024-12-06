package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.AttachmentEntity;
import io.yody.yodemy.elearning.service.dto.AttachmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, AttachmentEntity> {}
