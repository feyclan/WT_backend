package nl.workingtalent.wtacademy.book;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReadBookDto {

	private long id;
	
	private String description;
	
	private String isbn;
	
	private String title;
	
	private String imageLink;
	
	private List<String> authors;
	
	private int copyCount;
	
	private LocalDate publishingDate;
	
	public ReadBookDto(Book book) {
		id = book.getId();
		title = book.getTitle();
		authors = book.getAuthors().stream().map(author -> author.getName()).collect(Collectors.toList());                 
		copyCount = book.getBookCopies().size();
		description = book.getDescription();
		isbn = book.getIsbn();
		imageLink = book.getImageLink();
		publishingDate = book.getPublishingDate();
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCopyCount() {
		return copyCount;
	}

	public void setCopyCount(int copyCount) {
		this.copyCount = copyCount;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public LocalDate getPublishingDate() {
		return publishingDate;
	}

	public void setPublishingDate(LocalDate publishingDate) {
		this.publishingDate = publishingDate;
	}
	
	
	
}