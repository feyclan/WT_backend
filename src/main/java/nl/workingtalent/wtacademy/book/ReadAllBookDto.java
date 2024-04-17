package nl.workingtalent.wtacademy.book;

import java.util.stream.Stream;

public class ReadAllBookDto {

	private int totalPages;

	private Stream<ReadBookDto> books;

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public Stream<ReadBookDto> getBooks() {
		return books;
	}

	public void setBooks(Stream<ReadBookDto> books) {
		this.books = books;
	}

}
