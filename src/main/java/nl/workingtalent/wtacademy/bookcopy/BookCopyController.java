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
import nl.workingtalent.wtacademy.user.ReadUserDto;
import nl.workingtalent.wtacademy.user.User;

@RestController
public class BookCopyController {

	@Autowired
	private BookCopyService service;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("bookcopy/all")
	public ResponseDto getAllBooks(){
		List<BookCopy> copies = service.getAllBookCopies();
 		Stream<ReadBookCopyDto> dtos = copies.stream().map((copy)->{
 			return new ReadBookCopyDto(copy);
 		});
 		return createResponseDtoList(null, dtos.toList(), "copy");
 		
	}
	
	@RequestMapping("bookcopy/all/{bookId}")
	public ResponseDto getAllCopiesForBookId(@PathVariable("bookId") long bookId){
 		List<BookCopy> copies = service.getAllCopiesForBookId(bookId);
 		Stream<ReadBookCopyDto> dtos = copies.stream().map((copy)->{
 			return new ReadBookCopyDto(copy);
 		});
 		return createResponseDtoList(null, dtos.toList(), "copy");
	}
	
	@PostMapping("bookcopy/create")
	public ResponseDto addBookCopy(@RequestBody CreateBookCopyDto dto) {
		Optional<Book> book = bookService.getBookById(dto.getBookId());
		if(book.isEmpty()) {
			return new ResponseDto(false, dto, null,
					"Book does not exist.");
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
		if(bookCopy.isEmpty()) return new ResponseDto(false, dto, null, "Copy does not exist.");
		BookCopy dbCopy = service.getBookCopyById(dto.getId()).get();
		
		dbCopy.setState(dto.getState());
		dbCopy.setLocation(dto.getLocation());
		
		service.addBookCopy(dbCopy);
		return new ResponseDto(true, new ReadBookCopyDto(dbCopy), null, "Copy succesfully updated.");
	}
	
	@DeleteMapping("bookcopy/delete/{bookCopyId}")
	public ResponseDto deleteBookById(@PathVariable("bookCopyId") int bookCopyId) {
		Optional<BookCopy> copy = service.getBookCopyById(bookCopyId);
		if(copy.isEmpty()) {
			return new ResponseDto(false, copy, null, "Copy does not exist.");
		}
		service.deleteBookCopyById(bookCopyId);
		return new ResponseDto(true, new ReadBookCopyDto(copy.get()), null, "Copy deleted");
	}
	
//	// Gets the reponseDto for objects who return a single value
//	private ResponseDto createResponseDto(Object pathVal, Optional<BookCopy> bookCopyOptional, String pathVar) {
//		if (bookCopyOptional.isPresent()) {
//			BookCopy copy = bookCopyOptional.get();
//			ReadBookCopyDto readCopyDto = new ReadBookCopyDto(copy);
//			ResponseDto responseDto = new ResponseDto(true, readCopyDto, null, "Copy found.");
//			return responseDto;
//		}
//		ResponseDto responseDto = new ResponseDto(false, pathVal, null,
//				"No copies with " + pathVar + " '" + pathVal + "' found.");
//
//		return responseDto;
//	}

	// Gets the responseDto for objects who return a list of values
	private ResponseDto createResponseDtoList(Object pathVal, List<ReadBookCopyDto> bookCopies, String pathVar) {
		if (bookCopies.isEmpty()) {
			ResponseDto responseDto = new ResponseDto(false, pathVal, null,
					"No copies with the " + pathVar + " '" + pathVal + "' found.");
			return responseDto;
		}
		
		ResponseDto responseDto = new ResponseDto(true, bookCopies, null,
				bookCopies.size() + " " + pathVar + " found.");

		return responseDto;
	}
}
