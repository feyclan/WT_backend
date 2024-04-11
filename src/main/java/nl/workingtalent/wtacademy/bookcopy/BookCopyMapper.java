package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class BookCopyMapper {
	
	public Stream<ReadBookCopyDto> bookCopyListToDtos(List<BookCopy> copies){
		Stream<ReadBookCopyDto> dtos = copies.stream().map((copy) -> {
			return new ReadBookCopyDto(copy);
		});
		return dtos;
	}
}
