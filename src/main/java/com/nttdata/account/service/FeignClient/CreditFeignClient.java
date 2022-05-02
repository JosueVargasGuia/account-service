package com.nttdata.account.service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.account.service.FeignClient.FallBackImpl.CreditFeignClientFallBack;
import com.nttdata.account.service.model.CreditAccount;



@FeignClient(name="${api.credit-service.uri}", fallback = CreditFeignClientFallBack.class)
public interface CreditFeignClient {

	@GetMapping("/{id}")
	CreditAccount creditfindById(@PathVariable(name = "id") Long id);
	
	@GetMapping
	List<CreditAccount> findAll();
}
