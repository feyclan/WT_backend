package nl.workingtalent.wtacademy.book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookService {

	@Autowired
	private IBookRepository repository;
	
	
	public List<Book> getAllBooks(){
		return repository.findAll();
	}
	
	public Optional<Book> getBookById(long id){
		return repository.findById(id);
	}
	
	public void addBook(Book book) {
		repository.save(book);
	}
	
	public void updateBook(Book book) {
		repository.save(book);
	}
	
	public void deleteBookById(long id) {
		repository.deleteById(id);
	}
	
	 public List<Book> searchBooks(SearchBookDto searchBookDto) {
	        List<String> categories = searchBookDto.getCategories();
	        String title = searchBookDto.getTitle();
	        List<String> authors = searchBookDto.getAuthors();
	        String location = searchBookDto.getLocation();

	        // Constructing the query based on provided criteria
	        Specification<Book> spec = Specification.where(null);

	        if (categories != null && !categories.isEmpty()) {
	            spec = spec.and((root, query, builder) -> root.join("categories").get("category").in(categories));
	        }
	        if (title != null && !title.isEmpty()) {
	            spec = spec.and((root, query, builder) -> builder.like(root.get("title"), "%" + title + "%"));
	        }
	        if (authors != null && !authors.isEmpty()) {
	        	for (String author : authors) {
	                spec = spec.and((root, query, builder) -> builder.like(root.join("authors").get("name"), "%" + author + "%"));
	            }
	        }
	        if (location != null && !location.isEmpty()) {
	            spec = spec.and((root, query, builder) -> builder.equal(root.join("bookCopies").get("location"), location));
	        }

	        // Fetching books based on the constructed query
	        return repository.findAll(spec);
	    }
}
