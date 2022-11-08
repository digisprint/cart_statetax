package com.java.spring.api.service;

import java.util.List;

import com.java.spring.api.entity.CartEntity;
import com.java.spring.api.entity.Tax;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaxService {

	Flux<Tax> getAllTaxes();

	Mono<Tax> getByTaxId(Integer id);

	Mono<Tax> createTax(Mono<Tax> tax);

	Flux<Tax> createTaxes(Iterable<Tax> taxes);

	Mono<Tax> updateTax(Mono<Tax> tax, Integer id);

	Mono<Void> deleteByTaxId(Integer id);

	Mono<Void> deleteAllTaxes();

	Mono<Tax> getTax();

	Flux<Tax> createConfigTax();
	
	
	
	Iterable<Tax> getAllTaxes1();
	Tax getTaxByCountyCode1(String countyCode);

}
