package nl.workingtalent.wtacademy.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {
	
	@Autowired
	private LoanService service;

}
