package io.yody.yodemy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech.jhipster.config.JHipsterConstants;

@Configuration
@EntityScan({ "org.nentangso.core.domain", "io.yody.yodemy.domain", "io.yody.yodemy.elearning", "io.yody.yodemy.rule.knowledgeBase.domain" })
@EnableJpaRepositories({ "org.nentangso.core.repository", "io.yody.yodemy.repository", "io.yody.yodemy.elearning.repository", "io.yody.yodemy.rule.knowledgeBase.repository" })
@EnableJpaAuditing(auditorAwareRef = "ntsSpringSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {}
