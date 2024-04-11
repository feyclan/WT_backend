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

		return createResponseDtoList(null, users, null);
	}

	@RequestMapping("user/{id}")
	public ResponseDto findUserById(@PathVariable("id") long id) {
		Optional<User> userOptional = service.findUserById(id);

		return createResponseDto(id, userOptional, "id");
	}

	@RequestMapping("user/search")
	public Stream<ReadUserDto> serachUser(@RequestBody SearchUserDto dto) {

		Stream<ReadUserDto> dtos = service.searchUser(dto).stream().map((user) -> {
			return new ReadUserDto(user);
		});

		return dtos;
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
			ResponseDto responseDto = new ResponseDto(false, existingUserEmail, null,
					"User with the provided email already exists.");
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

		// Create SearchDto to search for a user with a certain email
		SearchUserDto searchDto = new SearchUserDto();
		searchDto.setEmail(dto.getEmail());
		
		// Check if email is already in use
		List<User> existingUserEmail = service.searchUser(searchDto);
		if (!existingUserEmail.isEmpty() && existingUserEmail.get(0).getId() != id) {
			ResponseDto responseDto = new ResponseDto(false, existingUserEmail.get(0).getEmail(), null,
					"User with the provided email already exists.");
			return responseDto;
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
		ResponseDto responseDto = new ResponseDto(true, dto, null, "User updated successfully.");
		return responseDto;
	}

	// DELETE
	@DeleteMapping("user/delete/{id}")
	public ResponseDto deleteUser(@PathVariable("id") long id) {
		Optional<User> user = service.findUserById(id);
		if (user.isEmpty()) {
			ResponseDto responseDto = new ResponseDto(false, user, null, "User doesn't exist.");
			return responseDto;
		}
		service.delete(id);
		ResponseDto responseDto = new ResponseDto(true, null, null, "User deleted successfully.");
		return responseDto;
	}

	// Gets a list of users using the DTO
	private Stream<ReadUserDto> getUsers(List<User> users) {
		return users.stream().map(user -> {
			return new ReadUserDto(user);
		});
	}

	// Gets the reponseDto for objects who return a single value
	private ResponseDto createResponseDto(Object pathVal, Optional<User> userOptional, String pathVar) {
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			ReadUserDto readUserDto = new ReadUserDto(user);
			ResponseDto responseDto = new ResponseDto(true, readUserDto, null, "User found.");
			return responseDto;
		}
		ResponseDto responseDto = new ResponseDto(false, pathVal, null,
				"No users with " + pathVar + " '" + pathVal + "' found.");

		return responseDto;
	}

	// Gets the responseDto for objects who return a list of values
	private ResponseDto createResponseDtoList(Object pathVal, List<User> users, String pathVar) {
		if (users.isEmpty()) {
			ResponseDto responseDto = new ResponseDto(false, pathVal, null,
					"No users with the " + pathVar + " '" + pathVal + "' found.");
			return responseDto;
		}

		Stream<ReadUserDto> readUserDtoStream = getUsers(users);
		ResponseDto responseDto = new ResponseDto(true, readUserDtoStream, null,
				users.size() + " " + pathVar + " found.");

		return responseDto;
	}
}
