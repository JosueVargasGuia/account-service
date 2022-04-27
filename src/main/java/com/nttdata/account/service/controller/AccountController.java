package com.nttdata.account.service.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

//import com.netflix.discovery.EurekaClient;
import com.nttdata.account.service.entity.Account;
import com.nttdata.account.service.entity.BankAccounts;
import com.nttdata.account.service.model.Customer;
import com.nttdata.account.service.model.MovementAccount;
import com.nttdata.account.service.model.Product;
import com.nttdata.account.service.service.AccountService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<BankAccounts> findAll(){
		return service.findAll();

	}
	
	@GetMapping(value = "/{idBankAccount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<BankAccounts>> findById(@PathVariable("idBankAccount") Long idBankAccount){
		return service.findById(idBankAccount).map(_account -> ResponseEntity.ok().body(_account))
				.onErrorResume(e -> {
					log.error("Error: " + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<BankAccounts>> saveAccount(@RequestBody BankAccounts bankAccounts){
		return service.save(bankAccounts).map(_account -> ResponseEntity.ok().body(_account)).
				
				onErrorResume(e -> {
			log.error("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
			//return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"BAD_REQUEST:"+e.getMessage()));
		});
	}
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Account>> updateAccount(@RequestBody BankAccounts bankAccounts){
		Mono<Account> objAccount = service.findById(bankAccounts.getIdAccount()).flatMap(_act -> {
			log.info("Update: [new] " + bankAccounts + " [Old]: " + _act);
			return service.update(bankAccounts);
		});
		//objAccount.subscribe();
		return objAccount.map(_account -> {
			log.info("Status: " + HttpStatus.OK);
			return ResponseEntity.ok().body(_account);
		}).onErrorResume(e -> {
			log.error("Status: " + HttpStatus.BAD_REQUEST + " Message:  " + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	@DeleteMapping(value = "/{idBankAccount}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable("idBankAccount") Long idBankAccount){
		return service.findById(idBankAccount).flatMap(account -> {
			return service.delete(account.getIdAccount()).then(Mono.just(ResponseEntity.ok().build()));
		});
	}
 
	//@Autowired 
	//EurekaClient eurekaClient;
	
	@PostMapping(value = "/registerAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	public Mono<ResponseEntity<Map<String, Object>>> registerAccount(@RequestBody BankAccounts bankAccounts) {
		return service.registerAccount(bankAccounts).
				map(_object ->{ 
					//_object.put("IntanceName", eurekaClient.getApplicationInfoManager().getInfo().getInstanceId());
				return ResponseEntity.ok().body(_object);})
				.onErrorResume(e -> {
					log.error("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	
	@GetMapping(value = "/consultMovementsAccount/{idAccount}")
	public Flux<MovementAccount> consultMovementsAccount(@PathVariable("idAccount") Long idAccount) {
		return service.consultMovementsAccount(idAccount).onErrorResume(e -> {
			log.error("Error:" + e.getMessage());
			return Mono.error(e);
		});
	}
	
	
	/*@GetMapping(value = "/findProduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product find(@PathVariable("id") Long id) {
		return service.findProduct(id);
	}
	
	@GetMapping(value = "/findCustomer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Customer findCustomer(@PathVariable("id") Long id) {
		return service.findCustomer(id);
	}*/
}