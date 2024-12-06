package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.elearning.domain.NodeMetafieldEntity;
import io.yody.yodemy.elearning.domain.constant.MetafieldConstant;
import io.yody.yodemy.elearning.repository.NodeMetafieldRepository;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.service.dto.NodeMetafieldDTO;
import io.yody.yodemy.elearning.service.mapper.NodeMetafieldMapper;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NodeMetafieldHelper {

    private NodeMetafieldRepository metafieldRepo;

    public NodeMetafieldHelper(NodeMetafieldRepository metafieldRepo) {
        this.metafieldRepo = metafieldRepo;
    }

    public NodeMetafieldRepository getMetafieldRepo() {
        return metafieldRepo;
    }

    public void setMetafieldRepo(NodeMetafieldRepository metafieldRepo) {
        this.metafieldRepo = metafieldRepo;
    }

    private <MTYPE> Map<Long, List<MTYPE>> convertToMap(
        List<NodeMetafieldEntity> metafields,
        Function<List<NodeMetafieldEntity>, List<MTYPE>> mapperFunction,
        Function<MTYPE, Long> getOwnerIdFunction
    ) {
        List<MTYPE> metafieldDTOS = mapperFunction.apply(metafields);
        Map<Long, List<MTYPE>> metafieldMap = metafieldDTOS.stream().collect(Collectors.groupingBy(getOwnerIdFunction));
        return metafieldMap;
    }

    private <DTO, MTYPE> void enrichMetafields(
        List<DTO> dtos,
        Function<DTO, Long> getIdFunction,
        BiConsumer<DTO, List<MTYPE>> setMetafieldFunction,
        Function<List<NodeMetafieldEntity>, List<MTYPE>> metafieldMapperFunction,
        Function<MTYPE, Long> metafieldGetOwnerIdFunction,
        String metafieldEnum
    ) {
        List<Long> ids = dtos.stream().map(getIdFunction).collect(Collectors.toList());
        List<NodeMetafieldEntity> metafields = metafieldRepo.findAllByOwnerResourceAndOwnerIdIn(metafieldEnum, ids);
        Map<Long, List<MTYPE>> metafieldMap = convertToMap(metafields, metafieldMapperFunction, metafieldGetOwnerIdFunction);
        for (DTO dto : dtos) {
            setMetafieldFunction.accept(dto, metafieldMap.get(getIdFunction.apply(dto)));
        }
    }

    public void enrichMetafieldNode(List<NodeDTO> dtos) {
        enrichMetafields(
            dtos,
            NodeDTO::getId,
            NodeDTO::setMetafields,
            NodeMetafieldMapper.INSTANCE::toDto,
            NodeMetafieldDTO::getOwnerId,
            MetafieldConstant.NODE
        );
    }
}
