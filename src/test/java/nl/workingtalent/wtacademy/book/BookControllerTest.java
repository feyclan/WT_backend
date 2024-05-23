package nl.workingtalent.wtacademy.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.dto.ResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

	@LocalServerPort
	private int serverPort;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	public void test_bookcreate_with_validation1() {
		Book book = new Book();

		ResponseEntity<ResponseDto> responseEntity = testRestTemplate.postForEntity("http://localhost:" + serverPort + "/book/create", book, ResponseDto.class);
		ResponseDto responseDto = responseEntity.getBody();

		Assertions.assertEquals(200, responseEntity.getStatusCode().value());

		Assertions.assertFalse(responseDto.isSuccess());
		Assertions.assertEquals("Title is required.", responseDto.getValidationMessage());
	}

	@Test
	public void test_bookcreate_with_validation2() {
		Book book = new Book();
		book.setTitle("Mijn leuke boek");

		ResponseEntity<ResponseDto> responseEntity = testRestTemplate.postForEntity("http://localhost:" + serverPort + "/book/create", book, ResponseDto.class);
		ResponseDto responseDto = responseEntity.getBody();

		Assertions.assertEquals(200, responseEntity.getStatusCode().value());

		Assertions.assertFalse(responseDto.isSuccess());
		Assertions.assertEquals("Description is required.", responseDto.getValidationMessage());
	}

}
