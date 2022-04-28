package com.nttdata.account.service.FeignClient.FallBackImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.account.service.FeignClient.ProductFeignClient;
import com.nttdata.account.service.model.Product;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ProductFeignClientFallBack implements ProductFeignClient {

	@Value("${api.product-service.uri}")
	private String productService;
	public Product findById(Long id) {
		//Product product = new Product();
		//product.setIdProducto(Long.valueOf(-1));
		log.info("ProductFeignClientFallBack["+productService+"/"+id+"]:" );
		return null;
	}

}
