package nl.workingtalent.wtacademy.book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookService {

	@Autowired
	private IBookRepository repository;

	private final int pageSize = 5;

	/**
	 * This method is used to fetch all books from the database in a paginated manner.
	 * The books are sorted by their title in descending order.
	 * The size of the page is determined by the 'pageSize' field of this class.
	 *
	 * @param pageNr The page number to fetch. Page numbers start from 0.
	 * @return A Page object containing a list of Book objects for the requested page.
	 *         The Page object also contains additional information about the pagination such as total number of pages, total number of elements etc.
	 */
	public Page<Book> getAllBooks(int pageNr) {
		//Get a page of certain size, sorted by title 
		Pageable pageable = PageRequest.of(pageNr, pageSize, Sort.by(Sort.Direction.ASC, "title"));

		// Fetch the page from the repository
		Page<Book> page = repository.findAll(pageable);

		return page;
	}

	public Optional<Book> getBookById(long id) {
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

	/**
	 * This method is used to search for books based on certain criteria.
	 * The search criteria include categories, title, and authors.
	 * The search results are fetched in a paginated manner, sorted by title in descending order.
	 * The size of the page is determined by the 'pageSize' field of this class.
	 *
	 * @param searchBookDto The SearchBookDto object that contains the search criteria.
	 * @return A Page object containing a list of Book objects that match the search criteria for the requested page.
	 *         The Page object also contains additional information about the pagination such as total number of pages, total number of elements etc.
	 */
	public Page<Book> searchBooks(SearchBookDto searchBookDto) {
		List<String> categories = searchBookDto.getCategories();
		String title = searchBookDto.getTitle();
		String authors = searchBookDto.getAuthors();
		String description = searchBookDto.getDescription();

		// Constructing the query based on provided criteria
		Specification<Book> spec = Specification.where(null);

		if (categories != null && !categories.isEmpty()) {
			spec = spec.and((root, query, builder) -> root.join("categories").get("category").in(categories));
		}
		if (title != null && !title.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("title"), "%" + title + "%"));
		}
		if (authors != null && !authors.isEmpty()) {
				spec = spec.and((root, query, builder) -> builder.like(root.join("authors").get("name"), "%" + authors + "%"));
		}
		if (description != null && !description.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.join("description").get("description"), "%" + description + "%"));
			}

		Pageable pageable = PageRequest.of(searchBookDto.getPageNr(), pageSize, Sort.by(Sort.Direction.ASC, "title"));


		// Fetching books based on the constructed query
		return repository.findAll(spec, pageable);
	}
}
