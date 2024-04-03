package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class UserController {

	@Autowired
	private UserService service;
	
	// READ
	@RequestMapping("user/all")
	public List<User> allUsers() {
		return service.findAllUsers();
	}
	
	@RequestMapping("user/{id}")
	public Optional<User> findUserById(@PathVariable("id") long id) {
		return service.findUserById(id);
	}
	
	@RequestMapping("user/find/{lastName}")
	public Optional<User> findUserByLastName(@PathVariable("lastName") String name) {
		return service.findUserByLastName(name);
	}
	
	@RequestMapping("user/role/{role}")
	public Optional<User> findUserByRole(@PathVariable("role") Role role) {
		return service.findUserByRole(role);
	}
	
	// CREATE
	@RequestMapping(method = RequestMethod.POST, value = "user/create")
	public void createUser(@RequestBody User user) {
		service.create(user);
	}
	
	// UPDATE
	@RequestMapping(method = RequestMethod.PUT, value = "user/update/{id}")
	public boolean updateUser(@RequestBody User newUser, @PathVariable("id") long id) {
		
		// ophalen bestaande user
		Optional<User> user = service.findUserById(id);
		if (user.isEmpty()) {
			return false;
		}
		
		User dbUser = user.get();
		
		// overschrijven
		dbUser.setFirstName(newUser.getFirstName());
		dbUser.setLastName(newUser.getLastName());
		dbUser.setEmail(newUser.getEmail());
		dbUser.setPassword(newUser.getPassword());
		dbUser.setRole(newUser.getRole());

		// opslaan
		service.update(dbUser);
		
		return true;
	}
	
	// DELETE
	@RequestMapping(method = RequestMethod.DELETE, value = "user/delete/{id}")
	public void deleteUser(@PathVariable("id") long id) {
		service.delete(id);
	}
}
