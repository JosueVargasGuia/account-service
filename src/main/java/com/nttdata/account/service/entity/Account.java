package com.nttdata.account.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {
	
	@Id
	private Long idAccount;
	private Long idProduct;
	private Long idCustomer;
	//private Double amount;
	@Override
	public String toString() {
		return "Account [idAccount=" + idAccount + ", idProduct=" + idProduct + ", idCustomer=" + idCustomer + "]";
	}
	 
	
	
}