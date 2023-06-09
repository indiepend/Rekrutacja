package me.dworak.rekrutacja;

import com.github.tomakehurst.wiremock.WireMockServer;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import me.dworak.rekrutacja.github_client.GithubFeignClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = IntegrationTestBase.IntegrationTestConfiguration.class)
public class IntegrationTestBase {

    @Autowired
    protected WireMockServer server;

    @Autowired
    protected WebTestClient client;

    @TestConfiguration
    static class IntegrationTestConfiguration {

        @Bean(initMethod = "start", destroyMethod = "stop")
        public WireMockServer wiremockServer() {
            return new WireMockServer(options().dynamicPort());
        }

        @Bean
        @Primary
        public GithubFeignClient testGithubFeignClient(WireMockServer server,
                                                       ObjectFactory<HttpMessageConverters> httpMessageConverter,
                                                       ObjectProvider<HttpMessageConverterCustomizer> httpMessageConverterCustomizer) {
            return Feign.builder()
                    .contract(new SpringMvcContract())
                    .encoder(new SpringEncoder(httpMessageConverter))
                    .decoder(new ResponseEntityDecoder(new SpringDecoder(httpMessageConverter, httpMessageConverterCustomizer)))
                    .options(new Request.Options())
                    .retryer(new Retryer.Default())
                    .target(GithubFeignClient.class, "http://localhost:" + server.port());
        }

    }
}
