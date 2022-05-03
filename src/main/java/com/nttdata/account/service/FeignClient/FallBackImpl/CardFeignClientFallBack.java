package com.nttdata.account.service.FeignClient.FallBackImpl;

import org.springframework.stereotype.Component;
import com.nttdata.account.service.FeignClient.CardFeignClient;
import com.nttdata.account.service.model.Card;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CardFeignClientFallBack implements CardFeignClient {

	public Card cardfindById(Long id) {
		log.info("CardFeignClientFallBack:" + id);
		return null;

	}

}
