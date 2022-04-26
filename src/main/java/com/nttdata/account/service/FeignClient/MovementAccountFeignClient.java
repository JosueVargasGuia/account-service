package com.nttdata.account.service.FeignClient;
import org.springframework.cloud.openfeign.FeignClient; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.account.service.FeignClient.FallBackImpl.MovementAccountFeignClientFallBack;
import com.nttdata.account.service.model.MovementAccount;

import reactor.core.publisher.Flux;
 

@FeignClient(name = "${api.movement-account-service.uri}", 
 
fallback = MovementAccountFeignClientFallBack.class)
public interface MovementAccountFeignClient {
	@GetMapping("/{id}")
	Flux<MovementAccount> getOneMovementAccount(@PathVariable("id") Long id);
}
