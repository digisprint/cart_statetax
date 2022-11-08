package com.java.spring.api.serviceImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.spring.api.entity.Tax;
import com.java.spring.api.repository.TaxRepository;
import com.java.spring.api.service.TaxService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class TaxServiceImpl implements TaxService{

	@Autowired
	private TaxRepository taxRepository;
	
	public Flux<Tax> getAllTaxes(){
		return taxRepository.findAll()
				.flatMap(p->{
					System.out.println("p.getId():"+p.getId());
					return Flux.just(p);
				}).delayElements(Duration.ofMillis(1000));
		//return taxRepository.findAll();
	}
	
	public Mono<Tax> getByTaxId(Integer id){
		return taxRepository.findById(id)
				.doOnNext(p->{
					System.out.println("p.getId():"+p.getId());
					System.out.println("p.getCity():"+p.getCity());
					System.out.println("p.getCityRate():"+p.getCityRate());
				})
				.flatMap(a-> Mono.just(a));
		//return taxRepository.findById(id);
	}
	
	public Mono<Tax> createTax(Mono<Tax> tax) {
		return tax.map(TaxServiceImpl::monoToObj)
		.flatMap(taxRepository::save);
	}
	
	public Flux<Tax> createTaxes(Iterable<Tax> taxes) {
		return taxRepository.saveAll(taxes);
	}
	
	
	public Mono<Tax> updateTax(Mono<Tax> tax, Integer id){
		return taxRepository.findById(id)
				.flatMap(p->tax.map(TaxServiceImpl::monoToObj)
				.doOnNext(e->e.setId(id)))
				.flatMap(taxRepository::save);
				//.map(AppUtils::entityToDto);
	}
	
	
	public Mono<Tax> updateTax2(Mono<Tax> tax, Integer id){
		Mono<Tax> saveTax = taxRepository.findById(id);
		BeanUtils.copyProperties(tax, saveTax);
		return saveTax.map(TaxServiceImpl::monoToObj)
				.flatMap(taxRepository::save);
	}
	
	public Mono<Void> deleteAllTaxes() {
		return taxRepository.deleteAll();
	}
	
	public Mono<Void> deleteByTaxId(Integer id){
		return taxRepository.deleteById(id);
	}
	
	private static Tax monoToObj(Tax tax) {
		return tax;
	}
	
	public Mono<Tax> getByTaxId1(Integer id){
		return taxRepository.findById(id)
				.flatMap(p->{
					System.out.println("p.getId():"+p.getId());
					System.out.println("p.getCity():"+p.getCity());
					System.out.println("p.getCityRate():"+p.getCityRate());
					return Mono.just(p);
				});
		//return taxRepository.findById(id);
	}

	@Override
	public Mono<Tax> getTax() {
		//ShipToAddress shippingAddress = cartEntity.getShippingAddress();
		//System.out.println("shippingAddress.getId():"+shippingAddress.getId());
		//return getByTaxId(shippingAddress.getId());//sopcoming
		return taxRepository.findById(2)
				.flatMap(p->{
					System.out.println("p.getId():"+p.getId());
					System.out.println("p.getCity():"+p.getCity());
					System.out.println("p.getCityRate():"+p.getCityRate());
					return Mono.just(p);
				});
		
		
		/*
		 * return taxRepository.findById(shippingAddress.getId()) .flatMap(p->{
		 * cartEntity.setCountryRate(p.getCountryRate());
		 * cartEntity.setStateRate(p.getStateRate());
		 * cartEntity.setCityRate(p.getCityRate());
		 * cartEntity.setCountyRate(p.getCountyRate());
		 * System.out.println("p.getIddddddd():"+p.getId());
		 * System.out.println("p.getCityyyyy():"+p.getCity());
		 * System.out.println("p.getCityRate():"+p.getCityRate()); return Mono.just(p);
		 * });
		 */
		/*
		 * Optional<Tax> optTax =
		 * taxRepository.findByCountryCodeAndStateCodeAndCityCodeAndCountyCode(
		 * shippingAddress.getCountryCode(), shippingAddress.getStateCode(),
		 * shippingAddress.getCityCode(), shippingAddress.getCountyCode());
		 * if(optTax.get() == null) { System.out.println("tax is null"); } else {
		 * System.out.println("tax is not null"); return
		 * optTax.flatMap(TaxServiceImpl::optToTax) .;
		 * //System.out.println("tax is not null cityrate:"+optTax.get().getCityRate());
		 * 
		 * } return optTax.get();
		 */
	}

	private static Tax optToTax(Tax tax1) {
		return tax1;
	}

	/*@Override
	public Flux<Tax> createConfigTax() {
		Tax tax1 = new Tax(1, "US", "UNITED STATES", 1.0, "1", "ALABAMA", 1.0,"124","ABBEVILLE",1.0,"67","HENRY",1.0);
		Tax tax2 = new Tax(2, "US", "UNITED STATES", 2.0, "1", "ALABAMA", 2.0,"820","ALABASTER",2.0,"117","SHELBY",2.0);
		Tax tax3 = new Tax(3, "US", "UNITED STATES", 1.0, "1", "ALABAMA", 1.0,"988","ALBERTVILLE",1.0,"95","MARSHALL",1.0);
		Tax tax4 = new Tax(4, "US", "UNITED STATES", 3.0, "4", "ARIZONA", 3.0,"82740","WICKENBURG",3.0,"13","MARICOPA",3.0);
		Tax tax5 = new Tax(5, "US", "UNITED STATES", 4.0, "5", "ARKANSAS", 4.0,"970","ALMA",4.0,"33","CRAWFORD",4.0);
		Tax tax6 = new Tax(6, "US", "UNITED STATES", 5.0, "61", "CALIFORNIA", 5.0,"562","ALAMEDA",5.0,"1","ALAMEDA",5.0);
		Tax tax7 = new Tax(7, "US", "UNITED STATES", 6.0, "8", "COLORADO", 6.0,"25390","EVERGREEN",6.0,"59","JEFFERSON",6.0);
		Tax tax8 = new Tax(8, "US", "UNITED STATES", 7.0, "9", "CONNECTICUT", 7.0,"15980","COLEBROOK1",0.0,"5","LITCHFIELD",7.0);
		Tax tax9 = new Tax(9, "US", "UNITED STATES", 1.0, "10", "DELAWARE", 1.0,"70560","TALLEYVILLE",1.0,"3","NEW CASTLE",1.0);
		Tax tax10 = new Tax(10, "US", "UNITED STATES", 1.0, "12", "FLORIDA", 1.0,"375","ALACHUA1",0.0,"1","ALACHUA",1.0);
		Tax tax11 = new Tax(11, "US", "UNITED STATES", 1.0, "12", "FLORIDA", 1.0,"950","ALTAMONTE SPRINGS",1.0,"117","SEMINOLE",1.0);
		Tax tax12 = new Tax(12, "US", "UNITED STATES", 1.0, "13", "GEORGIA", 1.0,"576","ADEL",1.0,"75","COOK",1.0);
		Tax tax13 = new Tax(13, "US", "UNITED STATES", 1.0, "13", "GEORGIA", 1.0,"1052","ALBANY",1.0,"95","DOUGHERTY",1.0);
		Tax tax14 = new Tax(14, "US", "UNITED STATES", 1.0, "15", "HAWAII", 1.0,"07300","EWA",1.0,"003","HONOLULU",1.0);
		Tax tax15 = new Tax(15, "US", "UNITED STATES", 1.0, "16", "IDAHO", 1.0,"01900","AMERICAN FALLS",1.0,"077","POWER",1.0);
		Tax tax16 = new Tax(16, "US", "UNITED STATES", 1.0, "17", "ILLINOIS", 1.0,"00113","ABINGDON",1.0,"095","KNOX",1.0);
		Tax tax17 = new Tax(17, "US", "UNITED STATES", 1.0, "18", "INDIANA", 1.0,"00802","ALBANY",1.0,"035","DELAWARE",1.0);
		Tax tax18 = new Tax(18, "US", "UNITED STATES", 1.0, "19", "IOWA", 1.0,"00505","ADEL",1.0,"049","DALLAS",1.0);
		Tax tax19 = new Tax(19, "US", "UNITED STATES", 1.0, "20", "KANSAS", 1.0,"00125","ABILENE",1.0,"041","DICKINSON",1.0);
		Tax tax20 = new Tax(20, "US", "UNITED STATES", 1.0, "21", "KENTUCKY", 1.0,"00802","ALEXANDRIA",1.0,"037","CAMPBELL",1.0);
		Tax tax21 = new Tax(21, "US", "UNITED STATES", 1.0, "22", "LOUISIANA", 1.0,"00100","ABBEVILLE",1.0,"113","VERMILION",1.0);
		Tax tax22 = new Tax(22, "US", "UNITED STATES", 1.0, "23", "MAINE", 1.0,"01360","ANSON",1.0,"025","SOMERSET",1.0);
		Tax tax23 = new Tax(23, "US", "UNITED STATES", 1.0, "24", "MARYLAND", 1.0,"00125","ABERDEEN",1.0,"025","HARFORD",1.0);
		Tax tax24 = new Tax(24, "US", "UNITED STATES", 1.0, "25", "MASSACHUSETTS", 1.0,"00135","ABINGTON",1.0,"023","PLYMOUTH",1.0);
		Tax tax25 = new Tax(25, "US", "UNITED STATES", 1.0, "26", "MICHIGAN", 1.0,"00440","ADRIAN",1.0,"091","LENAWEE",1.0);
		List<Tax> taxList = new ArrayList<>();
		taxList.add(tax1);
		taxList.add(tax2);
		taxList.add(tax3);
		taxList.add(tax4);
		taxList.add(tax5);
		taxList.add(tax6);
		taxList.add(tax7);
		taxList.add(tax8);
		taxList.add(tax9);
		taxList.add(tax10);
		
		taxList.add(tax11);
		taxList.add(tax12);
		taxList.add(tax13);
		taxList.add(tax14);
		taxList.add(tax15);
		taxList.add(tax16);
		taxList.add(tax17);
		taxList.add(tax18);
		taxList.add(tax19);
		taxList.add(tax20);
		
		taxList.add(tax21);
		taxList.add(tax22);
		taxList.add(tax23);
		taxList.add(tax24);
		taxList.add(tax25);
		
		return taxRepository.saveAll(taxList);
	}*/
	
	@Override
	public Flux<Tax> createConfigTax() {
		return taxRepository.saveAll(Stream.of(new Tax(1, "US", "UNITED STATES", 1.0, "1", "ALABAMA", 1.0,"124","ABBEVILLE",1.0,"67","HENRY",1.0),
				new Tax(2, "US", "UNITED STATES", 2.0, "1", "ALABAMA", 2.0,"820","ALABASTER",2.0,"117","SHELBY",2.0),
				new Tax(3, "US", "UNITED STATES", 1.0, "1", "ALABAMA", 1.0,"988","ALBERTVILLE",1.0,"95","MARSHALL",1.0),
				new Tax(4, "US", "UNITED STATES", 3.0, "4", "ARIZONA", 3.0,"82740","WICKENBURG",3.0,"13","MARICOPA",3.0),
				new Tax(5, "US", "UNITED STATES", 4.0, "5", "ARKANSAS", 4.0,"970","ALMA",4.0,"33","CRAWFORD",4.0),
				new Tax(6, "US", "UNITED STATES", 5.0, "61", "CALIFORNIA", 5.0,"562","ALAMEDA",5.0,"1","ALAMEDA",5.0),
				new Tax(7, "US", "UNITED STATES", 6.0, "8", "COLORADO", 6.0,"25390","EVERGREEN",6.0,"59","JEFFERSON",6.0),
				new Tax(8, "US", "UNITED STATES", 7.0, "9", "CONNECTICUT", 7.0,"15980","COLEBROOK1",0.0,"5","LITCHFIELD",7.0),
				new Tax(9, "US", "UNITED STATES", 1.0, "10", "DELAWARE", 1.0,"70560","TALLEYVILLE",1.0,"3","NEW CASTLE",1.0),
				new Tax(10, "US", "UNITED STATES", 1.0, "12", "FLORIDA", 1.0,"375","ALACHUA1",0.0,"1","ALACHUA",1.0),
				new Tax(11, "US", "UNITED STATES", 1.0, "12", "FLORIDA", 1.0,"950","ALTAMONTE SPRINGS",1.0,"117","SEMINOLE",1.0),
				new Tax(12, "US", "UNITED STATES", 1.0, "13", "GEORGIA", 1.0,"576","ADEL",1.0,"75","COOK",1.0),
				new Tax(13, "US", "UNITED STATES", 1.0, "13", "GEORGIA", 1.0,"1052","ALBANY",1.0,"95","DOUGHERTY",1.0),
				new Tax(14, "US", "UNITED STATES", 1.0, "15", "HAWAII", 1.0,"07300","EWA",1.0,"003","HONOLULU",1.0),
				new Tax(15, "US", "UNITED STATES", 1.0, "16", "IDAHO", 1.0,"01900","AMERICAN FALLS",1.0,"077","POWER",1.0),
				new Tax(16, "US", "UNITED STATES", 1.0, "17", "ILLINOIS", 1.0,"00113","ABINGDON",1.0,"095","KNOX",1.0),
				new Tax(17, "US", "UNITED STATES", 1.0, "18", "INDIANA", 1.0,"00802","ALBANY",1.0,"035","DELAWARE",1.0),
				new Tax(18, "US", "UNITED STATES", 1.0, "19", "IOWA", 1.0,"00505","ADEL",1.0,"049","DALLAS",1.0),
				new Tax(19, "US", "UNITED STATES", 1.0, "20", "KANSAS", 1.0,"00125","ABILENE",1.0,"041","DICKINSON",1.0),
				new Tax(20, "US", "UNITED STATES", 1.0, "21", "KENTUCKY", 1.0,"00802","ALEXANDRIA",1.0,"037","CAMPBELL",1.0),
				new Tax(21, "US", "UNITED STATES", 1.0, "22", "LOUISIANA", 1.0,"00100","ABBEVILLE",1.0,"113","VERMILION",1.0),
				new Tax(22, "US", "UNITED STATES", 1.0, "23", "MAINE", 1.0,"01360","ANSON",1.0,"025","SOMERSET",1.0),
				new Tax(23, "US", "UNITED STATES", 1.0, "24", "MARYLAND", 1.0,"00125","ABERDEEN",1.0,"025","HARFORD",1.0),
				new Tax(24, "US", "UNITED STATES", 1.0, "25", "MASSACHUSETTS", 1.0,"00135","ABINGTON",1.0,"023","PLYMOUTH",1.0),
				new Tax(25, "US", "UNITED STATES", 1.0, "26", "MICHIGAN", 1.0,"00440","ADRIAN",1.0,"091","LENAWEE",1.0)
				).collect(Collectors.toList()));
	}

	@Override
	public Iterable<Tax> getAllTaxes1() {
		return (Iterable<Tax>) taxRepository.findAll();
	}

	@Override
	public Tax getTaxByCountyCode1(String countyCode) {
		return taxRepository.findByCountyCode(countyCode);
	}


	
}
