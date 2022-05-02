package com.nttdata.account.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.account.service.FeignClient.FallBackImpl.CardFeignClientFallBack;
import com.nttdata.account.service.model.Card;

@FeignClient(name = "${api.card-service.uri}", fallback = CardFeignClientFallBack.class)
public interface CardFeignClient {

	@GetMapping("/{id}")
	Card cardfindById(@PathVariable(name = "id") Long id);

}
