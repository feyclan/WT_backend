package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ILoanRepository extends JpaRepository<Loan, Long> {

	@Query("SELECT l FROM Loan l WHERE " + "(:startDate IS NULL OR l.startDate = :startDate) AND "
			+ "(:endDate IS NULL OR l.endDate = :endDate) AND "
			+ "(:conditionStart IS NULL OR l.conditionStart = :conditionStart) AND "
			+ "(:conditionEnd IS NULL OR l.conditionEnd = :conditionEnd) AND "
			+ "(:isActive IS NULL OR l.isActive = :isActive)")
	List<Loan> search(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("conditionStart") String conditionStart, @Param("conditionEnd") String conditionEnd,
			@Param("isActive") Boolean isActive);
	
	List<Loan> findByUserId(long userId);
}
