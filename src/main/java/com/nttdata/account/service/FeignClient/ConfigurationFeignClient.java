package com.nttdata.account.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.account.service.FeignClient.FallBackImpl.ConfigurationFeignClientFallBack;
import com.nttdata.account.service.model.Configuration;

@FeignClient(name = "${api.configuration-service.uri}", fallback = ConfigurationFeignClientFallBack.class)
public interface ConfigurationFeignClient{

	@GetMapping("/{id}")
	Configuration configurationfindById(@PathVariable(name = "id") Long id);

}
