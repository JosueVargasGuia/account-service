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

public class Customer {
	@Id
	private Long idCustomer;
	private String firstname;
	private String lastname;
	private String emailAddress;
	private String phoneNumber;
	private String homeAddress;
	private TypeDocument typeDocument;
	private String documentNumber;
	private TypeCustomer typeCustomer;
	private Date creationDate;
	private Date dateModified;
}
