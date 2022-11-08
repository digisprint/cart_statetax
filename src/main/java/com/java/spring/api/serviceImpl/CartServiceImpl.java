package com.java.spring.api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.java.spring.api.entity.CartEntity;
import com.java.spring.api.entity.ShipToAddress;
import com.java.spring.api.exception.ResourceNotFoundException;
import com.java.spring.api.repository.CartRepository;
import com.java.spring.api.repository.ShippingCostRepository;
import com.java.spring.api.repository.StateTaxRepository;
import com.java.spring.api.repository.TaxRepository;
import com.java.spring.api.service.CartService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CartServiceImpl implements CartService {
	
	Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private TaxRepository taxRepository;
	
	@Autowired
	private StateTaxRepository stateTaxRepository;
	
	@Autowired
	private ShippingCostRepository shippingCostRepository;
	
	@Value("${default.tax.value}")
	private double defaultTaxValue;
	
	@Value("${default.shippingCost.value}")
	private double defaultShippingCostValue;

	@Override
	public Mono<CartEntity> createCart(Mono<CartEntity> cartEntity) throws ResourceNotFoundException {
		// working with no tax
		return cartEntity.map(CartServiceImpl::monoToDao)
				.flatMap(cartRepository::save);
	}
	
	// working
	//@Override
	//public Mono<CartEntity> createCart(CartEntity cartEntity) throws ResourceNotFoundException {
	//	ShipToAddress shippingAddress = cartEntity.getShippingAddress();
	//	return taxRepository.findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(
	//				shippingAddress.getCountryCode(), shippingAddress.getStateCode(),
	//				shippingAddress.getCityCode(), shippingAddress.getCountyCode())
	//	.flatMap(fluxUsingWhen -> {
	//			cartEntity.setCountryRate(fluxUsingWhen.getCountryRate());
	//			cartEntity.setStateRate(fluxUsingWhen.getStateRate());
	//			cartEntity.setCityRate(fluxUsingWhen.getCityRate());
	//			cartEntity.setCountyRate(fluxUsingWhen.getCountyRate());
	//			cartEntity.setTaxCost(cartEntity.getCountryRate()+cartEntity.getStateRate()+cartEntity.getCityRate()+cartEntity.getCountyRate());
	//			cartEntity.setTotal(cartEntity.getSubTotal()+cartEntity.getTaxCost());
	//			return cartRepository.save(cartEntity);
	//		});
	//}
	
	@Override
	public Mono<CartEntity> createCartByCodes(CartEntity cartEntity) throws ResourceNotFoundException {
		ShipToAddress shippingAddress = cartEntity.getShippingAddress();
		return taxRepository.findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(
					shippingAddress.getCountryCode(), shippingAddress.getStateCode(),
					shippingAddress.getCityCode(), shippingAddress.getCountyCode())
		.flatMap(fluxUsingWhen -> {
				cartEntity.setCountryRate(fluxUsingWhen.getCountryRate());
				cartEntity.setStateRate(fluxUsingWhen.getStateRate());
				cartEntity.setCityRate(fluxUsingWhen.getCityRate());
				cartEntity.setCountyRate(fluxUsingWhen.getCountyRate());
				cartEntity.setSubTotal(cartEntity.getAllItemsPrice());
				cartEntity.setTaxCost(cartEntity.getCountryRate()+cartEntity.getStateRate()+cartEntity.getCityRate()+cartEntity.getCountyRate());
				return cartRepository.save(cartEntity);
			})
		.flatMap(cart -> {
			return shippingCostRepository.findByZipCode(shippingAddress.getZipCode())
					.flatMap(shippingCost -> {
						cartEntity.setShippingCost(shippingCost.getShippingCost());
						//cartEntity.setSubTotal(cartEntity.getAllItemsPrice()+cartEntity.getShippingCost());
						cartEntity.setTotal(cartEntity.getSubTotal()+cartEntity.getShippingCost()+cartEntity.getTaxCost());
						return cartRepository.save(cartEntity);
					})
					.onErrorResume(err -> {
						System.out.println("onErrorResume : "+err);
						cartEntity.setShippingCost(defaultShippingCostValue);
						cartEntity.setSubTotal(cartEntity.getAllItemsPrice()+cartEntity.getShippingCost());
						cartEntity.setTotal(cartEntity.getSubTotal()+cartEntity.getTaxCost());
						return cartRepository.save(cartEntity);
					});
		});
	}
	
	@Override
	public Mono<CartEntity> createCartByStateTax(CartEntity cartEntity) {
		ShipToAddress shippingAddress = cartEntity.getShippingAddress();
		return stateTaxRepository.findByStateCodeIgnoreCase(
					shippingAddress.getStateCode())
		.flatMap(fluxUsingWhen -> {
				System.out.println("fluxUsingWhen.getId() : "+fluxUsingWhen.getId());
				System.out.println("fluxUsingWhen.getStateName() : "+fluxUsingWhen.getStateName());
				System.out.println("fluxUsingWhen.getStateCode() : "+fluxUsingWhen.getStateCode());
				System.out.println("fluxUsingWhen.getStateTaxRate() : "+fluxUsingWhen.getStateTaxRate());
				System.out.println("fluxUsingWhen.isApplyTax() : "+fluxUsingWhen.isApplyTax());
			
				cartEntity.setCountryRate(0.0);
				cartEntity.setStateRate(0.0);
				cartEntity.setCityRate(0.0);
				cartEntity.setCountyRate(0.0);
				cartEntity.setSubTotal(cartEntity.getAllItemsPrice());
				if(fluxUsingWhen.isApplyTax()) {
					cartEntity.setTaxCost(cartEntity.getAllItemsPrice()*fluxUsingWhen.getStateTaxRate()/100);
				} else {
					cartEntity.setTaxCost(defaultTaxValue);
					cartEntity.setDefaultTaxCost(true);
				}
				return cartRepository.save(cartEntity);
			})
		.flatMap(cart -> {
			return shippingCostRepository.findByZipCode(shippingAddress.getZipCode())
					.flatMap(shippingCost -> {
						cartEntity.setShippingCost(shippingCost.getShippingCost());
						//cartEntity.setSubTotal(cartEntity.getAllItemsPrice()+cartEntity.getShippingCost());
						cartEntity.setTotal(cartEntity.getSubTotal()+cartEntity.getShippingCost()+cartEntity.getTaxCost());
						return cartRepository.save(cartEntity);
					});
		});
	}
	
	@Override
	public Flux<CartEntity> createMultiCart(Iterable<CartEntity> cartEntityList) throws ResourceNotFoundException {
		return Flux.fromIterable(cartEntityList)
				.flatMap(cartEntity -> {
					ShipToAddress shippingAddress = cartEntity.getShippingAddress();
					return taxRepository.findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(
							shippingAddress.getCountryCode(), shippingAddress.getStateCode(),
							shippingAddress.getCityCode(), shippingAddress.getCountyCode())
							.flatMap(fluxUsingWhen -> {
								cartEntity.setCountryRate(fluxUsingWhen.getCountryRate());
								cartEntity.setStateRate(fluxUsingWhen.getStateRate());
								cartEntity.setCityRate(fluxUsingWhen.getCityRate());
								cartEntity.setCountyRate(fluxUsingWhen.getCountyRate());
								cartEntity.setSubTotal(cartEntity.getAllItemsPrice());
								cartEntity.setTaxCost(cartEntity.getCountryRate()+cartEntity.getStateRate()+cartEntity.getCityRate()+cartEntity.getCountyRate());
								return cartRepository.save(cartEntity);
							})
							.flatMap(cart -> {
								return shippingCostRepository.findByZipCode(shippingAddress.getZipCode())
										.flatMap(shippingCost -> {
											cartEntity.setShippingCost(shippingCost.getShippingCost());
											//cartEntity.setSubTotal(cartEntity.getAllItemsPrice()+cartEntity.getShippingCost());
											cartEntity.setTotal(cartEntity.getSubTotal()+cartEntity.getShippingCost()+cartEntity.getTaxCost());
											return cartRepository.save(cartEntity);
										});
							});
					
				});
		
		
		// working fine
		//return cartRepository.saveAll(cartEntityList);
	}

	@Override
	public Mono<CartEntity> findById(Integer id) {
		return cartRepository.findById(id);
	}

	@Override
	public Mono<CartEntity> updateByCartId(Mono<CartEntity> cartEntity, Integer id) {
		return cartRepository.findById(id)
				.flatMap(p->cartEntity.map(CartServiceImpl::dtoToEntity))
				.doOnNext(e->e.setCartId(id))
				.flatMap(cartRepository::save);
				//.map(AppUtils::entityToDto);
		
		//Function<Mono<CartEntity>, Integer> func = data->data.filter(s->s.)
		//Function<Flux<String>, Flux<String>> filterData = null;
		//.filter(s->s.length() > 3);
		//Function<Flux<String>, Flux<String>> filterData = data -> data.filter(s -> s.length() > 3);
		
		// working but adding new ones
		//return cartEntity.map(CartServiceImpl::monoToDao).flatMap(cartRepository::save);
	}

	@Override
	public Mono<Void> deleteByCartId(Integer id) {
		return cartRepository.deleteById(id);
	}
	
	@Override
	public Mono<Void> deleteAllCarts() {
		return cartRepository.deleteAll();
	}

	@Override
	public Flux<CartEntity> getAllCarts() {
		return cartRepository.findAll();
	}

	@Override
	public Mono<Object> getTaxByCart(CartEntity cartEntity, Integer id) {
		return cartRepository.findById(id)
				.flatMap(c -> {
					System.out.println("query: "+taxRepository.findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(
							c.getShippingAddress().getCountryCode(), c.getShippingAddress().getStateCode(),
							c.getShippingAddress().getCityCode(), c.getShippingAddress().getCountyCode()).getClass().getName());
					return taxRepository.findByCountryCodeAndStateCodeAndCityCodeAndCountyCodeIgnoreCase(
							c.getShippingAddress().getCountryCode(), c.getShippingAddress().getStateCode(),
							c.getShippingAddress().getCityCode(), c.getShippingAddress().getCountyCode())
							.flatMap(fluxUsingWhen -> {
								System.out.println("fluxUsingWhen:"+fluxUsingWhen);
								//Predicate<Object> p1 = i -> (i instanceof reactor.core.publisher.MonoNext);
								//if(p1.test(fluxUsingWhen)) {
								//	System.out.println("fluxUsingWhen:"+fluxUsingWhen);
								//}
								Predicate<Object> p = i -> (i == null);
								if(!p.test(fluxUsingWhen)) {
									System.out.println("--if--");
									c.setCountryRate(fluxUsingWhen.getCountryRate());
									c.setStateRate(fluxUsingWhen.getStateRate());
									c.setCityRate(fluxUsingWhen.getCityRate());
									c.setCountyRate(fluxUsingWhen.getCountyRate());
									c.setTaxCost(c.getCountryRate()+c.getStateRate()+c.getCityRate()+c.getCountyRate());
									c.setTotal(c.getSubTotal()+c.getTaxCost());

									log.debug("------------------------------------------------------------");
									log.debug("c.getCountryRate():",c.getCountryRate());
									log.debug("c.getStateRate()  :",c.getStateRate());
									log.debug("c.getCityRate()   :",c.getCityRate());
									log.debug("c.getCountyRate() :",c.getCountyRate());
									log.debug("c.getTaxCost()    :",c.getTaxCost());
									log.debug("c.getTotal()      :",c.getTotal());
									log.debug("------------------------------------------------------------");

									System.out.println("------------------------------------------------------------");
									System.out.println("c.getCountryRate():"+c.getCountryRate());
									System.out.println("c.getStateRate()  :"+c.getStateRate());
									System.out.println("c.getCityRate()   :"+c.getCityRate());
									System.out.println("c.getCountyRate() :"+c.getCountyRate());
									System.out.println("c.getTaxCost()    :"+c.getTaxCost());
									System.out.println("c.getTotal()      :"+c.getTotal());
									System.out.println("------------------------------------------------------------");
									return Mono.just(c.getTaxCost());
								} else {
									System.out.println("else---");
									return Mono.error(new RuntimeException("Product is out of stock: "));
									// default tax - code
									/*
									 * c.setCountryRate(0); c.setStateRate(0); c.setCityRate(0); c.setCountyRate(0);
									 * c.setTaxCost(50); c.setTotal(c.getSubTotal()+c.getTaxCost());
									 * c.setDefaultTaxCost(true); return Mono.just(c.getTaxCost());
									 */
									}
								})
								.defaultIfEmpty(defaultTaxValue);
					
				});
	}
	
	@Override
	public Mono<Object> getShippingCostByCart(Integer id) {
		return cartRepository.findById(id)
				.flatMap(c -> {
					System.out.println("query: "+shippingCostRepository.findByZipCode(c.getShippingAddress().getZipCode()));
					return shippingCostRepository.findByZipCode(c.getShippingAddress().getZipCode())
							.flatMap(shippingCost -> {
								System.out.println("shippingCost:"+shippingCost.getShippingCost());
								
								Predicate<Object> p = i -> (i == null);
								if(!p.test(shippingCost)) {
									System.out.println("--if--");
									c.setShippingCost(shippingCost.getShippingCost());
									System.out.println("shippingCost.getShippingCost() :::"+shippingCost.getShippingCost());
									log.debug("c.getShippingCost():",c.getShippingCost());
									return Mono.just(c.getShippingCost());
								} else {
									System.out.println("else---");
									return Mono.error(new RuntimeException("Shipping Cost Not Available!!!"));
									}
								})
								.defaultIfEmpty(defaultShippingCostValue);
				});
	}
	
	
	
	private static CartEntity monoToDao(CartEntity cartEntity) {
		return cartEntity;
	}
	
	private static CartEntity dtoToEntity(CartEntity cartEntity) {
		return cartEntity;
	}

}
