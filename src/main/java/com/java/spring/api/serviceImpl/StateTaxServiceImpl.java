package com.java.spring.api.serviceImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.spring.api.entity.StateTax;
import com.java.spring.api.repository.StateTaxRepository;
import com.java.spring.api.service.StateTaxService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class StateTaxServiceImpl implements StateTaxService{

	@Autowired
	private StateTaxRepository stateTaxRepository;
	
	public Flux<StateTax> getAllStateTaxes(){
		return stateTaxRepository.findAll()
				.flatMap(p->{
					System.out.println("p.getId():"+p.getId());
					return Flux.just(p);
				}).delayElements(Duration.ofMillis(10));
		//return taxRepository.findAll();
	}
	
	public Mono<StateTax> getByStateTaxId(Integer id){
		return stateTaxRepository.findById(id)
				.doOnNext(p->{
					System.out.println("p.getId():"+p.getId());
					System.out.println("p.getStateName():"+p.getStateName());
					System.out.println("p.getStateTaxRate():"+p.getStateTaxRate());
					
				})
				.flatMap(a-> Mono.just(a));
		//return taxRepository.findById(id);
	}
	
	@Override
	public Flux<StateTax> getByStateTaxName(String stateName) {
		return stateTaxRepository.findByStateNameIgnoreCase(stateName)
				.doOnNext(p->{
					System.out.println("p.getId():"+p.getId());
					System.out.println("p.getStateName():"+p.getStateName());
					System.out.println("p.getStateTaxRate():"+p.getStateTaxRate());
					
				})
				.flatMap(a-> Flux.just(a));
	}
	
	public Mono<StateTax> createStateTax(Mono<StateTax> stateTax) {
		return stateTax.map(StateTaxServiceImpl::monoToObj)
		.flatMap(stateTaxRepository::save);
	}
	
	public Flux<StateTax> createStateTaxes(Iterable<StateTax> stateTaxes) {
		return stateTaxRepository.saveAll(stateTaxes);
	}
	
	
	public Mono<StateTax> updateStateTax(Mono<StateTax> stateTax, Integer id){
		return stateTaxRepository.findById(id)
				.flatMap(p->stateTax.map(StateTaxServiceImpl::monoToObj)
				.doOnNext(e->e.setId(id)))
				.flatMap(stateTaxRepository::save);
				//.map(AppUtils::entityToDto);
	}
	
	public Mono<Void> deleteAllStateTaxes() {
		return stateTaxRepository.deleteAll();
	}
	
	public Mono<Void> deleteByStateTaxId(Integer id){
		return stateTaxRepository.deleteById(id);
	}
	
	private static StateTax monoToObj(StateTax stateTax) {
		return stateTax;
	}
	
	/*
	 * public Mono<StateTax> getByTaxId1(Integer id){ return
	 * stateTaxRepository.findById(id) .flatMap(p->{
	 * System.out.println("p.getId():"+p.getId());
	 * System.out.println("p.getStateName():"+p.getStateName());
	 * System.out.println("p.getStateTaxRate():"+p.getStateTaxRate()); return
	 * Mono.just(p); }); //return taxRepository.findById(id); }
	 */

	
		
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
		 * Optional<StateTax> optTax =
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
	//}

	/*
	 * private static StateTax optToTax(StateTax tax1) { return tax1; }
	 */

	@Override
	public Flux<StateTax> createConfigStateTax() {
		StateTax stateTax1 = new StateTax(1, "Alabama", "1", 4.000, false, 1.0, false);
		StateTax stateTax2 = new StateTax(2, "Alaska", "2", 0.000, false, 2.0, false);
		StateTax stateTax3 = new StateTax(3, "Arizona", "4", 5.600, false, 3.0, false);
		StateTax stateTax4 = new StateTax(4, "Arkansas", "5", 6.500, true, 4.0, false);
		StateTax stateTax5 = new StateTax(5, "California", "6", 7.250, true, 5.0, false);
		StateTax stateTax6 = new StateTax(6, "Colorado", "8", 2.900, true, 6.0, false);
		StateTax stateTax7 = new StateTax(7, "Connecticut", "9", 6.350, false, 7.0, false);
		StateTax stateTax8 = new StateTax(8, "Delaware", "10", 0.000, false, 8.0, false);
		StateTax stateTax9 = new StateTax(9, "District of Columbia", "11", 6.000, false, 9.0, false);
		StateTax stateTax10 = new StateTax(10, "Florida", "12", 6.000, true, 10.0, false);
		StateTax stateTax11 = new StateTax(11, "Georgia", "13", 4.000, true, 11.0, false);
		StateTax stateTax12 = new StateTax(12, "Hawaii", "15", 4.000, false, 12.0, false);
		StateTax stateTax13 = new StateTax(13, "Idaho", "16", 6.000, false, 13.0, false);
		StateTax stateTax14 = new StateTax(14, "Illinois", "17", 6.250, true, 14.0, false);
		StateTax stateTax15 = new StateTax(15, "Indiana", "18", 7.000, false, 15.0, false);
		StateTax stateTax16 = new StateTax(16, "Iowa", "19", 6.000, false, 16.0, false);
		StateTax stateTax17 = new StateTax(17, "Kansas", "20", 6.500, true, 17.0, false);
		StateTax stateTax18 = new StateTax(18, "Kentucky", "21", 6.000, false, 18.0, false);
		StateTax stateTax19 = new StateTax(19, "Louisiana", "22", 4.450, true, 19.0, false);
		StateTax stateTax20 = new StateTax(20, "Maine", "23", 5.500, false, 20.0, false);
		StateTax stateTax21 = new StateTax(21, "Maryland", "24", 6.000, false, 21.0, false);
		StateTax stateTax22 = new StateTax(22, "Massachusetts", "25", 6.250, false, 22.0, false);
		StateTax stateTax23 = new StateTax(23, "Michigan", "26", 6.000, false, 23.0, false);
		StateTax stateTax24 = new StateTax(24, "Minnesota", "27", 6.875, true, 24.0, false);
		StateTax stateTax25 = new StateTax(25, "Mississippi", "28", 7.000, false, 25.0, false);
		StateTax stateTax26 = new StateTax(26, "Missouri", "29", 4.225, true, 25.0, false);
		StateTax stateTax27 = new StateTax(27, "Montana", "30", 0.000, false, 25.0, false);
		StateTax stateTax28 = new StateTax(28, "Nebraska", "31", 5.500, true, 25.0, false);
		StateTax stateTax29 = new StateTax(29, "Nevada", "32", 6.850, true, 25.0, false);
		StateTax stateTax30 = new StateTax(30, "New Hampshire", "33", 0.000, false, 25.0, false);
		StateTax stateTax31 = new StateTax(31, "New Jersey", "34", 6.625, false, 25.0, false);
		StateTax stateTax32 = new StateTax(32, "New Mexico", "35", 5.000, false, 25.0, false);
		StateTax stateTax33 = new StateTax(33, "New York", "36", 4.000, true, 25.0, false);
		StateTax stateTax34 = new StateTax(34, "North Carolina", "37", 4.750, true, 25.0, false);
		StateTax stateTax35 = new StateTax(35, "North Dakota", "38", 5.000, true, 25.0, false);
		StateTax stateTax36 = new StateTax(36, "Ohio", "39", 5.750, true, 25.0, false);
		StateTax stateTax37 = new StateTax(37, "Oklahoma", "40", 4.500, true, 25.0, false);
		StateTax stateTax38 = new StateTax(38, "Oregon", "41", 0.000, false, 25.0, false);
		StateTax stateTax39 = new StateTax(39, "Pennsylvania", "42", 6.000, false, 25.0, false);
		StateTax stateTax40 = new StateTax(40, "Puerto Rico", "72", 11.500, true, 25.0, false);
		StateTax stateTax41 = new StateTax(41, "Rhode Island", "44", 7.000, false, 25.0, false);
		StateTax stateTax42 = new StateTax(42, "South Carolina", "45", 6.000, true, 25.0, false);
		StateTax stateTax43 = new StateTax(43, "South Dakota", "46", 4.500, true, 25.0, false);
		StateTax stateTax44 = new StateTax(44, "Tennessee", "47", 7.000, true, 25.0, false);
		StateTax stateTax45 = new StateTax(45, "Texas", "48", 6.250, true, 25.0, false);
		StateTax stateTax46 = new StateTax(46, "Utah", "49", 4.850, true, 25.0, false);
		StateTax stateTax47 = new StateTax(47, "Vermont", "50", 6.000, false, 25.0, false);
		StateTax stateTax48 = new StateTax(48, "Virginia", "51", 4.300, true, 25.0, false);
		StateTax stateTax49 = new StateTax(49, "Washington", "53", 6.500, true, 25.0, false);
		StateTax stateTax50 = new StateTax(50, "West Virginia", "54", 6.000, true, 25.0, false);
		StateTax stateTax51 = new StateTax(51, "Wisconsin", "55", 5.000, true, 25.0, false);
		StateTax stateTax52 = new StateTax(52, "Wyoming", "56", 4.000, true, 25.0, false);
		List<StateTax> stateTaxList = new ArrayList<>();
		stateTaxList.add(stateTax1);
		stateTaxList.add(stateTax2);
		stateTaxList.add(stateTax3);
		stateTaxList.add(stateTax4);
		stateTaxList.add(stateTax5);
		stateTaxList.add(stateTax6);
		stateTaxList.add(stateTax7);
		stateTaxList.add(stateTax8);
		stateTaxList.add(stateTax9);
		stateTaxList.add(stateTax10);
		stateTaxList.add(stateTax11);
		stateTaxList.add(stateTax12);
		stateTaxList.add(stateTax13);
		stateTaxList.add(stateTax14);
		stateTaxList.add(stateTax15);
		stateTaxList.add(stateTax16);
		stateTaxList.add(stateTax17);
		stateTaxList.add(stateTax18);
		stateTaxList.add(stateTax19);
		stateTaxList.add(stateTax20);
		stateTaxList.add(stateTax21);
		stateTaxList.add(stateTax22);
		stateTaxList.add(stateTax23);
		stateTaxList.add(stateTax24);
		stateTaxList.add(stateTax25);
		stateTaxList.add(stateTax26);
		stateTaxList.add(stateTax27);
		stateTaxList.add(stateTax28);
		stateTaxList.add(stateTax29);
		stateTaxList.add(stateTax30);
		stateTaxList.add(stateTax31);
		stateTaxList.add(stateTax32);
		stateTaxList.add(stateTax33);
		stateTaxList.add(stateTax34);
		stateTaxList.add(stateTax35);
		stateTaxList.add(stateTax36);
		stateTaxList.add(stateTax37);
		stateTaxList.add(stateTax38);
		stateTaxList.add(stateTax39);
		stateTaxList.add(stateTax40);
		stateTaxList.add(stateTax41);
		stateTaxList.add(stateTax42);
		stateTaxList.add(stateTax43);
		stateTaxList.add(stateTax44);
		stateTaxList.add(stateTax45);
		stateTaxList.add(stateTax46);
		stateTaxList.add(stateTax47);
		stateTaxList.add(stateTax48);
		stateTaxList.add(stateTax49);
		stateTaxList.add(stateTax50);
		stateTaxList.add(stateTax51);
		stateTaxList.add(stateTax52);

		return stateTaxRepository.saveAll(stateTaxList);
	}

}
