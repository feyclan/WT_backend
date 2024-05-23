package nl.workingtalent.wtacademy.author;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

	@InjectMocks
    private AuthorService service;

	// Vervangende repository
    @Mock
    private IAuthorRepository repo;

    @Test
    public void test_addAuthor_withvalidauthor() {
    	Mockito.when(repo.findOneByName("Author 3")).thenReturn(Optional.empty());
    	
    	service.addAuthor("Author 3");
    	
    	Mockito.verify(repo, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void test_addAuthor_withinvalidauthor() {
    	Mockito.when(repo.findOneByName("Author 1")).thenReturn(Optional.of(new Author()));
    	
    	service.addAuthor("Author 1");
    	
    	Mockito.verify(repo, Mockito.times(0)).save(Mockito.any());
    }

}
