package nl.workingtalent.wtacademy.user;

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
public class UserService {

	private final int pageSize = 5;

	@Autowired
	private IUserRepository repository;

	// READ
	public Page<User> findAllUsers(int pageNr) {

		// Get a page of certain size, sorted by last name
		Pageable pageable = PageRequest.of(pageNr, pageSize, Sort.by(Sort.Direction.ASC, "lastName"));
		Page<User> page = repository.findAll(pageable);

		return page;
	}

	public Optional<User> findUserById(long id) {
		return repository.findById(id);
	}

	public List<User> searchUser(SearchUserDto searchUserDto) {
		String firstName = searchUserDto.getFirstName();
		String lastName = searchUserDto.getLastName();
		String email = searchUserDto.getEmail();
		String role = searchUserDto.getRole();

		// Constructing the query based on provided criteria
		Specification<User> spec = Specification.where(null);

		if (firstName != null && !firstName.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("firstName"), "%" + firstName + "%"));
		}

		if (lastName != null && !lastName.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("lastName"), "%" + lastName + "%"));
		}

		if (email != null && !email.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.equal(root.get("email"), email));
		}

		if (role != null && !role.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.equal(root.get("role"), role));
		}

		// Fetching users based on the constructed query
		return repository.findAll(spec);
	}

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

	public Optional<User> login(String email, String password) {
		return repository.findByEmailAndPassword(email, password);
	}

	public Optional<User> getUserByToken(String token) {
		return repository.findByToken(token);
	}

}
