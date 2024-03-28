package nl.workingtalent.WTAcademy.Book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

	@Autowired
	private IBookRepository repository;
	
	
	public List<Books> getAllBooks(){
		return repository.findAll();
	}
	
	public Optional<Books> getBookById(int id){
		return repository.findById(id);
	}
	
	public void addBook(Books book) {
		repository.save(book);
	}
}
