package nl.workingtalent.wtacademy.review;

import java.util.stream.Stream;

public class ReadAllReviewDto {

    private Stream<ReadReviewDto> dto;

    private int totalPages;

    public Stream<ReadReviewDto> getDto() {
        return dto;
    }

    public void setDto(Stream<ReadReviewDto> dto) {
        this.dto = dto;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
