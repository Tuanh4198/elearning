package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.repository.QuizzCategoryRepository;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
import io.yody.yodemy.elearning.service.dto.QuizzCategoryDTO;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzAnswerMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.QUIZZANSWER;

@Component
public class QuizzHelper {

    private final MetafieldRepository metafieldRepository;
    private final QuizzAnswerRepository quizzAnswerRepository;
    private final QuizzCategoryRepository quizzCategoryRepository;

    public QuizzHelper(
        MetafieldRepository metafieldRepository,
        QuizzAnswerRepository quizzAnswerRepository,
        QuizzCategoryRepository quizzCategoryRepository
    ) {
        this.metafieldRepository = metafieldRepository;
        this.quizzAnswerRepository = quizzAnswerRepository;
        this.quizzCategoryRepository = quizzCategoryRepository;
    }

    public void enrichMetafields(List<QuizzDTO> quizzDTOS) {
        List<Long> examIds = quizzDTOS.stream().map(QuizzDTO::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafields = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(QUIZZANSWER, examIds);
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.toDto(metafields);
        Map<Long, List<MetafieldDTO>> metafieldDTOsMap = metafieldDTOS.stream().collect(Collectors.groupingBy(MetafieldDTO::getOwnerId));

        for (QuizzDTO quizzDTO : quizzDTOS) {
            if (!Objects.isNull(quizzDTO.getId())) {
                quizzDTO.setMetafields(metafieldDTOsMap.get(quizzDTO.getId()));
            }
        }
    }

    public void enrichAnswers(List<QuizzDTO> quizzDTOS) {
        List<Long> quizzIds = quizzDTOS.stream().map(QuizzDTO::getId).collect(Collectors.toList());
        List<QuizzAnswerEntity> quizzAnswerEntities = quizzAnswerRepository.findAllByRootIdIn(quizzIds);
        List<QuizzAnswerDTO> quizzAnswerDTOS = QuizzAnswerMapper.INSTANCE.entitiesToDtos(quizzAnswerEntities);
        Map<Long, List<QuizzAnswerDTO>> quizzAnswerMap = quizzAnswerDTOS.stream().collect(Collectors.groupingBy(QuizzAnswerDTO::getRootId));
        for (QuizzDTO quizzDTO : quizzDTOS) {
            if (!Objects.isNull(quizzDTO.getId())) {
                List<QuizzAnswerDTO> quizzAnswerDTOList = quizzAnswerMap.get(quizzDTO.getId());
                quizzDTO.setAnswers(quizzAnswerDTOList);
            }
        }
    }

    public void enrichCategory(List<QuizzDTO> quizzDTOS) {
        List<Long> categoryIds = quizzDTOS.stream().map(QuizzDTO::getCategoryId).collect(Collectors.toList());
        List<QuizzCategoryEntity> quizzCategoryEntities = quizzCategoryRepository.findAllById(categoryIds);
        List<QuizzCategoryDTO> quizzCategoryDTOS = QuizzCategoryMapper.INSTANCE.toDto(quizzCategoryEntities);
        Map<Long, QuizzCategoryDTO> categoryMap = quizzCategoryDTOS
            .stream()
            .collect(Collectors.toMap(QuizzCategoryDTO::getId, Function.identity()));
        for (QuizzDTO quizzDTO : quizzDTOS) {
            if (!Objects.isNull(quizzDTO.getId())) {
                QuizzCategoryDTO quizzCategoryDTO = categoryMap.get(quizzDTO.getCategoryId());
                if (!Objects.isNull(quizzCategoryDTO)) {
                    quizzDTO.setCategory(quizzCategoryDTO.getTitle());
                }
            }
        }
    }
}
