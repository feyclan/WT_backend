package nl.workingtalent.wtacademy.book;

import java.util.List;

public class SearchBookDto {

	private List<String> categories;

	private String title;

	private String authors;

	private int pageNr;
	
	private String description;
	
	private String SearchTerm;

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public int getPageNr() {
		return pageNr;
	}

	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSearchTerm() {
		return SearchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		SearchTerm = searchTerm;
	}

}
