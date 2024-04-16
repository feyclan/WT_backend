package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "endDate"));
		Page<Loan> page = repository.findAll(pageable);
		return page.toList();
	}

	public Optional<Loan> findLoanById(long id) {
		return repository.findById(id);
	}

	public List<Loan> searchLoans(SearchLoanDto searchLoanDto) {
		LocalDate startDate = searchLoanDto.getStartDate();
		LocalDate endDate = searchLoanDto.getEndDate();
		String conditionStart = searchLoanDto.getConditionStart();
		String conditionEnd = searchLoanDto.getConditionEnd();
		return repository.search(startDate, endDate, conditionStart, conditionEnd);
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

}
