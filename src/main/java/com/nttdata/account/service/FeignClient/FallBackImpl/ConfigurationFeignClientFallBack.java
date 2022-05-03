package com.nttdata.account.service.FeignClient.FallBackImpl;

 

import org.springframework.stereotype.Component;

import com.nttdata.account.service.FeignClient.ConfigurationFeignClient;
import com.nttdata.account.service.model.Configuration;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ConfigurationFeignClientFallBack implements ConfigurationFeignClient {

	public Configuration configurationfindById(Long id) {
		  log.info("ConfigurationFeignClientFallBack: "+id);
		return null;

	}

}
