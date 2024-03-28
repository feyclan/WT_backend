package nl.workingtalent.WTAcademy.Book;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;
	
	@RequestMapping("books/all")
	public List<Books> getAllBooks(){
		
		return service.getAllBooks();
	}
	
	@RequestMapping("books/{id}")
	public Optional<Books> getBookById(@PathVariable("id") int id){
		return service.getBookById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "books/create")
	public void addBook(@RequestBody Books book) {
		service.addBook(book);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "books/update/{id}")
	public boolean updateBook(@RequestBody Books newBook, @PathVariable("id") int id) {
		
		Optional<Books> optional = service.getBookById(id);
		
		if(optional.isEmpty()) {
			return false;
		}
		
		Books book = optional.get();
		
		//Check whether all data is filled
		book.setDescription(newBook.getDescription());
		book.setImageLink(newBook.getImageLink());
		book.setPublisherId(newBook.getPublisherId());
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
