package nl.workingtalent.WTAcademy.user;

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
		return service.findUserWithId(id);
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
		Optional<User> user = service.findUserWithId(id);
		if (user.isEmpty()) {
			return false;
		}
		
		User dbUser = user.get();
		
		// overschrijven
		dbUser.setFirstName(newUser.getFirstName());
		dbUser.setLastName(newUser.getLastName());
		dbUser.setEmailaddress(newUser.getEmailaddress());
		dbUser.setPassword(newUser.getPassword());
		dbUser.setAddressId(newUser.getAddressId());
		dbUser.setRoleId(newUser.getRoleId());
		
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
