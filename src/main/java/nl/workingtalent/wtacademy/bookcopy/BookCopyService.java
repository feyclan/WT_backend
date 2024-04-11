package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

	@Autowired
	private IBookCopyRepository repository;

	public List<BookCopy> getAllBookCopies() {
		return repository.findAll();
	}

	public List<BookCopy> getAllCopiesForBookId(long id) {
		return repository.findByBookId(id);
	}

	public void addBookCopy(BookCopy copy) {
		repository.save(copy);
	}

	public Optional<BookCopy> getBookCopyById(long id) {
		return repository.findById(id);
	}

	public void deleteBookCopyById(long id) {
		repository.deleteById(id);
	}

}
