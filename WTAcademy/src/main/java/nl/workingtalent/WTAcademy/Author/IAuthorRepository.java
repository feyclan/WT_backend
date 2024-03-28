package nl.workingtalent.WTAcademy.Author;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepository extends JpaRepository<Author, Integer>{

	Optional<Author> findOneByAuthorName(String author);
}
