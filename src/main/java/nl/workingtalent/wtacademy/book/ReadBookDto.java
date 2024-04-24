package nl.workingtalent.wtacademy.book;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import nl.workingtalent.wtacademy.bookcopy.BookCopy;

public class ReadBookDto {

	private long id;

	private String description;

	private String isbn;

	private String title;

	private String imageLink;

	private List<String> authors;

	private List<String> categories;

	private int copyCount;

	private int availableCopyCount;

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
		categories = book.getCategories().stream().map(category -> category.getCategory()).collect(Collectors.toList());

		availableCopyCount = 0;
		List<BookCopy> copies = book.getBookCopies();
		for (BookCopy copy : copies) {
			if (copy.isAvailable()) {
				availableCopyCount++;
			}
		}

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

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public void setAvailableCopyCount(int availableCopyCount) {
		this.availableCopyCount = availableCopyCount;
	}

	public int getAvailableCopyCount() {
		return availableCopyCount;
	}

}