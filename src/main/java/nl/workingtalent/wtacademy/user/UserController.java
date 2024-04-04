package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.workingtalent.wtacademy.dto.ResponseDto;

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

	@RequestMapping("user/firstname/{firstName}")
	public List<User> findAllUsersByFirstName(@PathVariable("firstName") String name) {
		return service.findUserByFirstName(name);
	}

	@RequestMapping("user/lastname/{lastName}")
	public List<User> findUserByLastName(@PathVariable("lastName") String name) {
		return service.findUserByLastName(name);
	}

	@RequestMapping("user/email/{email}")
	public Optional<User> findUserByEmail(@PathVariable("email") String email) {
		return service.findUserByEmail(email);
	}

	@RequestMapping("user/role/{role}")
	public List<User> findUserByRole(@PathVariable("role") Role role) {
		return service.findUserByRole(role);
	}

	// CREATE
	@PostMapping("user/create")
	public ResponseDto createUser(@RequestBody CreateUserDto dto) {
		
		// DOES EMAIL EXIST?
		Optional<User> existingUser = service.findUserByEmail(dto.getEmail());
		if (existingUser.isPresent()) {
			ResponseDto responseDto = new ResponseDto(false, existingUser.get().getEmail(), null, "User with the provided email already exists.");
	        return responseDto;
		}
		
		User newUser = new User();
		newUser.setFirstName(dto.getFirstName());
		newUser.setLastName(dto.getLastName());
		newUser.setEmail(dto.getEmail());
		newUser.setPassword(dto.getPassword());
		newUser.setRole(dto.getRole());
		service.create(newUser);
		
        ResponseDto responseDto = new ResponseDto(true, newUser, null, "User created successfully.");
        return responseDto;
	}

	// UPDATE
	@PutMapping("user/update/{id}")
	public ResponseEntity<String> updateUser(@RequestBody User newUser, @PathVariable("id") long id) {

		Optional<User> existingEmail = service.findUserByEmail(newUser.getEmail());

		// ophalen bestaande user
		Optional<User> user = service.findUserById(id);
		if (user.isEmpty() || existingEmail.isPresent()) {
			return ResponseEntity.badRequest().body("User with the provided email already exists.");
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

		return ResponseEntity.ok("User updated successfully.");
	}

	// DELETE
	@DeleteMapping("user/delete/{id}")
	public void deleteUser(@PathVariable("id") long id) {
		service.delete(id);
	}
}
