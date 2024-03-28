package nl.workingtalent.WTAcademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository repository;
	
	// READ
	public List<User> findAllUsers() {
		return repository.findAll();
	}
	
	public Optional<User> findUserWithId(long id) {
		return repository.findById(id);
	}

	// CREATE
	public void create(User user) {
		repository.save(user);
	}
	
	// UPDATE
	public void update(User dbUser) {
		repository.save(dbUser);
	}
	
	// DELETE
	public void delete(long id) {
		repository.deleteById(id);
	}

}
