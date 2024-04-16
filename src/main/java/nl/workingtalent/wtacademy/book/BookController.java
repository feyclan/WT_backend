package nl.workingtalent.wtacademy.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nl.workingtalent.wtacademy.author.Author;

import nl.workingtalent.wtacademy.author.AuthorService;
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
	private UserService userService;

	@RequestMapping("book/all")
	public ResponseDto getAllBooks() {
		List<Book> books = service.getAllBooks();
		Stream<ReadBookDto> dtos = books.stream().map((book) -> {
			return new ReadBookDto(book);
		});
		return new ResponseDto(true, dtos, null, books.size() + " books found.");
	}

	@RequestMapping("book/{id}")
	public ResponseDto getBookById(@PathVariable("id") int id) {
		Optional<Book> book = service.getBookById(id);
		if (book.isEmpty()) {
			return new ResponseDto(false, null, null, "No book with id " + id + " found.");
		}
		return new ResponseDto(true, new ReadBookDto(book.get()), null, "Book found with id " + id);
	}

	@RequestMapping("book/search")
	public ResponseDto searchBook(@RequestBody SearchBookDto dto) {
		List<Book> books = service.searchBooks(dto);
		Stream<ReadBookDto> dtos = books.stream().map((book) -> {
			return new ReadBookDto(book);
		});
		return new ResponseDto(true, dtos, null, books.size() + " books found.");

	}

	@PostMapping("book/create")
	public ResponseDto addBook(@RequestBody CreateBookDto saveBookDto) {
		if (saveBookDto == null || saveBookDto.getTitle().isBlank()) {
			return new ResponseDto(false, null, null, "Title is required.");
		}
		
		Optional<User> user = userService.getUserByToken(saveBookDto.getToken());
		if (user.isEmpty() || user.get().getRole() != Role.TRAINER) {
			return new ResponseDto(false, null, null, "Action not allowed.");
		}

		Book dbBook = new Book();
		dbBook.setTitle(saveBookDto.getTitle());
		dbBook.setDescription(saveBookDto.getDescription());
		dbBook.setImageLink(saveBookDto.getImageLink());
		dbBook.setPublishingDate(saveBookDto.getPublishingDate());
		dbBook.setIsbn(saveBookDto.getIsbn());

		if (saveBookDto.getAuthors() != null) {
			dbBook.setAuthors(createAuthorsAndAddToDB(saveBookDto.getAuthors()));
		}

		if (saveBookDto.getCategories() != null) {
			dbBook.setCategories(createCategoriesAndAddToDB(saveBookDto.getCategories()));
		}

		service.addBook(dbBook);
		return new ResponseDto(true, null, null, "Book created.");
	}

	@PutMapping("book/update")
	public ResponseDto updateBook(@RequestBody UpdateBookDto dto) {

		Optional<Book> optional = service.getBookById(dto.getId());

		if (optional.isEmpty()) {
			return new ResponseDto(false, null, null, "No book with id " + String.valueOf(dto.getId()));
		}

		Book book = optional.get();

		// Check whether all data is filled

		book.setDescription(dto.getDescription());
		book.setImageLink(dto.getImageLink());
		book.setPublishingDate(dto.getPublishingDate());
		book.setTitle(dto.getTitle());
		book.setIsbn(dto.getIsbn());

		if (dto.getAuthors() != null) {
			book.setAuthors(createAuthorsAndAddToDB(dto.getAuthors()));
		}

		if (dto.getCategories() != null) {
			book.setCategories(createCategoriesAndAddToDB(dto.getCategories()));
		}

		service.updateBook(book);
		return new ResponseDto(true, null, null, "Book updated with id " + String.valueOf(dto.getId()));
	}

	@DeleteMapping("books/delete/{id}")
	public ResponseDto deleteBookById(@PathVariable("id") int id) {
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
