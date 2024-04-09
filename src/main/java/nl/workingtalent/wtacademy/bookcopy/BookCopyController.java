package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nl.workingtalent.wtacademy.book.BookService;

@RestController
public class BookCopyController {

	@Autowired
	private BookCopyService service;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("book-copy/all")
	public List<BookCopy> getAllBooks(){
		
		return service.getAllBookCopies();
	}
	
	@RequestMapping("book/book-copy/{bookId}")
	public List<BookCopy> getAllCopiesForBookId(@PathVariable("bookId") long bookId){
		
		return service.getAllCopiesForBookId(bookId);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "book-copy/create/{bookId}")
	public void addBookCopy(@RequestBody BookCopy copy, @PathVariable("bookId") long bookId) {
		if(bookService.getBookById(bookId).isEmpty()) return;
		copy.setBook(bookService.getBookById(bookId).get());
		service.addBookCopy(copy);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "book-copy/update/{bookCopyId}")
	public boolean updateBookCopy(@RequestBody BookCopy copy, @PathVariable("bookCopyId") long bookCopyId) {
		if(service.getBookCopyById(bookCopyId).isEmpty()) return false;
		BookCopy dbCopy = service.getBookCopyById(bookCopyId).get();
		
		dbCopy.setCondition(copy.getCondition());
		dbCopy.setLocation(copy.getLocation());
		
		service.addBookCopy(dbCopy);
		return true;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "book-copy/delete/{bookCopyId}")
	public void deleteBookById(@PathVariable("bookCopyId") int bookCopyId) {
		service.deleteBookCopyById(bookCopyId);
	}
}
