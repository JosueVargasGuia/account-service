package com.nttdata.account.service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.account.service.entity.Account;
import com.nttdata.account.service.entity.BankAccounts;
import com.nttdata.account.service.service.AccountService;
import com.nttdata.purchaserequest.model.PurchaseRequestKafka;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PurchaseAccountConsumer {
	@Value("${api.kafka-uri.account-topic-respose}")
	String accountTopicResponse;
	@Autowired
	KafkaTemplate<String, PurchaseRequestKafka> kafkaTemplate;

	@Autowired
	AccountService accountService;

	@KafkaListener(topics = "${api.kafka-uri.account-bank-topic}", groupId = "group_id")
	public void purchaseWalletConsumer(PurchaseRequestKafka purchaseRequestKafka) {
		log.info("Mensaje recivido[purchaseWalletConsumer]:" + purchaseRequestKafka.toString());
		BankAccounts accountsFind = new BankAccounts();
		accountsFind.setIdCustomer(purchaseRequestKafka.getCustomerOrigin().getIdCustomer());
		accountsFind.setIdProduct(Long.valueOf(1));
		BankAccounts accounts = this.accountService.findByIdForExample(accountsFind).blockOptional().orElse(null);
		Long idBankAccount=Long.valueOf(0);
		if (accounts != null) {
			idBankAccount=accounts.getIdBankAccount();
		}else{
			accountsFind.setIdProduct(Long.valueOf(2));
			  accounts = this.accountService.findByIdForExample(accountsFind).blockOptional().orElse(null);
			  if(accounts!=null) {
				  idBankAccount=accounts.getIdBankAccount();
			  }
		}
		if(idBankAccount>=1) {
			purchaseRequestKafka.setIdBankAccount(idBankAccount);
			log.info("Enviando kafka de account:"+accountTopicResponse+"-->"+purchaseRequestKafka.toString());
			kafkaTemplate.send(accountTopicResponse, purchaseRequestKafka);
		}
	}
}
