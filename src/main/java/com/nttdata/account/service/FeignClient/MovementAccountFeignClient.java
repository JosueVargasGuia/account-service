package com.nttdata.account.service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.nttdata.account.service.FeignClient.FallBackImpl.MovementAccountFeignClientFallBack;
import com.nttdata.account.service.model.MovementAccount;

@FeignClient(name = "${api.movement-account-service.uri}", fallback = MovementAccountFeignClientFallBack.class)
public interface MovementAccountFeignClient {
	@GetMapping
	List<MovementAccount> findAll();
}
