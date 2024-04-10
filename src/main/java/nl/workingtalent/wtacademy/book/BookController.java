package nl.workingtalent.wtacademy.book;

import java.util.ArrayList;
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
import nl.workingtalent.wtacademy.author.Author;

import nl.workingtalent.wtacademy.author.AuthorService;
import nl.workingtalent.wtacademy.category.Category;
import nl.workingtalent.wtacademy.category.CategoryService;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private CategoryService categoryService;
	

	@RequestMapping("book/all")
	public Stream<ReadBookDto> getAllBooks(){
		List<Book> books = service.getAllBooks();
		Stream<ReadBookDto> dtos = books.stream().map((book)->{
 			return new ReadBookDto(book);
 		});
		return dtos;
	}
	
	@RequestMapping("book/{id}")
	public Optional<Book> getBookById(@PathVariable("id") int id){
		return service.getBookById(id);
	}
	
	@PostMapping("book/create")
	public void addBook(@RequestBody CreateBookDto saveBookDto) {
		
		Book dbBook = new Book();
		dbBook.setTitle(saveBookDto.getTitle());
		dbBook.setDescription(saveBookDto.getDescription());
		dbBook.setImageLink(saveBookDto.getImageLink());
		dbBook.setPublishingDate(saveBookDto.getPublishingDate());
		dbBook.setIsbn(saveBookDto.getIsbn());	
		
		dbBook.setAuthors(createAuthorsAndAddToDB(saveBookDto.getAuthors()));		
		dbBook.setCategories(createCategoriesAndAddToDB(saveBookDto.getCategories()));

		service.addBook(dbBook);
	}
	
	@PutMapping("book/update")
	public boolean updateBook(@RequestBody UpdateBookDto dto) {
		
		Optional<Book> optional = service.getBookById(dto.getId());
		
		if(optional.isEmpty()) {
			return false;
		}
		
		Book book = optional.get();
		
		//Check whether all data is filled

		book.setDescription(dto.getDescription());
		book.setImageLink(dto.getImageLink());
		book.setPublishingDate(dto.getPublishingDate());
		book.setTitle(dto.getTitle());
		book.setIsbn(dto.getIsbn());	
		
		book.setAuthors(createAuthorsAndAddToDB(dto.getAuthors()));		
		book.setCategories(createCategoriesAndAddToDB(dto.getCategories()));
		
		service.updateBook(book);
		return true;
	}

	@DeleteMapping("books/delete/{id}")
	public void deleteBookById(@PathVariable("id") int id) {
		service.deleteBookById(id);
	}
	
	private List<Category> createCategoriesAndAddToDB(List<String> categoryNames){
		
		ArrayList<Category> categories = new ArrayList<Category>();
		for (String categoryName : categoryNames) {
			Optional<Category> category = categoryService.getCategoryByName(categoryName);
			if(category.isEmpty()) {
				categoryService.addCategory(categoryName);
			}
			categories.add(categoryService.getCategoryByName(categoryName).get());
		}
		
		return categories;
	}
	
	
	// Authors langs gaan
	// Bestaat de author al in de db -> voeg author toe aan boek
	// Niet bestaat dan aanmaken en toevoegen aan lijst authors
private List<Author> createAuthorsAndAddToDB(List<String> authorNames){
		
		ArrayList<Author> authors = new ArrayList<Author>();
		for (String authorName : authorNames) {
			Optional<Author> author = authorService.getAuthorByName(authorName);
			if(author.isEmpty()) {
				authorService.addAuthor(authorName);
			}
			authors.add(authorService.getAuthorByName(authorName).get());
		}
		
		return authors;
	}
}
