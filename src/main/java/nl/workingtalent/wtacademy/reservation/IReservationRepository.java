package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IReservationRepository extends JpaRepository<Reservation, Long>{
	
	List<Reservation> findReservationByRequest(boolean request);
	List<Reservation> findReservationByDate(LocalDate date);

}
