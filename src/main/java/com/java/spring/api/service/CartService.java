package com.java.spring.api.service;

import com.java.spring.api.entity.CartEntity;
import com.java.spring.api.exception.ResourceNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {

	Mono<CartEntity> createCart(Mono<CartEntity> cartEntity) throws ResourceNotFoundException;
	
	Mono<CartEntity> createCartByCodes(CartEntity cartEntity) throws ResourceNotFoundException;

	Mono<CartEntity> findById(Integer id);

	Flux<CartEntity> createMultiCart(Iterable<CartEntity> cartEntityList) throws ResourceNotFoundException;

	Mono<CartEntity> updateByCartId(Mono<CartEntity> cartEntity, Integer id);

	Mono<Void> deleteByCartId(Integer id);

	Flux<CartEntity> getAllCarts();

	Mono<Object> getTaxByCart(CartEntity cartEntity, Integer id);

	Mono<Void> deleteAllCarts();

	Mono<Object> getShippingCostByCart(Integer id);

	Mono<CartEntity> createCartByStateTax(CartEntity cartEntity);

	//ShippingAddress getShippingAddress(Integer id);

}
