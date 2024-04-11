package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.SearchBookDto;

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

	// TODO: Remove when implemented
//	public List<User> findUserByLastName(String name) {
//		return repository.findUserByLastName(name);
//	}
//
	public Optional<User> findUserByEmail(String email) {
		return repository.findUserByEmail(email);
	}
	// TODO: Remove when implemented
//	public List<User> findUserByRole(Role role) {
//
//		return repository.findUserByRole(role);
//	}

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

	public List<User> searchUser(SearchUserDto searchUserDto) {
		String firstName = searchUserDto.getFirstName();
		String lastName = searchUserDto.getLastName();
		String email = searchUserDto.getEmail();
		// Moet dit een role object worden?
		Role role = searchUserDto.getRole();

		// Constructing the query based on provided criteria
		Specification<User> spec = Specification.where(null);

//		if (firstName != null && !firstName.isEmpty()) {
//			spec = spec.and((root, query, builder) -> root.join("categories").get("category").in(firstName));
//		}
		if (firstName != null && !firstName.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("firstName"), "%" + firstName + "%"));
		}
		
		if (lastName != null && !lastName.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("lastName"), "%" + lastName + "%"));
		}
		
		if (email != null && !email.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("email"), "%" + email + "%"));
		}
//		if (authors != null && !authors.isEmpty()) {
//			for (String author : authors) {
//				spec = spec.and(
//						(root, query, builder) -> builder.like(root.join("authors").get("name"), "%" + author + "%"));
//			}
//		}
//		if (location != null && !location.isEmpty()) {
//			spec = spec.and((root, query, builder) -> builder.equal(root.join("bookCopies").get("location"), location));
//		}

		// Fetching books based on the constructed query
		return repository.findAll(spec);
	}

}
