package nl.workingtalent.wtacademy.author;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.BookService;
import nl.workingtalent.wtacademy.book.SearchBookDto;

@RestController
@CrossOrigin(maxAge = 3600)
public class AuthorController {

    @Autowired
    private BookService service;

    @PostMapping("author/search")
    public List<Author> searchByKeyword(@RequestBody SearchBookDto dto, HttpServletRequest request) {
        // Create a Pageable object
        Pageable pageable = PageRequest.of(dto.getPageNr(), 100, Sort.by(Sort.Direction.ASC, "title"));

        // Fetch the list of books based on the search criteria
        Specification<Book> spec = this.service.searchSpecification(dto.getCategories(), dto.getSearchTerm());
        List<Book> books = service.searchBooks(spec, pageable).getContent();

        // Extract authors from the books and ensure the list is unique
        Set<Author> authors = books.stream()
                                   .flatMap(book -> book.getAuthors().stream())
                                   .collect(Collectors.toSet());

        return List.copyOf(authors);
    }
}
