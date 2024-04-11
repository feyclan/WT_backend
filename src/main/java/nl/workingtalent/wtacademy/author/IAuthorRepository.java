package nl.workingtalent.wtacademy.author;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepository extends JpaRepository<Author, Long>{

	Optional<Author> findOneByName(String author);
	

}
