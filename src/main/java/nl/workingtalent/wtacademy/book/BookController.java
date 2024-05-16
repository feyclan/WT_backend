package nl.workingtalent.wtacademy.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import nl.workingtalent.wtacademy.bookcopy.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.author.Author;

import nl.workingtalent.wtacademy.author.AuthorService;
import nl.workingtalent.wtacademy.bookcopy.BookCopy;
import nl.workingtalent.wtacademy.bookcopy.BookCopyService;
import nl.workingtalent.wtacademy.bookcopy.ReadBookCopyDto;
import nl.workingtalent.wtacademy.category.Category;
import nl.workingtalent.wtacademy.category.CategoryService;
import nl.workingtalent.wtacademy.dto.ResponseDto;
import nl.workingtalent.wtacademy.user.Role;
import nl.workingtalent.wtacademy.user.User;
import nl.workingtalent.wtacademy.user.UserService;

@RestController
@CrossOrigin(maxAge = 3600)
public class BookController {

	@Autowired
	private BookService service;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BookCopyService bookCopyService;

	/**
	 * Endpoint to get all books in a paginated manner.
	 * @param pageNr The page number to fetch. Page numbers start from 0.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing a list of books for the requested page and additional information.
	 */
	@PostMapping("book/all")
	public ResponseDto getAllBooks(@RequestBody int pageNr, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Page<Book> books = service.getAllBooks(pageNr);
		Stream<ReadBookDto> dtos = books.stream().map((book) -> {
			return new ReadBookDto(book);
		});

		ReadAllBookDto dto = new ReadAllBookDto();
		dto.setTotalPages(books.getTotalPages());
		dto.setBooks(dtos);
		return new ResponseDto(true, dto, null, books.getNumberOfElements() + " books found.");
	}

	/**
	 * Endpoint to get a book by its id.
	 * @param id The id of the book to fetch.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the requested book and additional information.
	 */
	@RequestMapping("book/{id}")
	public ResponseDto getBookById(@PathVariable("id") int id, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Book> book = service.getBookById(id);
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book with id " + id + " found.");
		}
		return new ResponseDto(true, new ReadBookDto(book.get()), null, "Book found with id " + id);
	}

	/**
	 * Endpoint to search for books based on certain criteria.
	 * @param dto The SearchBookDto object that contains the search criteria.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the search results and additional information.
	 */
	@PostMapping("book/search")
	public ResponseDto searchBook(@RequestBody SearchBookDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Page<Book> books = service.searchBooks(dto.getCategories(), dto.getSearchTerm(), dto.getPageNr());
		Stream<ReadBookDto> dtos = books.stream().map((book) -> {
			return new ReadBookDto(book);
		});

		ReadAllBookDto resultDto = new ReadAllBookDto();
		resultDto.setTotalPages(books.getTotalPages());
		resultDto.setBooks(dtos);

		return new ResponseDto(true, resultDto, null, books.getNumberOfElements() + " books found.");

	}

	/**
	 * Endpoint to add a new book.
	 * @param saveBookDto The CreateBookDto object that contains the details of the book to be added.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the added book and additional information.
	 */
	@PostMapping("book/create")
	public ResponseDto addBook(@RequestBody CreateBookDto saveBookDto, HttpServletRequest request) {
		if (saveBookDto.getTitle() == null || saveBookDto.getTitle().isBlank()) {
			return new ResponseDto(false, null, null, "Title is required.");
		}

		if (saveBookDto.getDescription() == null || saveBookDto.getDescription().isBlank()) {
			return new ResponseDto(false, null, null, "Description is required.");
		}

		if (saveBookDto.getAuthors() == null || saveBookDto.getAuthors().isEmpty()) {
			return new ResponseDto(false, null, null, "At least one author is required.");
		}

		if (saveBookDto.getCategories() == null || saveBookDto.getCategories().isEmpty()) {
			return new ResponseDto(false, null, null, "At least one category is required.");
		}

		if (saveBookDto.getStates() == null || saveBookDto.getStates().isEmpty()) {
			return new ResponseDto(false, null, null, "At least one book copy with state is required.");
		}

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Book dbBook = new Book();
		dbBook.setTitle(saveBookDto.getTitle());
		dbBook.setDescription(saveBookDto.getDescription());
		dbBook.setImageLink(saveBookDto.getImageLink());
		dbBook.setPublishingDate(saveBookDto.getPublishingDate());
		dbBook.setIsbn(saveBookDto.getIsbn());

		dbBook.setAuthors(createAuthorsAndAddToDB(saveBookDto.getAuthors()));

		dbBook.setCategories(createCategoriesAndAddToDB(saveBookDto.getCategories()));

		service.addBook(dbBook);

		// Initialized at 1, assumed that when adding a book we start counting copies
		// from 1
		int bookCopyCounter = 1;

		
		List<ReadBookCopyDto> bookCopyList = new ArrayList<>();
		//For each state given add a book copy with a unique id and that given state

		for (State state : saveBookDto.getStates()) {
			BookCopy copy = new BookCopy();
			copy.setBook(dbBook);
			copy.setState(state);
			copy.setWTId(dbBook.getId() + "." + bookCopyCounter);
			copy.setAvailable(true);
			copy.setDateAdded(LocalDateTime.now());
			copy.setAddedBy(user);
			
			bookCopyService.addBookCopy(copy);
			bookCopyCounter++;
			bookCopyList.add(new ReadBookCopyDto(copy));
		}

		return new ResponseDto(true, bookCopyList, null, "Book created.");
	}

	/**
	 * Endpoint to update an existing book.
	 * @param dto The UpdateBookDto object that contains the updated details of the book.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the updated book and additional information.
	 */
	@PutMapping("book/update")
	public ResponseDto updateBook(@RequestBody UpdateBookDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Book> optional = service.getBookById(dto.getId());

		if (optional.isEmpty()) {
			return new ResponseDto(false, null, null, "No book with id " + String.valueOf(dto.getId()));
		}

		Book book = optional.get();

		// Check whether all data is filled
		if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
			book.setDescription(dto.getDescription());
		}

		if (dto.getImageLink() != null && !dto.getImageLink().isBlank()) {
			book.setImageLink(dto.getImageLink());
		}

		if (dto.getPublishingDate() != null) {
			book.setPublishingDate(dto.getPublishingDate());
		}

		if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
			book.setTitle(dto.getTitle());
		}

		if (dto.getIsbn() != null && !dto.getIsbn().isBlank()) {
			book.setIsbn(dto.getIsbn());
		}

		if (dto.getAuthors() != null && !dto.getAuthors().isEmpty()) {
			book.setAuthors(createAuthorsAndAddToDB(dto.getAuthors()));
		}

		if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
			book.setCategories(createCategoriesAndAddToDB(dto.getCategories()));
		}

		service.updateBook(book);
		return new ResponseDto(true, null, null, "Book updated with id " + String.valueOf(dto.getId()));
	}

	/**
	 * Endpoint to delete a book by its id.
	 * @param id The id of the book to be deleted.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the deleted book and additional information.
	 */
	@DeleteMapping("books/delete/{id}")
	public ResponseDto deleteBookById(@PathVariable("id") int id, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Book> book = service.getBookById(id);
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book with id " + id + " found");
		}
		service.deleteBookById(id);
		return new ResponseDto(true, null, null, "Book with id " + id + " deleted");
	}

	private List<Category> createCategoriesAndAddToDB(List<String> categoryNames) {

		ArrayList<Category> categories = new ArrayList<Category>();
		for (String categoryName : categoryNames) {
			Optional<Category> category = categoryService.getCategoryByName(categoryName);
			if (category.isEmpty()) {
				categoryService.addCategory(categoryName);
			}
			categories.add(categoryService.getCategoryByName(categoryName).get());
		}

		return categories;
	}

	// Authors langs gaan
	// Bestaat de author al in de db -> voeg author toe aan boek
	// Niet bestaat dan aanmaken en toevoegen aan lijst authors
	private List<Author> createAuthorsAndAddToDB(List<String> authorNames) {

		ArrayList<Author> authors = new ArrayList<Author>();
		for (String authorName : authorNames) {
			Optional<Author> author = authorService.getAuthorByName(authorName);
			if (author.isEmpty()) {
				authorService.addAuthor(authorName);
			}
			authors.add(authorService.getAuthorByName(authorName).get());
		}

		return authors;
	}
}