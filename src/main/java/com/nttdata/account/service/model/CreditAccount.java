package com.nttdata.account.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class CreditAccount extends Account {
	private Long idCreditAccount;
	private Long idProducto;
	private Double amountCreditLimit;
	private Long idAccount;
}
