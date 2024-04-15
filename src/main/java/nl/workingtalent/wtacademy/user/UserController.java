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
	public ResponseDto findAllUsers() {
		List<User> users = service.findAllUsers();

		if (users.isEmpty()) {
			return new ResponseDto(false, null, null, "No users found.");
		}

		Stream<ReadUserDto> readUserDtoStream = users.stream().map(user -> {
			return new ReadUserDto(user);
		});

		return new ResponseDto(true, readUserDtoStream, null, users.size() + " users " + " found.");
	}

	@RequestMapping("user/{id}")
	public ResponseDto findUserById(@PathVariable("id") long id) {
		Optional<User> userOptional = service.findUserById(id);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			ReadUserDto readUserDto = new ReadUserDto(user);
			return new ResponseDto(true, readUserDto, null, "User found.");
		}

		return new ResponseDto(false, null, null, "No user with id '" + id + "' found.");
	}

	@RequestMapping("user/search")
	public ResponseDto searchUser(@RequestBody SearchUserDto dto) {
		List<User> users = service.searchUser(dto);

		Stream<ReadUserDto> dtos = service.searchUser(dto).stream().map((user) -> {
			return new ReadUserDto(user);
		});

		return new ResponseDto(true, dtos, null, users.size() + " users " + " found.");
	}

	// CREATE
	@PostMapping("user/create")
	public ResponseDto createUser(@RequestBody CreateUserDto dto) {

		// Create SearchDto to search for a user with a certain email
		SearchUserDto searchDto = new SearchUserDto();
		searchDto.setEmail(dto.getEmail());

		// Check if user exists
		List<User> existingUserEmail = service.searchUser(searchDto);
		if (!existingUserEmail.isEmpty()) {
			return new ResponseDto(false, existingUserEmail, null, "User with the provided email already exists.");
		}

		User newUser = new User();
		newUser.setFirstName(dto.getFirstName());
		newUser.setLastName(dto.getLastName());
		newUser.setEmail(dto.getEmail());
		newUser.setPassword(dto.getPassword());
		newUser.setRole(dto.getRole());
		service.create(newUser);

		return new ResponseDto(true, newUser, null, "User created successfully.");
	}

	// UPDATE
	@PutMapping("user/update/{id}")
	public ResponseDto updateUser(@RequestBody UpdateUserDto dto, @PathVariable("id") long id) {

		// DOES USER EXIST?
		Optional<User> existingUser = service.findUserById(id);
		if (existingUser.isEmpty()) {
			return new ResponseDto(false, existingUser, null, "User doesn't exist.");
		}

		// Create SearchDto to search for a user with a certain email
		SearchUserDto searchDto = new SearchUserDto();
		searchDto.setEmail(dto.getEmail());

		// Check if email is already in use
		List<User> existingUserEmail = service.searchUser(searchDto);
		if (!existingUserEmail.isEmpty() && existingUserEmail.get(0).getId() != id) {
			return new ResponseDto(false, existingUserEmail.get(0).getEmail(), null,
					"User with the provided email already exists.");
		}

		User dbUser = existingUser.get();

		// OVERWRITE
		dbUser.setFirstName(dto.getFirstName());
		dbUser.setLastName(dto.getLastName());
		dbUser.setEmail(dto.getEmail());
		dbUser.setPassword(dto.getPassword());
		dbUser.setRole(dto.getRole());

		// SAVE
		service.update(dbUser);
		return new ResponseDto(true, dto, null, "User updated successfully.");
	}

	// DELETE
	@DeleteMapping("user/delete/{id}")
	public ResponseDto deleteUser(@PathVariable("id") long id) {
		Optional<User> user = service.findUserById(id);
		if (user.isEmpty()) {
			return new ResponseDto(false, user, null, "User doesn't exist.");
		}
		service.delete(id);
		return new ResponseDto(true, null, null, "User deleted successfully.");
	}
}
