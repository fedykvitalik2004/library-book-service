package org.vitalii.fedyk.librarybookservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vitalii.fedyk.librarybookservice.dto.ReadUserDto;

@FeignClient(name = "LIBRARY-USER-SERVICE")
public interface UserClient {
    @GetMapping("/users/exists/{userId}")
    boolean existsById(@PathVariable Long userId);
    @GetMapping("/users/{userId}")
    ReadUserDto getUserById(@PathVariable Long userId);
}