package nl.workingtalent.WTAcademy.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {

	@Autowired
	private AuthorService service;
	

}
