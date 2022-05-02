package com.nttdata.account.service.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.account.service.FeignClient.CardFeignClient;
import com.nttdata.account.service.FeignClient.ConfigurationFeignClient;
import com.nttdata.account.service.FeignClient.CreditFeignClient;
import com.nttdata.account.service.FeignClient.CustomerFeignClient;
import com.nttdata.account.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.account.service.FeignClient.ProductFeignClient;
import com.nttdata.account.service.FeignClient.TableIdFeignClient;
import com.nttdata.account.service.entity.Account;
import com.nttdata.account.service.entity.BankAccounts;
import com.nttdata.account.service.model.Card;
import com.nttdata.account.service.model.Configuration;
import com.nttdata.account.service.model.CreditAccount;
import com.nttdata.account.service.model.Customer;
import com.nttdata.account.service.model.MovementAccount;
import com.nttdata.account.service.model.Product;
import com.nttdata.account.service.model.ProductId;
import com.nttdata.account.service.model.TypeCustomer;
import com.nttdata.account.service.model.TypeProduct;
import com.nttdata.account.service.repository.AccountRepository;
import com.nttdata.account.service.service.AccountService;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository repository;

	@Autowired
	CustomerFeignClient customerFeignClient;
	@Autowired
	ProductFeignClient productFeignClient;
	@Autowired
	TableIdFeignClient tableIdFeignClient;
	@Autowired
	MovementAccountFeignClient movementAccountFeignClient;
	@Autowired
	ConfigurationFeignClient configurationfeignClient;
	@Autowired
	CardFeignClient cardFeignClient;
	@Autowired
	CreditFeignClient creditFeignClient;

	public Flux<BankAccounts> findAll() {
		return repository.findAll();
	}

	@Override
	public Mono<BankAccounts> save(BankAccounts bankAccounts) {
		Long idAccount = generateKey(Account.class.getSimpleName());
		log.info("Key:" + idAccount);
		if (idAccount >= 1) {
			bankAccounts.setIdAccount(idAccount);
		} else {
			return Mono.error(new InterruptedException("Servicio no disponible:" + Account.class.getSimpleName()));
		}
		Long idBankAccount = generateKey(BankAccounts.class.getSimpleName());
		if (idBankAccount >= 1) {
			bankAccounts.setIdBankAccount(idBankAccount);
			bankAccounts.setCreationDate(Calendar.getInstance().getTime());
		} else {
			return Mono.error(new InterruptedException("Servicio no disponible:" + BankAccounts.class.getSimpleName()));
		}
		
		return repository.insert(bankAccounts);
	}

	@Override
	public Mono<BankAccounts> update(BankAccounts bankAccounts) {
		return repository.save(bankAccounts);
	}

	@Override
	public Mono<BankAccounts> findById(Long idBankAccount) {
		return repository.findById(idBankAccount);
	}

	@Override
	public Mono<Void> delete(Long idBankAccount) {
		return repository.deleteById(idBankAccount);
	}

	@Override
	public Mono<Map<String, Object>> registerAccount(BankAccounts bankAccounts) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		Product product = findProduct(bankAccounts.getIdProduct()); // obteniendo producto para luego comparar su tipo
		Customer customer = findCustomer(bankAccounts.getIdCustomer()); // obteniendo cliente para luego comparar su tipo
		Configuration configuration = findConfiguration(product.getIdConfiguration());
		
		/*
		 * (var) devuelve 1 - 0 : 
		 * 		0 indica que hay un cliente del tipo empresarial que puede registrar cuenta Pyme
		 * 		1 indica que hay cliente del tipo personal que puede registrar cuenta Vip
		 */
		Long var = Flux.fromIterable(creditFeignClient.findAll()).filter(e-> e.getIdCustomer() == customer.getIdCustomer())
			.collect(Collectors.counting()).blockOptional().get();
		
		log.info("Variable-->"+var);
		
		if (product!=null && customer!=null) {
			log.info("Customer: " + customer.getFirstname());
			log.info("Product: " + product.getDescriptionProducto());
			if (customer.getTypeCustomer() == TypeCustomer.personal &&( product.getTypeProduct() == TypeProduct.pasivos || product.getProductId() == ProductId.Pyme)) {

				Mono<Map<String, Object>> mono = this.findAll()
						.filter(obj -> (obj.getIdCustomer() == bankAccounts.getIdCustomer()
								&& obj.getIdProduct() == bankAccounts.getIdProduct()))
						.collect(Collectors.counting()).map(e_value -> {
							log.info("Cantidad:" + e_value);
							if (e_value <= 0) {
								if (product.getProductId() == ProductId.Savings) {
									this.save(bankAccounts).subscribe(e -> log.info("Message:" + e.toString()));
									hashMap.put("Account_Savings: ", "Cuenta de Ahorro registrada con s/"+ configuration.getCostMaintenance()+ " de comisión por mantenimiento y limite de "+configuration.getQuantityMovement()+" movimientos mensuales");
								} else if (product.getProductId() == ProductId.CurrentAccount) {
									this.save(bankAccounts).subscribe(e -> log.info("Message:" + e.toString()));
									if(configuration.getQuantityMovement()==null) {
										hashMap.put("Account_CurrentAccount: ","Cuenta Corriente registrada con s/"+ configuration.getCostMaintenance()+ " de comisión por mantenimiento y sin limite de movimientos mensuales");
									}	
								} else if (product.getProductId() == ProductId.FixedTerm) {
									this.save(bankAccounts).subscribe(e -> log.info("Message:" + e.toString()));
									hashMap.put("Account_FixedTerm: ", "Cuenta a Plazo fijo registrada con s/"+ configuration.getCostMaintenance() + " de comisión por mantenimiento con limite de "+ configuration.getQuantityMovement()+ " movimiento al mes los dias "+configuration.getSpecificDate());
								}else if (product.getProductId() == ProductId.Vip) {
									if(var>0) { //si es mayor a 0 quiere decir que es cliente tipo personal
										this.save(bankAccounts).subscribe(e -> log.info("Message:" + e.toString()));
										hashMap.put("Account_VIP: ", "Cuenta VIP registrada");
									}else {
										hashMap.put("NotFound: ", "No se encontró cuenta válida");
									}
								} else {
									hashMap.put("Error_Account: ", "No se puede registrar a una cuenta empresarial");
								}
								// Falta realizar validacion cuando no cumple los if
							} else {
								hashMap.put("Account: ", "No se puede registrar una cuenta");
							}

							return hashMap;
						});
				return mono;

			} else { // si es del tipo empresarial permitir solo multiples cuentas corrientes
				if (product.getTypeProduct() == TypeProduct.pasivos) {
					if (product.getProductId() == ProductId.CurrentAccount) {
						this.save(bankAccounts).subscribe(e -> log.info("Message:" + e.toString()));
						log.info("Cliente Empresarial -> Cuenta corriente registrada.");
						hashMap.put("Account: ", "Cuenta corriente registrada.");
					} else if(product.getProductId() == ProductId.Pyme) {
						if(var==0) {
							this.save(bankAccounts).subscribe(e -> log.info("Message:" + e.toString()));
							hashMap.put("Account_Pyme: ", "Cuenta Pyme registrada");
						}else {
							hashMap.put("Failed_Account_Pyme: ", "Cuenta Pyme no es posible registrar");
						}
					}
					else {
						log.info("Cliente Empresarial -> No es posible abrir una cuenta de  "+ product.getDescriptionProducto());
						hashMap.put("Account: ","No es posible abrir una cuenta de " + product.getDescriptionProducto());
					}
				} else {
					log.info("Este servicio es para el registro de cuentas bancarias.");
					hashMap.put("Account", "Este servicio es para el registro de cuentas bancarias.");
				}
			}
		} else {
			hashMap.put("Message", "Datos no encontrados.");
		}
		return Mono.just(hashMap);
	}

	// busqueda de producto por su id
	@Override
	public Product findProduct(Long id) {
		// log.info(productService + "/" + id);
		/*
		 * ResponseEntity<Product> response = restTemplate.exchange(productService + "/"
		 * + id, HttpMethod.GET, null, new ParameterizedTypeReference<Product>() { });
		 * if (response.getStatusCode() == HttpStatus.OK) { return response.getBody(); }
		 * else { return null; }
		 */
		Product product = productFeignClient.findById(id);
		//log.info("productFeignClient:" + product.toString());
		return product;
	}

	// busqueda de cliente por id
	@Override
	public Customer findCustomer(Long id) {
		/*
		 * log.info(customerService + "/" + id); ResponseEntity<Customer> response =
		 * restTemplate.exchange(customerService + "/" + id, HttpMethod.GET, null, new
		 * ParameterizedTypeReference<Customer>() { }); if (response.getStatusCode() ==
		 * HttpStatus.OK) { return response.getBody(); } else { return null; }
		 */
		Customer cus = customerFeignClient.customerfindById(id);
		//log.info("customerFeignClient:" + cus.toString());
		return cus;
	}

	@Override
	public Flux<MovementAccount> consultMovementsAccount(Long idBankAccount) {
		
		  //log.info(movementAccountService + "/" + idAccount); // consulta de
		  //movimientos de la cuenta 
		 /* ResponseEntity<List<MovementAccount>> response =
		  restTemplate.exchange(movementAccountService, HttpMethod.GET, null, new
		  ParameterizedTypeReference<List<MovementAccount>>() { });
		  List<MovementAccount> list; if (response.getStatusCode() == HttpStatus.OK) {
		  list = response.getBody(); return
		  Flux.fromIterable(list).filter(movementAccount ->
		  movementAccount.getIdAccount() == idAccount); } else { return Flux.empty(); }
		 */
		log.info("idBankAccount:"+idBankAccount);
		List<MovementAccount> lista=movementAccountFeignClient.findAll();
		return Flux.fromIterable(lista)
				.filter(movementAccount -> movementAccount.getIdBankAccount() == idBankAccount);

	}

	@Override
	public Long generateKey(String nameTable) {
		/*
		 * log.info(tableIdService + "/generateKey/" + nameTable); ResponseEntity<Long>
		 * responseGet = restTemplate.exchange(tableIdService + "/generateKey/" +
		 * nameTable, HttpMethod.GET, null, new ParameterizedTypeReference<Long>() { });
		 * if (responseGet.getStatusCode() == HttpStatus.OK) { log.info("Body:" +
		 * responseGet.getBody()); return responseGet.getBody(); } else { return
		 * Long.valueOf(0); }
		 */
		return tableIdFeignClient.generateKey(nameTable);
	}

	@Override
	public Configuration findConfiguration(Long idConfiguration) {
		Configuration configuration = configurationfeignClient.configurationfindById(idConfiguration);
		return configuration;
	}

	@Override
	public Card findCard(Long idCard) {
		Card card = cardFeignClient.cardfindById(idCard);
		return card;
	}

	@Override
	public CreditAccount findCreditAccount(Long idCreditAccount) {
		CreditAccount creditAccount = creditFeignClient.creditfindById(idCreditAccount);
		return creditAccount;
	}
}