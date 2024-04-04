package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Stream<ReadUserDto> findAllUsers() {
		List<User> users = service.findAllUsers();

		return getUsers(users);
	}

	@RequestMapping("user/{id}")
	public ReadUserDto findUserById(@PathVariable("id") long id) {
		User user = service.findUserById(id).get();

		return new ReadUserDto(user);
	}

	@RequestMapping("user/firstname/{firstName}")
	public Stream<ReadUserDto> findAllUsersByFirstName(@PathVariable("firstName") String name) {
		List<User> users = service.findUserByFirstName(name);

		return getUsers(users);
	}

	@RequestMapping("user/lastname/{lastName}")
	public Stream<ReadUserDto> findUserByLastName(@PathVariable("lastName") String name) {
		List<User> users = service.findUserByLastName(name);

		return getUsers(users);
	}

	@RequestMapping("user/email/{email}")
	public ReadUserDto findUserByEmail(@PathVariable("email") String email) {
		User user = service.findUserByEmail(email).get();

		return new ReadUserDto(user);
	}

	@RequestMapping("user/role/{role}")
	public Stream<ReadUserDto> findUserByRole(@PathVariable("role") Role role) {
		List<User> users = service.findUserByRole(role);

		return getUsers(users);
	}

	// CREATE
	@PostMapping("user/create")
	public ResponseDto createUser(@RequestBody CreateUserDto dto) {
		
		// DOES EMAIL EXIST?
		Optional<User> existingUserEmail = service.findUserByEmail(dto.getEmail());
		if (existingUserEmail.isPresent()) {
			ResponseDto responseDto = new ResponseDto(false, existingUserEmail.get().getEmail(), null, "User with the provided email already exists.");
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
	public ResponseDto updateUser(@RequestBody UpdateUserDto dto, @PathVariable("id") long id) {

		// DOES USER EXIST?
		Optional<User> existingUser = service.findUserById(id);
		if (existingUser.isEmpty()) {
			ResponseDto responseDto = new ResponseDto(false, existingUser, null, "User doesn't exist.");
			return responseDto;
		}
		
		// DOES EMAIL EXIST?
		Optional<User> existingUserEmail = service.findUserByEmail(dto.getEmail());
		if (existingUserEmail.isPresent() && existingUserEmail.get().getId() != id) {
			ResponseDto responseDto = new ResponseDto(false, existingUserEmail.get().getEmail(), null, "User with the provided email already exists.");
	        return responseDto;
		}

		User dbUser = existingUser.get();

		// OVERWRITE
		dbUser.setFirstName(dto.getFirstName());
		dbUser.setLastName(dto.getLastName());
		dbUser.setEmail(dto.getEmail());
		dbUser.setPassword(dto.getPassword());

		// SAVE
		service.update(dbUser);
		ResponseDto responseDto = new ResponseDto(true, dto, null, "User updated successfully.");
        return responseDto;
	}

	// DELETE
	@DeleteMapping("user/delete/{id}")
	public void deleteUser(@PathVariable("id") long id) {
		service.delete(id);
	}

	// Gets a list of users using the DTO
	private Stream<ReadUserDto> getUsers(List<User> users) {
		return users.stream().map(user -> {
			return new ReadUserDto(user);
		});
	}
}
