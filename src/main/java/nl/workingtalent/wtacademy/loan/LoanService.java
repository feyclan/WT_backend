package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import nl.workingtalent.wtacademy.bookcopy.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

	@Autowired
	private ILoanRepository repository;

	// READ
	public List<Loan> findAllLoans() {
		return repository.findAll();
	}

	public Optional<Loan> findLoanById(long id) {
		return repository.findById(id);
	}

	/**
	 * This method is used to search for loans based on certain criteria.
	 * The search criteria include start date, end date, start condition, end condition, and active status.
	 * The search results are fetched from the repository using the provided criteria.
	 *
	 * @param searchLoanDto The SearchLoanDto object that contains the search criteria.
	 * @return A list of Loan objects that match the search criteria.
	 */
	public List<Loan> searchLoans(SearchLoanDto searchLoanDto) {
		LocalDate startDate = searchLoanDto.getStartDate();
		LocalDate endDate = searchLoanDto.getEndDate();
		State conditionStart = searchLoanDto.getConditionStart();
		State conditionEnd = searchLoanDto.getConditionEnd();
		Boolean isActive = searchLoanDto.getIsActive();
		return repository.search(startDate, endDate, conditionStart, conditionEnd, isActive);
	}

	// CREATE
	public void create(Loan loan) {
		repository.save(loan);
	}

	// UPDATE
	public void update(Loan dbLoan) {
		repository.save(dbLoan);
	}

	// DELETE
	public void delete(long id) {
		repository.deleteById(id);
	}

	public List<Loan> findAllLoansForUser(long id) {
		return repository.findByUserId(id);
	}

}
