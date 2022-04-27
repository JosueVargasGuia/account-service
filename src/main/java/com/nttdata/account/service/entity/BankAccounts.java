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
 @Document(collection = "BankAccounts")
 public final class BankAccounts extends Account{
	@Id
	private Long idBankAccount;
	private Long idProduct;
	//private Long idAccount;
}
