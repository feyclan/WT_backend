package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
	
	@Autowired
	private IReservationRepository repository;
	
	// READ
	public List<Reservation> findAllReservations() {
		return repository.findAll();
	}
	
	public Optional<Reservation> findReservationById(long id) {
		return repository.findById(id);
	}
	
	public List<Reservation> findReservationByReservationRequest(boolean request) {
		return repository.findReservationByReservationRequest(request);
	}
	
	public List<Reservation> findReservationByRequestDate(LocalDate date) {
		return repository.findReservationByRequestDate(date);
	}
	
	// CREATE
	public void create(Reservation reservation) {
		repository.save(reservation);
	}
	
	// UPDATE
	public void update(Reservation dbReservation) {
		repository.save(dbReservation);
	}

	// DELETE
	public void delete(long id) {
		repository.deleteById(id);
	}

}
