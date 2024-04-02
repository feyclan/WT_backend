package nl.workingtalent.wtacademy.bookcopy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookCopyController {

	@Autowired
	private BookCopyService service;
}
