package com.nttdata.account.service.model;

import java.util.Date; 
import com.fasterxml.jackson.annotation.JsonFormat; 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString 
public class MovementAccount {
	//@Id
	private Long idMovementAccount;
	private Long idBankAccount;
	private Long idCustomer;
	private Double amount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") 
	private Date dateMovementAccount;
	private TypeMovementAccount typeMovementAccount;
	private Double commissionForTransaction;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") 
	private Date creationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dateModified;
}
