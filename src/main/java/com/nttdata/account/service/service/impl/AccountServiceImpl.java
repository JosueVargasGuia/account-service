package com.nttdata.account.service.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.nttdata.account.service.FeignClient.CustomerFeignClient;
import com.nttdata.account.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.account.service.FeignClient.ProductFeignClient;
import com.nttdata.account.service.FeignClient.TableIdFeignClient;
import com.nttdata.account.service.entity.Account;
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

	// Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

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
	RestTemplate restTemplate;

	// @Value("${api.customer-service.uri}")
	// private String customerService;

	//@Value("${api.movement-account-service.uri}")
	//private String movementAccountService;

	public Flux<Account> findAll() {
		return repository.findAll();
	}

	@Override
	public Mono<Account> save(Account account) {
		Long key = generateKey(Account.class.getSimpleName());
		log.info("Key:"+key);
		if (key >= 1) {			
			account.setIdAccount(key);
		} else {
			return Mono.error(new InterruptedException("Servicio no disponible:" + Account.class.getSimpleName()));			
		}
		return repository.insert(account);
	}

	@Override
	public Mono<Account> update(Account account) {
		return repository.save(account);
	}

	@Override
	public Mono<Account> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Void> delete(Long id) {
		return repository.deleteById(id);
	}

	@Override
	public Mono<Map<String, Object>> registerAccount(Account account) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		Product product = findProduct(account.getIdProduct()); // obteniendo producto para luego comparar su tipo
		Customer customer = findCustomer(account.getIdCustomer()); // obteniendo cliente para luego comparar su tipo

		if (product.getIdProducto() >= 1 && customer.getId() >= 1) {
			log.info("Customer: " + customer.getFirstname());
			log.info("Product: " + product.getDescriptionProducto());
			if (customer.getTypeCustomer() == TypeCustomer.personal
					&& product.getTypeProduct() == TypeProduct.pasivos) {

				Mono<Map<String, Object>> mono = this.findAll()
						.filter(obj -> (obj.getIdCustomer() == account.getIdCustomer()
								&& obj.getIdProduct() == account.getIdProduct()))
						.collect(Collectors.counting()).map(e_value -> {
							log.info("Cantidad:" + e_value);
							if (e_value <= 0) {
								if (product.getProductId() == ProductId.Ahorro) {
									this.save(account).subscribe(e -> log.info("Message:" + e.toString()));
									hashMap.put("Account: ", "Cuenta de Ahorro registrada");
								}
								if (product.getProductId() == ProductId.CuentaCorriente) {
									this.save(account).subscribe(e -> log.info("Message:" + e.toString()));
									hashMap.put("Account: ", "Cuenta corriente registrada");
								}
								if (product.getProductId() == ProductId.PlazoFijo) {
									this.save(account).subscribe(e -> log.info("Message:" + e.toString()));
									hashMap.put("Account: ", "Cuenta a Plazo fijo registrada");
								}
								//Falta realizar validacion cuando no cumple los if
							} else {
								hashMap.put("Account: ", "No se puede registrar una cuenta");
							}
							
							return hashMap;
						});
				return mono;

			} else { // si es del tipo empresarial permitir solo multiples cuentas corrientes
				if (product.getTypeProduct() == TypeProduct.pasivos) {
					if (product.getProductId() == ProductId.CuentaCorriente) {
						this.save(account).subscribe(e -> log.info("Message:" + e.toString()));
						log.info("Cliente Empresarial -> Cuenta corriente registrada.");
						hashMap.put("Account: ", "Cuenta corriente registrada.");
					} else {
						log.info("Cliente Empresarial -> No es posible abrir una cuenta de  "
								+ product.getDescriptionProducto());
						hashMap.put("Account: ",
								"No es posible abrir una cuenta de " + product.getDescriptionProducto());
					}
				} else {
					log.info("Este servicio es para el registro de cuentas bancarias.");
					hashMap.put("Account", "Este servicio es para el registro de cuentas bancarias.");
				}
			}
		} else {
			if (product.getIdProducto() <= 0) {
				hashMap.put("Product", "El producto no existe.");
			}
			if (customer.getId() <= 0) {
				hashMap.put("Customer", "El cliente no existe.");
			}
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
		log.info("productFeignClient:" + product.toString());
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
		log.info("customerFeignClient:" + cus.toString());
		return cus;
	}

	@Override
	public 	Flux<MovementAccount> consultMovementsAccount(Long idAccount) {
		/*log.info(movementAccountService + "/" + idAccount);
		// consulta de movimientos de la cuenta
		ResponseEntity<List<MovementAccount>> response = restTemplate.exchange(movementAccountService, HttpMethod.GET,
				null, new ParameterizedTypeReference<List<MovementAccount>>() {
				});
		List<MovementAccount> list;
		if (response.getStatusCode() == HttpStatus.OK) {
			list = response.getBody();
			return Flux.fromIterable(list).filter(movementAccount -> movementAccount.getIdAccount() == idAccount);
		} else {
			return Flux.empty();
		}*/
		return movementAccountFeignClient.getOneMovementAccount(idAccount)
				.filter(movementAccount -> movementAccount.getIdAccount() == idAccount);			
				 
		 
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
}