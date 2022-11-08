package com.java.spring.api.controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.java.spring.api.entity.Tax;
import com.java.spring.api.service.TaxService;
import com.java.spring.api.serviceImpl.TaxServiceImpl;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tax")
public class TaxController {
	
	@Autowired
	private TaxService taxService;
	
	@Value("classpath:tax.graphqls")
	private Resource schemaResource;
	
	private GraphQL graphQL;
	
	@PostConstruct
	public void loadSchema()  throws IOException{
		File schemaFile = schemaResource.getFile();
		TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring = buildWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
		graphQL = GraphQL.newGraphQL(schema).build();
	}
	
	private RuntimeWiring buildWiring() {
		DataFetcher<Iterable<Tax>> fetcher1 = data->{
			return taxService.getAllTaxes1();
		};
		
		DataFetcher<Tax> fetcher2 = data->{
			System.out.println("2bindWiring");
			System.out.println("3taxService.getTaxByCountyCode1() : "+ taxService.getTaxByCountyCode1(data.getArgument("countyCode")));
			return taxService.getTaxByCountyCode1(data.getArgument("countyCode"));
		};
		return RuntimeWiring.newRuntimeWiring().type("Query",
				typeWriting -> typeWriting.dataFetcher("getAllTax", fetcher1).dataFetcher("findTax", fetcher2)).build();
		
	}

	@PostMapping("/getAllTaxes1")
	public ResponseEntity<Object> getAllTaxes1(@RequestBody String query){
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	@PostMapping("/getTaxByCountyCode1")
	public ResponseEntity<Object> getTaxByCountyCode1(@RequestBody String query){
		System.out.println("1controller - Start-endpoint");
		ExecutionResult result = graphQL.execute(query);
		System.out.println("4controller - End-endpoint");
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllTaxes",method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	//@GetMapping("/getAllTaxes")
	public Flux<Tax> getAllTaxes(){
		return taxService.getAllTaxes();
	}
	
	@GetMapping("/getByTaxId/{id}")
	public Mono<Tax> getByTaxId(@PathVariable(value = "id") Integer id){
		return taxService.getByTaxId(id);
	}
	
	@PostMapping("/createConfigTax")
	public Flux<Tax> createConfigTax() {
		return taxService.createConfigTax();
	}
	
	@PostMapping("/createTax")
	public Mono<Tax> createTax(@RequestBody Mono<Tax> tax) {
		return taxService.createTax(tax);
	}
	
	@PostMapping("/createTaxes")
	public Flux<Tax> createTaxes(@RequestBody Iterable<Tax> taxes) {
		return taxService.createTaxes(taxes);
	}
	
	@PutMapping("/updateTax/{id}")
	public Mono<Tax> updateTax(@RequestBody Mono<Tax> tax, @PathVariable(value = "id") Integer id){
		return taxService.updateTax(tax, id);
	}
	
	@DeleteMapping("/deleteByTaxId/{id}")
	public Mono<Void> deleteByTaxId(@PathVariable(value = "id") Integer id){
		return taxService.deleteByTaxId(id);
	}
	
	@DeleteMapping("/deleteAllTaxes")
	public Mono<Void> deleteAllTaxes(){
		return taxService.deleteAllTaxes();
	}
}
