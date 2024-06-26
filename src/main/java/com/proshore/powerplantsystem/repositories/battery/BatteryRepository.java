package com.proshore.powerplantsystem.repositories.battery;


import com.proshore.powerplantsystem.model.battery.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The BatteryRepository interface extends the Spring Data JPA `JpaRepository` and provides
 * methods for performing CRUD (Create, Read, Update, Delete) operations on Battery entities.
 * It enables interaction with the underlying database for battery related data.
 */
@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {

	/**
	 * Retrieves a Battery entity by its postcode.
	 * @param postCode The postcode to search for.
	 * @return A Battery object matching the specified postcode or null if not found.
	 */
	Battery findByPostcode(String postCode);

}
