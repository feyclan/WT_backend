package nl.workingtalent.wtacademy.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.workingtalent.wtacademy.dto.LoginRequestDto;
import nl.workingtalent.wtacademy.dto.LoginResponseDto;
import nl.workingtalent.wtacademy.dto.LogoutDto;
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

		return new ResponseDto(true, readUserDtoStream, null, (users.size() < 2) ? "user" : " users " + " found.");
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
		String hashedPassword = hashSHA256(dto.getPassword());
		newUser.setPassword(hashedPassword);
		newUser.setRole(dto.getRole());
		service.create(newUser);

		return new ResponseDto(true, null, null, "User created successfully.");
	}

	// UPDATE
	@PutMapping("user/update")
	public ResponseDto updateUser(@RequestBody UpdateUserDto dto) {

		// DOES USER EXIST?
		Optional<User> existingUser = service.findUserById(dto.getId());
		if (existingUser.isEmpty()) {
			return new ResponseDto(false, existingUser, null, "User doesn't exist.");
		}

		// Create SearchDto to search for a user with a certain email
		SearchUserDto searchDto = new SearchUserDto();
		searchDto.setEmail(dto.getEmail());

		// Check if email is already in use
		List<User> existingUserEmail = service.searchUser(searchDto);
		if (!existingUserEmail.isEmpty() && existingUserEmail.get(0).getId() != dto.getId()) {
			return new ResponseDto(false, existingUserEmail.get(0).getEmail(), null,
					"User with the provided email already exists.");
		}

		User dbUser = existingUser.get();

		// OVERWRITE
		dbUser.setFirstName(dto.getFirstName());
		dbUser.setLastName(dto.getLastName());
		dbUser.setEmail(dto.getEmail());
		String hashedPassword = hashSHA256(dto.getPassword());
		dbUser.setPassword(hashedPassword);
		dbUser.setRole(dto.getRole());

		// SAVE
		service.update(dbUser);
		return new ResponseDto(true, null, null, "User updated successfully.");
	}

	// DELETE
	@DeleteMapping("user/delete/{id}")
	public ResponseDto deleteUser(@PathVariable("id") long id) {
		Optional<User> user = service.findUserById(id);
		if (user.isEmpty()) {
			return new ResponseDto(false, user, null, "User doesn't exist.");
		}
		service.delete(id);
		ResponseDto responseDto = new ResponseDto(true, null, null, "User deleted successfully.");
		return responseDto;
	}

	@PostMapping("user/login")
	public ResponseDto login(@RequestBody LoginRequestDto dto) {
		String hashedPassword = hashSHA256(dto.getPassword());
		Optional<User> optionalUser = service.login(dto.getEmail(), hashedPassword);
		if (optionalUser.isEmpty()) {
			return new ResponseDto(false, null, null, "Gebruiker niet gevonden");
		}

		// Dit is de gevonden user
		User user = optionalUser.get();

		// Generate token -> Maak gebruik van apache commons
		user.setToken(RandomStringUtils.random(100, true, true));

		// User opslaan
		service.update(user);

		// Data terug esturen naar de frontend
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setName(user.getFirstName() + " " + user.getLastName());
		loginResponseDto.setToken(user.getToken());

		return new ResponseDto(true, loginResponseDto, null, null);
	}

	@PostMapping("user/logout")
	public ResponseDto logout(@RequestBody LogoutDto dto) {
		
		Optional<User> user = service.getUserByToken(dto.getToken());
		if(user.isEmpty()) {
			return new ResponseDto(false, null, null, "Geen gebruiker gevonden.");
		}
		
		User dbUser = user.get();
		dbUser.setToken(null);
		service.update(dbUser);
		
		return new ResponseDto(true, null, null, "Gebruiker is uitgelogd.");
	}	
	
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
