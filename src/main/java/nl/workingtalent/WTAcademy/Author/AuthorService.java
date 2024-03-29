package nl.workingtalent.WTAcademy.Author;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

	@Autowired
	private IAuthorRepository repository;
	
	
	public void addAuthor(String author) {
		
		Optional<Author> dbAuthor = getAuthorByName(author);
		if(dbAuthor.isEmpty()) {
			Author newAuthor = new Author();
			newAuthor.setAuthorName(author);
			repository.save(newAuthor);
		}		
	}
	
	
	public Optional<Author> getAuthorByName(String author) {
		
		return repository.findOneByAuthorName(author);
		
	}
}
