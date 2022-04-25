package com.nttdata.account.service.service;

import java.util.Map;

import com.nttdata.account.service.entity.Account;
import com.nttdata.account.service.model.Customer;
import com.nttdata.account.service.model.MovementAccount;
import com.nttdata.account.service.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	Flux<Account> findAll();

	Mono<Account> save(Account account);

	Mono<Account> update(Account account);

	Mono<Account> findById(Long id);

	Mono<Void> delete(Long id);

	Mono<Map<String, Object>> registerAccount(Account account);

	Product findProduct(Long id);

	Customer findCustomer(Long id);

	Flux<MovementAccount> consultMovementsAccount(Long idCredit);

	Long generateKey(String nameTable);
}
