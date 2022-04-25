package com.nttdata.account.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.account.service.entity.Account;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, Long> {

}