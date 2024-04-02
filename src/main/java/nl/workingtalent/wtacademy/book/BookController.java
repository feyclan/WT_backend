package nl.workingtalent.wtacademy.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import nl.workingtalent.wtacademy.author.Author;

import nl.workingtalent.wtacademy.author.AuthorService;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;
	
	@Autowired
	private AuthorService authorService;
	
	@RequestMapping("books/all")
	public List<Book> getAllBooks(){
		
		return service.getAllBooks();
	}
	
	@RequestMapping("books/{id}")
	public Optional<Book> getBookById(@PathVariable("id") int id){
		return service.getBookById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "books/create")
	public void addBook(@RequestBody SaveBookDto saveBookDto) {
		
		Book dbBook = new Book();
		dbBook.setTitle(saveBookDto.getTitle());
		dbBook.setDescription(saveBookDto.getDescription());
		dbBook.setImageLink(saveBookDto.getImageLink());
		dbBook.setPublishingDate(saveBookDto.getPublishingDate());

		// Authors langs gaan
		// Bestaat de author al in de db -> voeg author toe aan boek
		// Niet bestaat dan aanmaken en toevoegen aan lijst authors
		ArrayList<Author> authors = new ArrayList<Author>();
		for (String authorName : saveBookDto.getAuthors()) {
			if(authorService.getAuthorByName(authorName).isEmpty()) {
				authorService.addAuthor(authorName);
			}
			authors.add(authorService.getAuthorByName(authorName).get());
		}
		
		dbBook.setAuthors(authors);

		service.addBook(dbBook);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "books/update/{id}")
	public boolean updateBook(@RequestBody SaveBookDto saveBookDto, @PathVariable("id") int id) {
		
		Optional<Book> optional = service.getBookById(id);
		
		if(optional.isEmpty()) {
			return false;
		}
		
		Book book = optional.get();
		
		//Check whether all data is filled

		book.setDescription(saveBookDto.getDescription());
		book.setImageLink(saveBookDto.getImageLink());
		book.setPublishingDate(saveBookDto.getPublishingDate());
		book.setTitle(saveBookDto.getTitle());

		
//		 Check if the edit contains unknown authors
//		If so, add them to author table
		ArrayList<Author> authors = new ArrayList<Author>();
		for (String authorName : saveBookDto.getAuthors()) {
			if(authorService.getAuthorByName(authorName).isEmpty()) {
				authorService.addAuthor(authorName);
			}
			authors.add(authorService.getAuthorByName(authorName).get());
		}
		
		book.setAuthors(authors);
		service.updateBook(book);
		return true;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "books/delete/{id}")
	public void deleteBookById(@PathVariable("id") int id) {
		service.deleteBookById(id);
	}
}
