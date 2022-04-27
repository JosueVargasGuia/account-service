package com.nttdata.account.service.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data 
public class Product {
	@Id
	private Long idProducto;
	private String descriptionProducto;
	private TypeProduct typeProduct;
	private Long idConfiguration;
	private ProductId productId;
	private Date creationDate;
	private Date dateModified;
	
}
