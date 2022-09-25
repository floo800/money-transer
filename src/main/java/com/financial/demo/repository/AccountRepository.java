package com.financial.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.financial.demo.model.db.AccountDB;

public interface AccountRepository extends CrudRepository<AccountDB,Long> {
}
