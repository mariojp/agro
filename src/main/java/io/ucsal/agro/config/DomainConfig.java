package io.ucsal.agro.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.ucsal.agro.domain")
@EnableJpaRepositories("io.ucsal.agro.repos")
@EnableTransactionManagement
public class DomainConfig {
}
