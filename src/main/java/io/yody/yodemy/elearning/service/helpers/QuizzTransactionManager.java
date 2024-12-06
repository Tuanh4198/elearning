package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.domain.QuizzEntity;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.repository.QuizzRepository;
import io.yody.yodemy.elearning.service.business.MetafieldBO;
import io.yody.yodemy.elearning.service.business.QuizzAnswerBO;
import io.yody.yodemy.elearning.service.business.QuizzBO;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzAnswerMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.QUIZZANSWER;

@Component
public class QuizzTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(QuizzTransactionManager.class);
    private final QuizzRepository quizzRepository;
    private final QuizzMapper quizzMapper;
    private final MetafieldRepository metafieldRepository;
    private final MetafieldMapper metafieldMapper;
    private final QuizzAnswerMapper quizzAnswerMapper;
    private final QuizzAnswerRepository quizzAnswerRepository;

    public QuizzTransactionManager(
        QuizzRepository quizzRepository,
        QuizzMapper quizzMapper,
        MetafieldRepository metafieldRepository,
        MetafieldMapper metafieldMapper,
        QuizzAnswerMapper quizzAnswerMapper,
        QuizzAnswerRepository quizzAnswerRepository
    ) {
        this.quizzRepository = quizzRepository;
        this.quizzMapper = quizzMapper;
        this.metafieldRepository = metafieldRepository;
        this.metafieldMapper = metafieldMapper;
        this.quizzAnswerMapper = quizzAnswerMapper;
        this.quizzAnswerRepository = quizzAnswerRepository;
    }

    private void deleteMetafields(List<MetafieldBO> metafieldBOS) {
        List<MetafieldEntity> metafields = MetafieldMapper.INSTANCE.bosToEntities(metafieldBOS);
        metafieldRepository.deleteAll(metafields);
    }

    private void deleteOrphanAnswers(List<QuizzAnswerBO> answersBOS) {
        List<QuizzAnswerEntity> orphanAnswers = QuizzAnswerMapper.INSTANCE.bosToEntities(answersBOS);
        for (QuizzAnswerEntity answer : orphanAnswers) {
            answer.setDeleted(false);
        }
        quizzAnswerRepository.saveAll(orphanAnswers);
    }

    private List<MetafieldDTO> saveMetafields(QuizzBO quizzBO, Long examId) {
        List<MetafieldBO> metafieldBOs = quizzBO.getMetafields();
        List<MetafieldEntity> metafieldEntities = metafieldMapper.bosToEntities(metafieldBOs);
        for (MetafieldEntity metafieldEntity : metafieldEntities) {
            metafieldEntity.setOwnerResource(QUIZZANSWER);
            metafieldEntity.setNamespace(QUIZZANSWER);
            metafieldEntity.setType(QUIZZANSWER);
            metafieldEntity.setOwnerId(examId);
        }
        metafieldEntities = metafieldRepository.saveAll(metafieldEntities);
        return metafieldMapper.toDto(metafieldEntities);
    }

    private List<QuizzAnswerDTO> saveAnswer(QuizzBO quizzBO, Long quizzId) {
        List<QuizzAnswerBO> answerBOs = quizzBO.getAnswers();
        if (answerBOs.isEmpty()) {
            return Collections.<QuizzAnswerDTO>emptyList();
        }
        List<QuizzAnswerEntity> answerEntities = quizzAnswerMapper.bosToEntities(answerBOs);
        for (QuizzAnswerEntity answer : answerEntities) {
            answer.setRootId(quizzId);
        }
        answerEntities = quizzAnswerRepository.saveAll(answerEntities);
        return quizzAnswerMapper.toDto(answerEntities);
    }

    @Transactional
    public QuizzDTO save(QuizzBO quizzBO) {
        QuizzEntity quizzEntity = quizzMapper.boToEntity(quizzBO);
        quizzEntity = quizzRepository.save(quizzEntity);
        if (Objects.isNull(quizzEntity.getId())) {
            log.error("Không lưu được câu hỏi %s", quizzEntity.getContent());
            return null;
        }
        List<MetafieldDTO> metafieldDTOS = saveMetafields(quizzBO, quizzEntity.getId());
        List<QuizzAnswerDTO> answerDTOS = saveAnswer(quizzBO, quizzEntity.getId());
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);
        quizzDTO.setMetafields(metafieldDTOS);
        quizzDTO.setAnswers(answerDTOS);

        return quizzDTO;
    }

    @Transactional
    public QuizzDTO update(List<MetafieldBO> oldMetafields, List<QuizzAnswerBO> orphanAnswers, QuizzBO quizzBO) {
        deleteMetafields(oldMetafields);
        deleteOrphanAnswers(orphanAnswers);
        QuizzDTO quizzDTO = save(quizzBO);

        return quizzDTO;
    }

    private void enrichQuizzMetafield(QuizzBO quizzBO) {
        Long examId = quizzBO.getId();
        List<MetafieldEntity> metafieldEntities = metafieldRepository.findAllByOwnerResourceAndOwnerId(QUIZZANSWER, examId);
        List<MetafieldBO> metafieldBOS = metafieldMapper.entitiesToBos(metafieldEntities);
        quizzBO.setMetafields(metafieldBOS);
    }
//
    private void enrichAnswers(QuizzBO quizzBO) {
        Long quizzBOId = quizzBO.getId();
        if (quizzBO.getType() == QuizzTypeEnum.MULTIPLE_CHOICE) {
            List<QuizzAnswerEntity> answerEntities = quizzAnswerRepository.findAllByRootId(quizzBOId);
            List<QuizzAnswerBO> answerBOS = quizzAnswerMapper.entitiesToBos(answerEntities);
            quizzBO.setAnswers(answerBOS);
        } else {
            quizzBO.setAnswers(Collections.<QuizzAnswerBO>emptyList());
        }
    }

    @Transactional(readOnly = true)
    public QuizzBO findById(Long id) {
        QuizzEntity quizzEntity = quizzRepository.findById(id).filter(Predicate.not(QuizzEntity::isDeleted)).orElse(null);
        if (quizzEntity == null) {
            log.error("Không tìm thấy câu hỏi");
            return null;
        }
        QuizzBO quizzBO = quizzMapper.entityToBo(quizzEntity);
        enrichQuizzMetafield(quizzBO);
        enrichAnswers(quizzBO);
        return quizzBO;
    }
}
