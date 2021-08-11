package edu.bu.cs673.stockportfolio.service.utilities;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**********************************************************************************************************************
 * The IexCloudConfig class configures this application with an IEX Cloud API Key.
 *********************************************************************************************************************/
@Configuration
@PropertySource("classpath:secrets.properties")
public class IexCloudConfig {
    private static final String IEX_CLOUD = "IexCloudApiKey";
    private final String apiKey;

    /**
     * Creates an IEXCloudConfig.
     * Looks for the api key in the IexCloudApiKey.
     * 
     * @param env the environment (autowired by Spring).
     */
    public IexCloudConfig(Environment env) {
        apiKey = env.getProperty(IEX_CLOUD);
    }

    /**
     * Creates a string bean containing the api key.
     * 
     * @return the api key.
     */
    @Bean
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Creates a {@link org.springframework.web.client.RestTemplate RestTemplate} bean,
     * for autowiring.
     * 
     * @param builder the builder.
     * @return an instance of a {@code RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
