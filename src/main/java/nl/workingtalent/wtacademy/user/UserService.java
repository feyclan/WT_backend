package nl.workingtalent.wtacademy.user;

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

	public Optional<User> findUserById(long id) {
		return repository.findById(id);
	}

	public List<User> findUserByFirstName(String name) {
		return repository.findUserByFirstName(name);
	}

	public List<User> findUserByLastName(String name) {
		return repository.findUserByLastName(name);
	}

	public Optional<User> findUserByEmail(String email) {
		return repository.findUserByEmail(email);
	}

	public List<User> findUserByRole(Role role) {

		return repository.findUserByRole(role);
	}
	
	public Optional<User> login(String email, String password) {
		return repository.findByEmailAndPassword(email, password);
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
