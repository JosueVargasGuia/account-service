package com.nttdata.account.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
	
	private Long idProducto;
	private ProductId productId;
	private String descriptionProducto;
	private TypeProduct typeProduct;
	private Long idConfiguration;
	
 
}
