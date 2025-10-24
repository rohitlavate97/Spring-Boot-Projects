package com.alchemist.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("43-GREET-API")
public interface GreetFeignClient {
	@GetMapping("/greet")
	public String invokeGreetApi();
}
