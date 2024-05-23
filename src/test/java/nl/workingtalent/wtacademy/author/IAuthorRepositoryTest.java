package nl.workingtalent.wtacademy.author;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class IAuthorRepositoryTest {

	@Autowired
	private IAuthorRepository repo;
	
	@Test
	public void test_findOneByName_found() {
		// Input / Start situation 
		String searchName = "Author 2";
		
		// Code / Actie
		Optional<Author> optional = repo.findOneByName(searchName);
		
		// Output / End situation
		Assertions.assertTrue(optional.isPresent());
		Assertions.assertEquals("Author 2", optional.get().getName());
	}
	
	
	@Test
	public void test_findOneByName_notfound() {
		// Input / Start situation 
		String searchName = "zzzzzzz";
		
		// Code / Actie
		Optional<Author> optional = repo.findOneByName(searchName);
		
		// Output / End situation
		Assertions.assertTrue(optional.isEmpty());
	}
	
}
