package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;
import java.util.Optional;

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
import nl.workingtalent.wtacademy.dto.ResponseDto;

@RestController
public class BookCopyController {

	@Autowired
	private BookCopyService service;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookCopyMapper mapper;

	@RequestMapping("bookcopy/all")
	public ResponseDto getAllBooks() {
		List<BookCopy> copies = service.getAllBookCopies();
		return new ResponseDto(true, mapper.bookCopyListToDtos(copies), null,
				String.valueOf(copies.size()) + ((copies.size() < 2) ? " copy" : " copies") + " found");
	}

	@RequestMapping("bookcopy/all/{bookId}")
	public ResponseDto getAllCopiesForBookId(@PathVariable("bookId") long bookId) {
		Optional<Book> book = bookService.getBookById(bookId);
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book exists with book ID " + bookId);
		}
		List<BookCopy> copies = service.getAllCopiesForBookId(bookId);
		return new ResponseDto(true, mapper.bookCopyListToDtos(copies), null,
				String.valueOf(copies.size()) + ((copies.size() < 2) ? " copy" : " copies") + " found");
	}

	@PostMapping("bookcopy/create")
	public ResponseDto addBookCopy(@RequestBody CreateBookCopyDto dto) {
		Optional<Book> book = bookService.getBookById(dto.getBookId());
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book exists with book ID " + dto.getBookId());
		}

		BookCopy copy = new BookCopy();

		copy.setState(dto.getState());
		copy.setLocation(dto.getLocation());
		copy.setBook(book.get());

		service.addBookCopy(copy);
		return new ResponseDto(true, null, null, "Copy added.");
	}

	@PutMapping("bookcopy/update")
	public ResponseDto updateBookCopy(@RequestBody UpdateBookCopyDto dto) {
		Optional<BookCopy> bookCopy = service.getBookCopyById(dto.getId());
		if (bookCopy.isEmpty())
			return new ResponseDto(false, null, null, "Copy does not exist with id " + dto.getId());
		BookCopy dbCopy = service.getBookCopyById(dto.getId()).get();

		dbCopy.setState(dto.getState());
		dbCopy.setLocation(dto.getLocation());

		service.addBookCopy(dbCopy);
		return new ResponseDto(true, null, null, "Copy succesfully updated.");
	}

	@DeleteMapping("bookcopy/delete/{bookCopyId}")
	public ResponseDto deleteBookById(@PathVariable("bookCopyId") int bookCopyId) {
		Optional<BookCopy> copy = service.getBookCopyById(bookCopyId);
		if (copy.isEmpty()) {
			return new ResponseDto(false, null, null, "Copy does not exist with id " + bookCopyId);
		}
		service.deleteBookCopyById(bookCopyId);
		return new ResponseDto(true, null, null, "Copy deleted");
	}

}
