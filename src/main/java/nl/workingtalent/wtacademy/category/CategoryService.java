package nl.workingtalent.wtacademy.category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private ICategoryRepository repository;

	/**
	 * This method is used to add a new category to the database.
	 * Before adding, it checks if the category already exists in the database.
	 * If the category does not exist, it creates a new Category object, sets the category name, and saves it to the database.
	 *
	 * @param category The name of the category to be added.
	 */
	public void addCategory(String category) {
		// Fetch the category from the database
		Optional<Category> dbCategory = getCategoryByName(category);

		// Check if the category already exists
		if (dbCategory.isEmpty()) {
			// If not, create a new Category object
			Category newCategory = new Category();

			// Set the category name
			newCategory.setCategory(category);

			// Save the new category to the database
			repository.save(newCategory);
		}
	}

	public Optional<Category> getCategoryByName(String category) {
		return repository.findOneByCategory(category);
	}
}
