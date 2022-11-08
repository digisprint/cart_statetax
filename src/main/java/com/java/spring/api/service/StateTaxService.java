package com.java.spring.api.service;

import com.java.spring.api.entity.StateTax;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StateTaxService {

	Flux<StateTax> getAllStateTaxes();

	Mono<StateTax> getByStateTaxId(Integer id);

	Mono<StateTax> createStateTax(Mono<StateTax> stateTax);

	Flux<StateTax> createStateTaxes(Iterable<StateTax> stateTaxes);

	Mono<StateTax> updateStateTax(Mono<StateTax> stateTax, Integer id);

	Mono<Void> deleteByStateTaxId(Integer id);

	Mono<Void> deleteAllStateTaxes();

	Flux<StateTax> createConfigStateTax();

	Flux<StateTax> getByStateTaxName(String stateName);

}
