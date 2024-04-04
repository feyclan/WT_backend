package nl.workingtalent.wtacademy.author;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

	@Autowired
	private IAuthorRepository repository;

	public void addAuthor(String author) {
		Optional<Author> dbAuthor = getAuthorByName(author);
		if (dbAuthor.isEmpty()) {
			Author newAuthor = new Author();
			newAuthor.setName(author);
			repository.save(newAuthor);
		}
	}

	public Optional<Author> getAuthorByName(String name) {
		return repository.findOneByName(name);
	}
	

}
