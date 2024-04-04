package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.BookService;

@RestController
public class BookCopyController {

	@Autowired
	private BookCopyService service;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("bookcopy/all")
	public Stream<BookCopyDto> getAllBooks(){
		List<BookCopy> copies = service.getAllBookCopies();
 		Stream<BookCopyDto> dtos = copies.stream().map((copy)->{
 			return new BookCopyDto(copy);
 		});
		return dtos;

	}
	
	@RequestMapping("bookcopy/all/{bookId}")
	public Stream<BookCopyDto> getAllCopiesForBookId(@PathVariable("bookId") long bookId){
 		List<BookCopy> copies = service.getAllCopiesForBookId(bookId);
 		Stream<BookCopyDto> dtos = copies.stream().map((copy)->{
 			return new BookCopyDto(copy);
 		});
		return dtos;
	}
	
	@PostMapping("bookcopy/create")
	public void addBookCopy(@RequestBody SaveBookCopyDto dto) {
		Optional<Book> book = bookService.getBookById(dto.getBookId());
		if(book.isEmpty()) return;
		BookCopy copy = new BookCopy();
		
		copy.setCondition(dto.getCondition());
		copy.setLocation(dto.getLocation());
		copy.setBook(book.get());
		
		service.addBookCopy(copy);
	}
	
	@PutMapping("bookcopy/update")
	public boolean updateBookCopy(@RequestBody SaveBookCopyDto dto) {
		Optional<Book> book = bookService.getBookById(dto.getBookId());
		if(book.isEmpty()) return false;
		BookCopy dbCopy = service.getBookCopyById(dto.getBookId()).get();
		
		dbCopy.setCondition(dto.getCondition());
		dbCopy.setLocation(dto.getLocation());
		dbCopy.setBook(book.get());
		
		service.addBookCopy(dbCopy);
		return true;
	}
	
	@DeleteMapping("bookcopy/delete/{bookCopyId}")
	public void deleteBookById(@PathVariable("bookCopyId") int bookCopyId) {
		service.deleteBookCopyById(bookCopyId);
	}
}
