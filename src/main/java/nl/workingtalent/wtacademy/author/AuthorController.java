package nl.workingtalent.wtacademy.author;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.BookService;
import nl.workingtalent.wtacademy.book.SearchBookDto;

@RestController
public class AuthorController {

	@Autowired
	private BookService service;
	
	@PostMapping("author/search/keyword")
	public List<Author> searchByKeyword(@RequestBody SearchBookDto dto, HttpServletRequest request) {
		Specification<Book> spec = this.service.searchSpecification(dto.getCategories(), dto.getSearchTerm());
		
		// specification van books -> omzetten naar een uniek lijst van authors
	}
	

}
