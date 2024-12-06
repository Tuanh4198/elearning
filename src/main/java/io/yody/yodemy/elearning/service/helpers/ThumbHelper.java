package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.service.MediaProvider;
import io.yody.yodemy.service.MediaProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ThumbHelper {
    private final Logger log = LoggerFactory.getLogger(ThumbHelper.class);
    private MediaProviderFactory mediaProviderFactory;

    public ThumbHelper(MediaProviderFactory mediaProviderFactory) {
        this.mediaProviderFactory = mediaProviderFactory;
    }

    public <DTO> void enrichThumb(List<DTO> dtos, Function<DTO, String> getThumbFunction, BiConsumer<DTO, String> setThumbUrl) {
        try {
            MediaProvider mediaProvider = mediaProviderFactory.getMediaProvider();
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            List<CompletableFuture<Void>> futures = dtos.stream()
                .map(dto -> CompletableFuture.runAsync(() -> {
                    String thumbUrl = getThumbFunction.apply(dto);
                    if (!ObjectUtils.isEmpty(thumbUrl)) {
                        String presignedThumbUrl = mediaProvider.createPreSignedGetUrl(thumbUrl);
                        setThumbUrl.accept(dto, presignedThumbUrl);
                    }
                }, executorService))
                .collect(Collectors.toList());

            // Wait for all tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            executorService.shutdown(); // Shut down the executor service
        } catch (Exception e) {
            log.error("enrich thumb err", e.getMessage());
        }
    }
}
