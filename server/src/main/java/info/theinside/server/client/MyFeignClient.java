package info.theinside.server.client;

import info.theinside.server.client.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "myFeignClient", url = "${test-token-service.url}")
public interface MyFeignClient {

    @GetMapping(value = "/authenticate")
    TokenResponse checkAuthentication(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "name") String name);
}
