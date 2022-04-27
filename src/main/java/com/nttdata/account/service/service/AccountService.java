package com.nttdata.account.service.service;

import java.util.Map;

import com.nttdata.account.service.entity.Account;
import com.nttdata.account.service.entity.BankAccounts;
import com.nttdata.account.service.model.Customer;
import com.nttdata.account.service.model.MovementAccount;
import com.nttdata.account.service.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	Flux<BankAccounts> findAll();

	Mono<BankAccounts> save(BankAccounts bankAccounts);

	Mono<BankAccounts> update(BankAccounts bankAccounts);

	Mono<BankAccounts> findById(Long idBankAccount);

	Mono<Void> delete(Long idBankAccount);

	Mono<Map<String, Object>> registerAccount(BankAccounts bankAccounts);

	Product findProduct(Long idProducto);

	Customer findCustomer(Long id);

	Flux<MovementAccount> consultMovementsAccount(Long idBankAccount);

	Long generateKey(String nameTable);
}
