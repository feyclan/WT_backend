package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

	/**
	 * This method is used to search for reservations based on certain criteria.
	 * The search criteria include reservation request and request date.
	 * If the reservation request is null, it will return all reservations.
	 * If the request date is null, it will return all reservations.
	 * If both are provided, it will return reservations that match both criteria.
	 *
	 * @param request The ReservationRequest object that contains the reservation request criteria.
	 * @param date The LocalDate object that contains the request date criteria.
	 * @return A list of Reservation objects that match the search criteria.
	 */
	@Query("SELECT r FROM Reservation r " + "WHERE (:request is null OR r.reservationRequest = :request) "
			+ "AND (:date is null OR r.requestDate = :date)")
	List<Reservation> search(@Param("request") ReservationRequest request, @Param("date") LocalDate date);

	List<Reservation> findByUserId(long userId);

}