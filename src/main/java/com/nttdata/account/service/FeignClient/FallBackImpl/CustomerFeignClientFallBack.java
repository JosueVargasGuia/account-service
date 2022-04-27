package com.nttdata.account.service.FeignClient.FallBackImpl;

 

import org.springframework.stereotype.Component;

import com.nttdata.account.service.FeignClient.CustomerFeignClient;
import com.nttdata.account.service.model.Customer;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomerFeignClientFallBack implements CustomerFeignClient {

	public Customer customerfindById(Long id) {
		// TODO Auto-generated method stub
	    //https://arnoldgalovics.com/feign-fallback/
		  Customer customer = new Customer(); 
		  customer.setIdCustomer(Long.valueOf(-1));
		  log.info("CustomerFeignClientFallBack:"+customer.toString());
		return customer;

	}

}
