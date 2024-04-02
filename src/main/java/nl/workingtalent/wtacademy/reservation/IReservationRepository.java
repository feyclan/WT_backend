package nl.workingtalent.wtacademy.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservationRepository extends JpaRepository<Reservation, Long>{

}
