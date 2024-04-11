package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

	// JPQL
	@Query("SELECT r FROM Reservation r " + "WHERE (:request is null OR r.reservationRequest = :request) "
			+ "AND (:date is null OR r.requestDate = :date)")
	List<Reservation> search(@Param("request") boolean request, @Param("date") LocalDate date);

}
