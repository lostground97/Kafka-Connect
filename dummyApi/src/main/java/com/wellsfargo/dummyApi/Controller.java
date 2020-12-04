package com.wellsfargo.dummyApi;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@CrossOrigin(origins = "*")
	@GetMapping(path="/1")
	public String getCall1() {
		String tmp = "{\n"
				+ "id: 1;\n"
				+ "}\n";
		return tmp;
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(path="/2")
	public String getCall2() {
		String tmp = "{\n"
				+ "id: 2;\n"
				+ "}\n";
		return tmp;
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(path="/3")
	public String getCall3() {
		String tmp = "{\n id: 3;\n}\n";
		return tmp;
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(path="/4")
	public String getCall4() {
		String tmp = "{\n"
				+ "id: 4;\n"
				+ "}\n";
		return tmp;
	}

}
