package com.nttdata.account.service.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementAccount {
	
	private Long idMovementAccount;
	private Double amount;
	private Date dateMovementAccount;
	private TypeMovementAccount typeMovementAccount;
	private Long idAccount;
	
	@Override
	public String toString() {
		return "MovementAccount [idMovementAccount=" + idMovementAccount + ", amount=" + amount
				+ ", dateMovementAccount=" + dateMovementAccount + ", typeMovementAccount=" + typeMovementAccount
				+ ", idAccount=" + idAccount + "]";
	}

}
