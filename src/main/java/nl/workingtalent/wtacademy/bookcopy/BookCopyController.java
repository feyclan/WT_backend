package nl.workingtalent.wtacademy.bookcopy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.BookService;
import nl.workingtalent.wtacademy.dto.ResponseDto;
import nl.workingtalent.wtacademy.user.Role;
import nl.workingtalent.wtacademy.user.User;

@RestController
@CrossOrigin(maxAge = 3600)
public class BookCopyController {

	@Autowired
	private BookCopyService service;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookCopyMapper mapper;

	@RequestMapping("bookcopy/all")
	public ResponseDto getAllBooks(HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}
		List<BookCopy> copies = service.getAllBookCopies();
		return new ResponseDto(true, mapper.bookCopyListToDtos(copies), null,
				String.valueOf(copies.size()) + ((copies.size() < 2) ? " copy" : " copies") + " found");
	}

	@RequestMapping("bookcopy/all/{bookId}")
	public ResponseDto getAllCopiesForBookId(@PathVariable("bookId") long bookId, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Book> book = bookService.getBookById(bookId);
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book exists with book ID " + bookId);
		}
		List<BookCopy> copies = service.getAllCopiesForBookId(bookId);
		return new ResponseDto(true, mapper.bookCopyListToDtos(copies), null,
				String.valueOf(copies.size()) + ((copies.size() < 2) ? " copy" : " copies") + " found");
	}

	@PostMapping("bookcopy/create")
	public ResponseDto addBookCopy(@RequestBody CreateBookCopyDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		if (dto.getStates() == null) {
			return new ResponseDto(false, null, null, "No states were given for the copies.");
		}

		Optional<Book> book = bookService.getBookById(dto.getBookId());
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book exists with book ID " + dto.getBookId());
		}

		Book dbBook = book.get();
		long bookId = dbBook.getId();

		// Initialize list to return information to front-end
		List<ReadBookCopyDto> bookCopyList = new ArrayList<>();
		
		// Initialized as the amount of book copies already present for a book + 1
		int bookCopyCounter = service.getAllCopiesForBookId(bookId).size() + 1;

		for (State state : dto.getStates()) {
			BookCopy copy = new BookCopy();

			copy.setState(state);
			copy.setBook(dbBook);
			copy.setWTId(bookId + "." + bookCopyCounter);
			copy.setAvailable(true);

			service.addBookCopy(copy);

			bookCopyCounter++;
			bookCopyList.add(new ReadBookCopyDto(copy));
		}

		return new ResponseDto(true, bookCopyList, null, String.valueOf(dto.getStates().size())
				+ ((dto.getStates().size() < 2) ? " copy was" : " copies were") + " added");
	}

	@PutMapping("bookcopy/update")
	public ResponseDto updateBookCopy(@RequestBody UpdateBookCopyDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<BookCopy> bookCopy = service.getBookCopyById(dto.getId());
		if (bookCopy.isEmpty())
			return new ResponseDto(false, null, null, "Copy does not exist with id " + dto.getId());
		BookCopy dbCopy = service.getBookCopyById(dto.getId()).get();

		if (dto.getState() != null) {
			dbCopy.setState(dto.getState());
		}

		dbCopy.setAvailable(dto.isAvailable());

		service.addBookCopy(dbCopy);
		return new ResponseDto(true, null, null, "Copy successfully updated.");
	}

	@DeleteMapping("bookcopy/delete/{bookCopyId}")
	public ResponseDto deleteBookById(@PathVariable("bookCopyId") int bookCopyId, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<BookCopy> copy = service.getBookCopyById(bookCopyId);
		if (copy.isEmpty()) {
			return new ResponseDto(false, null, null, "Copy does not exist with id " + bookCopyId);
		}
		service.deleteBookCopyById(bookCopyId);
		return new ResponseDto(true, null, null, "Copy deleted");
	}

}
