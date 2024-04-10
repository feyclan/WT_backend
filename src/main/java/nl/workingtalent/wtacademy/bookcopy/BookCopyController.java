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
import nl.workingtalent.wtacademy.dto.ResponseDto;

@RestController
public class BookCopyController {

	@Autowired
	private BookCopyService service;

	@Autowired
	private BookService bookService;

	@RequestMapping("bookcopy/all")
	public ResponseDto getAllBooks() {
		List<BookCopy> copies = service.getAllBookCopies();
		return createResponseDtoList(null, copies, (copies.size() < 2) ? "copy" : "copies");

	}

	@RequestMapping("bookcopy/all/{bookId}")
	public ResponseDto getAllCopiesForBookId(@PathVariable("bookId") long bookId) {
		List<BookCopy> copies = service.getAllCopiesForBookId(bookId);
		return createResponseDtoList(null, copies, (copies.size() < 2) ? "copy" : "copies");
	}

	@PostMapping("bookcopy/create")
	public ResponseDto addBookCopy(@RequestBody CreateBookCopyDto dto) {
		Optional<Book> book = bookService.getBookById(dto.getBookId());
		if (book.isEmpty()) {
			return new ResponseDto(false, dto, null, "Book does not exist.");
		}

		BookCopy copy = new BookCopy();

		copy.setState(dto.getState());
		copy.setLocation(dto.getLocation());
		copy.setBook(book.get());

		service.addBookCopy(copy);
		return new ResponseDto(true, new ReadBookCopyDto(copy), null, "Copy added");
	}

	@PutMapping("bookcopy/update")
	public ResponseDto updateBookCopy(@RequestBody UpdateBookCopyDto dto) {
		Optional<BookCopy> bookCopy = service.getBookCopyById(dto.getId());
		if (bookCopy.isEmpty())
			return new ResponseDto(false, dto, null, "Copy does not exist.");
		BookCopy dbCopy = service.getBookCopyById(dto.getId()).get();

		dbCopy.setState(dto.getState());
		dbCopy.setLocation(dto.getLocation());

		service.addBookCopy(dbCopy);
		return new ResponseDto(true, new ReadBookCopyDto(dbCopy), null, "Copy succesfully updated.");
	}

	@DeleteMapping("bookcopy/delete/{bookCopyId}")
	public ResponseDto deleteBookById(@PathVariable("bookCopyId") int bookCopyId) {
		Optional<BookCopy> copy = service.getBookCopyById(bookCopyId);
		if (copy.isEmpty()) {
			return new ResponseDto(false, copy, null, "Copy does not exist.");
		}
		service.deleteBookCopyById(bookCopyId);
		return new ResponseDto(true, new ReadBookCopyDto(copy.get()), null, "Copy deleted");
	}

	// Gets the responseDto for objects who return a list of values
	private ResponseDto createResponseDtoList(Object pathVal, List<BookCopy> copies, String pathVar) {
		if (copies.isEmpty()) {
			ResponseDto responseDto = new ResponseDto(false, pathVal, null,
					"No copies with the " + pathVar + " '" + pathVal + "' found.");
			return responseDto;
		}

		Stream<ReadBookCopyDto> dtos = copies.stream().map((copy) -> {
			return new ReadBookCopyDto(copy);
		});

		ResponseDto responseDto = new ResponseDto(true, dtos, null, copies.size() + " " + pathVar + " found.");

		return responseDto;
	}
}
