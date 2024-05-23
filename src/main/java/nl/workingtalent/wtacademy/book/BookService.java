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

    private final int pageSize = 100;

    public Page<Book> getAllBooks(int pageNr) {
        Pageable pageable = PageRequest.of(pageNr, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        return repository.findAll(pageable);
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

    public Page<Book> searchBooks(List<String> categories, String searchTerm, int pageNr) {
        Specification<Book> spec = this.searchSpecification(categories, searchTerm);
        Pageable pageable = PageRequest.of(pageNr, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        return repository.findAll(spec, pageable);
    }

    public Specification<Book> searchSpecification(List<String> categories, String searchTerm) {
        Specification<Book> spec = Specification.where(null);
        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, builder) -> root.join("categories").get("category").in(categories));
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.or(
                    builder.like(root.get("title"), "%" + searchTerm + "%"),
                    builder.like(root.join("authors").get("name"), "%" + searchTerm + "%"),
                    builder.like(root.get("description"), "%" + searchTerm + "%"),
                    builder.like(root.get("isbn"), "%" + searchTerm + "%"),
                    builder.like(root.join("categories").get("category"), "%" + searchTerm + "%")
            ));
        } else {
            spec = Specification.where(null);
        }
        return spec;
    }

    public Page<Book> searchBooks(Specification<Book> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }
}
