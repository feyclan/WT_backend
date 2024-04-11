package nl.workingtalent.wtacademy.category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long>{

	Optional<Category> findOneByCategory(String category);
}
