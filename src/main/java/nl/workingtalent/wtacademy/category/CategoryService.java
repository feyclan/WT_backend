package nl.workingtalent.wtacademy.category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private ICategoryRepository repository;
	
	public void addCategory(String category) {
		Optional<Category> dbCategory = getCategoryByName(category);
		if (dbCategory.isEmpty()) {
			Category newCategory = new Category();
			newCategory.setCategory(category);
			repository.save(newCategory);
		}
	}

	public Optional<Category> getCategoryByName(String category) {
		return repository.findOneByCategory(category);
	}
}
