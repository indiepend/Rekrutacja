package me.dworak.rekrutacja;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.dworak.rekrutacja.github_client.GithubClient;
import me.dworak.rekrutacja.github_client.GithubFeignClient;
import me.dworak.rekrutacja.web.CustomWebConfigurer;
import me.dworak.rekrutacja.web.CustomWebHandlerExceptionResolver;
import me.dworak.rekrutacja.web.UserRepositoriesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class AppConfig {

    @Value("${application.github.api-version}")
    private String apiVersion;
    @Value("${application.github.auth-token}")
    private String authToken;

    @Bean
    public GithubClient getGithubClient(GithubFeignClient githubFeignClient) {
        return new GithubClient(githubFeignClient, apiVersion, authToken);
    }

    @Bean
    public UserRepositoriesService getUserRepositoriesService(GithubClient githubClient) {
        return new UserRepositoriesService(githubClient);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public CustomWebHandlerExceptionResolver customWebHandlerExceptionResolver(ObjectMapper objectMapper) {
        return new CustomWebHandlerExceptionResolver(objectMapper);
    }

    @Bean
    public CustomWebConfigurer customWebConfigurer(CustomWebHandlerExceptionResolver customWebHandlerExceptionResolver) {
        return new CustomWebConfigurer(customWebHandlerExceptionResolver);
    }

}
