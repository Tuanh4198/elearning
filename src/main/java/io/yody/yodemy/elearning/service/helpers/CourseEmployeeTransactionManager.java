package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.repository.CourseEmployeeRepository;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.service.business.MetafieldBO;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.COURSE_EMPLOYEE_OWNER_RESOURCE;
import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.DOCUMENT_EMPLOYEE_OWNER_RESOURCE;

@Component
public class CourseEmployeeTransactionManager {
    private static final Logger log = LoggerFactory.getLogger(CourseEmployeeTransactionManager.class);
    private final CourseEmployeeRepository courseEmployeeRepository;
    private final MetafieldRepository metafieldRepository;
    private final MetafieldMapper metafieldMapper;

    public CourseEmployeeTransactionManager(
        CourseEmployeeRepository courseEmployeeRepository,
        MetafieldRepository metafieldRepository,
        MetafieldMapper metafieldMapper
    ) {
        this.courseEmployeeRepository = courseEmployeeRepository;
        this.metafieldRepository = metafieldRepository;
        this.metafieldMapper = metafieldMapper;
    }

    public List<MetafieldDTO> saveLearnMetafields(List<MetafieldBO> metafieldBOS, Long courseEmployeeId) {
        List<MetafieldEntity> metafieldEntities = metafieldMapper.bosToEntities(metafieldBOS);
        for (MetafieldEntity metafieldEntity : metafieldEntities) {
            metafieldEntity.setOwnerResource(COURSE_EMPLOYEE_OWNER_RESOURCE);
            metafieldEntity.setNamespace(COURSE_EMPLOYEE_OWNER_RESOURCE);
            metafieldEntity.setType(COURSE_EMPLOYEE_OWNER_RESOURCE);
            metafieldEntity.setOwnerId(courseEmployeeId);
        }
        metafieldEntities = metafieldRepository.saveAll(metafieldEntities);
        return metafieldMapper.toDto(metafieldEntities);
    }

    public List<MetafieldDTO> saveDocumentLearnMetafields(List<MetafieldBO> metafieldBOS, Long documentEmployeeId) {
        List<MetafieldEntity> metafieldEntities = metafieldMapper.bosToEntities(metafieldBOS);
        for (MetafieldEntity metafieldEntity : metafieldEntities) {
            metafieldEntity.setOwnerResource(DOCUMENT_EMPLOYEE_OWNER_RESOURCE);
            metafieldEntity.setNamespace(DOCUMENT_EMPLOYEE_OWNER_RESOURCE);
            metafieldEntity.setType(DOCUMENT_EMPLOYEE_OWNER_RESOURCE);
            metafieldEntity.setOwnerId(documentEmployeeId);
        }
        metafieldEntities = metafieldRepository.saveAll(metafieldEntities);
        return metafieldMapper.toDto(metafieldEntities);
    }
}
