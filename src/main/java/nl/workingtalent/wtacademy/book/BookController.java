package nl.workingtalent.wtacademy.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.workingtalent.wtacademy.author.AuthorService;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;
	
	@Autowired
	private AuthorService authorService;
	
	@RequestMapping("books/all")
	public List<Book> getAllBooks(){
		
		return service.getAllBooks();
	}
	
	@RequestMapping("books/{id}")
	public Optional<Book> getBookById(@PathVariable("id") int id){
		return service.getBookById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "books/create")
	public void addBook(@RequestBody ObjectNode bookData) {
		
		JsonNode newBook = bookData.get("book");
		
		String author = bookData.get("author").asText();
		Book dbBook = new Book();
		
		dbBook.setTitle(newBook.get("title").asText());
		dbBook.setDescription(newBook.get("description").asText());
		dbBook.setImageLink(newBook.get("imageLink").asText());
		//dbBook.setPublisherId(newBook.get("publisherId").asInt());
		//dbBook.setPublishingDate(LocalDateTime.parse(newBook.get("publishingDate").asText()));
		
		authorService.addAuthor(author);
		
		service.addBook(dbBook);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "books/update/{id}")
	public boolean updateBook(@RequestBody Book newBook, @PathVariable("id") int id) {
		
		Optional<Book> optional = service.getBookById(id);
		
		if(optional.isEmpty()) {
			return false;
		}
		
		Book book = optional.get();
		
		//Check whether all data is filled
		book.setDescription(newBook.getDescription());
		book.setImageLink(newBook.getImageLink());
		//book.setPublisherId(newBook.getPublisherId());
		book.setPublishingDate(newBook.getPublishingDate());
		book.setTitle(newBook.getTitle());
		
		service.updateBook(book);
		return true;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "books/delete/{id}")
	public void deleteBookById(@PathVariable("id") int id) {
		service.deleteBookById(id);
	}
}
