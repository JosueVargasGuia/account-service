package com.nttdata.account.service.FeignClient.FallBackImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.account.service.FeignClient.CreditFeignClient;
import com.nttdata.account.service.model.CreditAccount;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CreditFeignClientFallBack implements CreditFeignClient {
	
	@Value("${api.credit-service.uri}")
	String creditFeignClient;

	@Override
	public CreditAccount creditfindById(Long id) {
		log.info("CreditFeignClientFallBack ->" + creditFeignClient + "/" + id);
		return null;
	}

	@Override
	public List<CreditAccount> findAll() {
		log.info("CreditFeignClientFallBack ->" + creditFeignClient);
		return new ArrayList<>();
	}

}
