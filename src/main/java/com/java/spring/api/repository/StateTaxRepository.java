package com.java.spring.api.repository;




import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.java.spring.api.entity.CartEntity;
import com.java.spring.api.entity.StateTax;
import com.java.spring.api.entity.Tax;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StateTaxRepository extends ReactiveMongoRepository<StateTax, Integer>{

	/*
	 * Mono<Tax> save(Mono<Tax> tax);
	 * 
	 * Flux<Tax> saveAll(Flux<List<Tax>> taxes);
	 * 
	 * Mono<Tax> findById(String id);
	 */

	//Optional<Tax> findByCountryCodeAndStateCode(String countryCode, String stateCode);
	//Tax findByCountryCodeAndStateCodeAndCityCodeAndCountyCode();
	//Tax findByCountryCodeAndStateCodeAndCityCodeAndCountyCode(String countryCode, String stateCode, String cityCode, String countyCode);
	//Mono<Tax> findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(String countryCode, String stateCode, String cityCode, String countyCode);

	//Mono<StateTax> findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(String countryCode, String stateCode, String cityCode, String countyCode);

	Mono<StateTax> findByStateCodeIgnoreCase(String stateCode);

	Flux<StateTax> findByStateNameIgnoreCase(String stateName);

}
