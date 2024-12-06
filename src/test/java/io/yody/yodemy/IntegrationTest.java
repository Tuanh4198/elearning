package io.yody.yodemy;

import io.yody.yodemy.YodemyApp;
import io.yody.yodemy.config.AsyncSyncConfiguration;
import io.yody.yodemy.config.EmbeddedRedis;
import io.yody.yodemy.config.EmbeddedSQL;
import io.yody.yodemy.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { YodemyApp.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
