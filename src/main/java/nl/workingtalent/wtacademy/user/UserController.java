package nl.workingtalent.wtacademy.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.book.ReadAllBookDto;
import nl.workingtalent.wtacademy.dto.LoginRequestDto;
import nl.workingtalent.wtacademy.dto.LoginResponseDto;
import nl.workingtalent.wtacademy.dto.ResponseDto;

@RestController
@CrossOrigin(maxAge = 3600)
public class UserController {

	@Autowired
	private UserService service;

	/**
	 * Endpoint to get all users.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param pageNr The page number for pagination.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing a list of users and additional information.
	 */
	@PostMapping("user/all")
	public ResponseDto findAllUsers(@RequestBody int pageNr, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Page<User> users = service.findAllUsers(pageNr);

		if (users.isEmpty()) {
			return new ResponseDto(false, null, null, "No users found.");
		}

		Stream<ReadUserDto> readUserDtoStream = users.stream().map(user -> {
			return new ReadUserDto(user);
		});

		ReadAllUserDto dto = new ReadAllUserDto();
		dto.setTotalPages(users.getTotalPages());
		dto.setUsers(readUserDtoStream);

		return new ResponseDto(true, dto, null, (users.getNumberOfElements() < 2) ? "user" : " users " + " found.");
	}

	/**
	 * Endpoint to get a user by its id.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param id The id of the user to fetch.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the requested user and additional information.
	 */
	@RequestMapping("user/{id}")
	public ResponseDto findUserById(@PathVariable("id") long id, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<User> userOptional = service.findUserById(id);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			ReadUserDto readUserDto = new ReadUserDto(user);
			return new ResponseDto(true, readUserDto, null, "User found.");
		}

		return new ResponseDto(false, null, null, "No user with id '" + id + "' found.");
	}

	/**
	 * Endpoint to search for users based on certain criteria.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param dto The SearchUserDto object that contains the search criteria.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the search results and additional information.
	 */
	@PostMapping("user/search")
	public ResponseDto searchUser(@RequestBody SearchUserDto dto, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Page<User> users = service.searchUser(dto);

		Stream<ReadUserDto> dtos = service.searchUser(dto).stream().map((user) -> {
			return new ReadUserDto(user);
		});

		ReadAllUserDto resultDto = new ReadAllUserDto();
		resultDto.setTotalPages(users.getTotalPages());
		resultDto.setUsers(dtos);

		return new ResponseDto(true, resultDto, null, users.getNumberOfElements() + " users " + " found.");
	}

	/**
	 * Endpoint to add a new user.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param dto The CreateUserDto object that contains the details of the user to be added.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the added user and additional information.
	 */
	@PostMapping("user/create")
	public ResponseDto createUser(@RequestBody CreateUserDto dto, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		if (dto.getFirstName() == null || dto.getLastName() == null || dto.getFirstName().isBlank()
				|| dto.getLastName().isBlank()) {
			return new ResponseDto(false, null, null, "Name is required.");
		}
		if (dto.getEmail() == null || dto.getEmail().isBlank()) {
			return new ResponseDto(false, null, null, "Email is required.");
		}
		if (dto.getPassword() == null || dto.getPassword().isBlank()) {
			return new ResponseDto(false, null, null, "Password is required.");
		}
		if (dto.getRole() == null) {
			return new ResponseDto(false, null, null, "Role is required.");
		}

		// Create SearchDto to search for a user with a certain email
		SearchUserDto searchDto = new SearchUserDto();
		searchDto.setEmail(dto.getEmail());

		// Check if user exists
		List<User> existingUserEmail = service.searchUser(searchDto).toList(); 
		if (!existingUserEmail.isEmpty()) {
			return new ResponseDto(false, null, null, "User with the provided email already exists.");
		}

		User newUser = new User();
		newUser.setFirstName(dto.getFirstName());
		newUser.setLastName(dto.getLastName());
		newUser.setEmail(dto.getEmail());
		String hashedPassword = hashSHA256(dto.getPassword());
		newUser.setPassword(hashedPassword);
		newUser.setRole(dto.getRole());
		service.create(newUser);

		return new ResponseDto(true, null, null, "User created successfully.");
	}

	/**
	 * Endpoint to update an existing user.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param dto The UpdateUserDto object that contains the updated details of the user.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the updated user and additional information.
	 */
	@PutMapping("user/update")
	public ResponseDto updateUser(@RequestBody UpdateUserDto dto, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		// DOES USER EXIST?
		Optional<User> existingUser = service.findUserById(dto.getId());
		if (existingUser.isEmpty()) {
			return new ResponseDto(false, null, null, "User doesn't exist.");
		}

		// Create SearchDto to search for a user with a certain email
		SearchUserDto searchDto = new SearchUserDto();
		searchDto.setEmail(dto.getEmail());

		// Check if email is already in use
		List<User> existingUserEmail = service.searchUser(searchDto).toList();
		if (!existingUserEmail.isEmpty() && existingUserEmail.get(0).getId() != dto.getId()) {
			return new ResponseDto(false, existingUserEmail.get(0).getEmail(), null,
					"User with the provided email already exists.");
		}

		User dbUser = existingUser.get();

		// OVERWRITE
		if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
			dbUser.setFirstName(dto.getFirstName());
		}

		if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
			dbUser.setLastName(dto.getLastName());
		}

		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			dbUser.setEmail(dto.getEmail());
		}

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			String hashedPassword = hashSHA256(dto.getPassword());
			dbUser.setPassword(hashedPassword);
		}

		if (dto.getRole() != null) {
			dbUser.setRole(dto.getRole());
		}

		// SAVE
		service.update(dbUser);
		return new ResponseDto(true, null, null, "User updated successfully.");
	}

	/**
	 * Endpoint to delete a user by its id.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param id The id of the user to be deleted.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the deleted user and additional information.
	 */
	@DeleteMapping("user/delete/{id}")
	public ResponseDto deleteUser(@PathVariable("id") long id, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<User> user = service.findUserById(id);
		if (user.isEmpty()) {
			return new ResponseDto(false, user, null, "User doesn't exist.");
		}
		service.delete(id);
		ResponseDto responseDto = new ResponseDto(true, null, null, "User deleted successfully.");
		return responseDto;
	}

	/**
	 * Endpoint to login a user.
	 * @param dto The LoginRequestDto object that contains the login details of the user.
	 * @return A ResponseDto object containing the login response and additional information.
	 */
	@PostMapping("user/login")
	public ResponseDto login(@RequestBody LoginRequestDto dto) {
		String hashedPassword = hashSHA256(dto.getPassword());
		Optional<User> optionalUser = service.login(dto.getEmail(), hashedPassword);
		if (optionalUser.isEmpty()) {
			return new ResponseDto(false, null, null, "User not found.");
		}

		// Found user
		User user = optionalUser.get();

		// Generate token -> uses apache commons
		user.setToken(RandomStringUtils.random(100, true, true));

		// Store user
		service.update(user);

		// Send response to backend
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setName(user.getFirstName() + " " + user.getLastName());
		loginResponseDto.setToken(user.getToken());
		loginResponseDto.setRole(user.getRole().name());

		return new ResponseDto(true, loginResponseDto, null, null);
	}

	/**
	 * Endpoint to logout a user.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the logout response and additional information.
	 */
	@PostMapping("user/logout")
	public ResponseDto logout(HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		requestUser.setToken(null);
		service.update(requestUser);

		return new ResponseDto(true, null, null, "User is logged out.");
	}

	/**
	 * This method is used to hash a password using SHA-256.
	 * @param input The password to be hashed.
	 * @return The hashed password.
	 */
	private String hashSHA256(String input) {
		try {
			// Create MessageDigest instance for SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Add input string bytes to digest
			md.update(input.getBytes());

			// Get the hash's bytes
			byte[] bytes = md.digest();

			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
