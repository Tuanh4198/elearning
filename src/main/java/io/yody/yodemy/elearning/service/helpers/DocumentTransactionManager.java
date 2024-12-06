package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import org.springframework.stereotype.Component;

@Component
public class DocumentTransactionManager {
    private final MetafieldMapper metafieldMapper;
    private final MetafieldRepository metafieldRepository;

    public DocumentTransactionManager(
        MetafieldMapper metafieldMapper,
        MetafieldRepository metafieldRepository
    ) {
        this.metafieldMapper = metafieldMapper;
        this.metafieldRepository = metafieldRepository;
    }
}
