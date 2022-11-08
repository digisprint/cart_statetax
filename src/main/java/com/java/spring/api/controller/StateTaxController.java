package com.java.spring.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.java.spring.api.entity.StateTax;
import com.java.spring.api.entity.Tax;
import com.java.spring.api.service.StateTaxService;
import com.java.spring.api.service.TaxService;
import com.java.spring.api.serviceImpl.TaxServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tax")
public class StateTaxController {
	
	@Autowired
	private StateTaxService stateTaxService;
	
	// GET - http://localhost:9091/tax/stateTax
	@RequestMapping(value = "/stateTax",method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	//@GetMapping("/getAllTaxes")
	public Flux<StateTax> getAllStateTaxes(){
		return stateTaxService.getAllStateTaxes();
	}
	
	// GET - http://localhost:9091/tax/stateTax/2
	@GetMapping("/stateTax/{id}")
	public Mono<StateTax> getByStateTaxId(@PathVariable(value = "id") Integer id){
		return stateTaxService.getByStateTaxId(id);
	}
	
	// GET - http://localhost:9091/tax/stateTax/alabama
	@GetMapping("/stateTax/{id}/{stateName}")
	public Flux<StateTax> getByStateTaxName(@PathVariable(value = "id") Integer id, @PathVariable(value = "stateName") String stateName){
		return stateTaxService.getByStateTaxName(stateName);
	}
	
	// POST - http://localhost:9091/tax/configStateTax
	@PostMapping("/configStateTax")
	public Flux<StateTax> createConfigStateTax() {
		return stateTaxService.createConfigStateTax();
	}
	
	// POST - http://localhost:9091/tax/stateTax
	@PostMapping("/stateTax")
	public Mono<StateTax> createStateTax(@RequestBody Mono<StateTax> stateTax) {
		return stateTaxService.createStateTax(stateTax);
	}
	
	// POST - http://localhost:9091/tax/stateTaxes
	@PostMapping("/stateTaxes")
	public Flux<StateTax> createStateTaxes(@RequestBody Iterable<StateTax> stateTaxes) {
		return stateTaxService.createStateTaxes(stateTaxes);
	}
	
	// PUT - http://localhost:9091/tax/stateTax
	@PutMapping("/stateTax/{id}")
	public Mono<StateTax> updateStateTax(@RequestBody Mono<StateTax> stateTax, @PathVariable(value = "id") Integer id){
		return stateTaxService.updateStateTax(stateTax, id);
	}
	
	// DELETE - http://localhost:9091/tax/stateTax
	@DeleteMapping("/stateTax/{id}")
	public Mono<Void> deleteByStateTaxId(@PathVariable(value = "id") Integer id){
		return stateTaxService.deleteByStateTaxId(id);
	}
	
	// DELETE - http://localhost:9091/tax/stateTaxes
	@DeleteMapping("/stateTaxes")
	public Mono<Void> deleteAllStateTaxes(){
		return stateTaxService.deleteAllStateTaxes();
	}
}
