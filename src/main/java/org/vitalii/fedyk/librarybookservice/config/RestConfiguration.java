package org.vitalii.fedyk.librarybookservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vitalii.fedyk.librarybookservice.ApiClient;
import org.vitalii.fedyk.librarybookservice.client.UserApi;

@Configuration
public class RestConfiguration {
    @Value("${paths.users}")
    private String userBasePath;

    @Bean
    public UserApi userClient() {
        return new UserApi(userApiClient());
    }

    public ApiClient userApiClient() {
        final ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(userBasePath);
        return apiClient;
    }
}
