package com.nttdata.account.service.FeignClient.FallBackImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nttdata.account.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.account.service.model.MovementAccount;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
@Component
public class MovementAccountFeignClientFallBack implements MovementAccountFeignClient {
	 
	public Flux<MovementAccount> getOneMovementAccount(Long id) {
		//MovementAccount movementAccount = new MovementAccount();
		log.info("MovementAccountFeignClientFallBack:empty"   );
		return Flux.empty();
	}

}
