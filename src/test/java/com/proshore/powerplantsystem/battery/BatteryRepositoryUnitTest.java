package com.proshore.powerplantsystem.battery;

import com.proshore.powerplantsystem.model.battery.Battery;
import com.proshore.powerplantsystem.repositories.battery.BatteryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BatteryRepositoryUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BatteryRepository batteryRepository;

    @Test
    public void testFindByPostcodeExistingBattery() {

        // Arrange: Insert a battery with a specific postcode into the database
        Battery battery = new Battery("Cannington", "6107", 13500);
        entityManager.persist(battery);
        entityManager.flush();

        // Call the custom query method findByPostcode
        Battery dbBattery = batteryRepository.findByPostcode("6107");

        assertEquals("Cannington", dbBattery.getName());
        assertEquals("6107", dbBattery.getPostcode());
        assertEquals(13500, dbBattery.getCapacity());
    }

    @Test
    public void testFindByPostcodeNonExistingBattery() {

        // Call the custom query method findByPostcode with a non-existing postcode
        Battery foundBattery = batteryRepository.findByPostcode("6107");

        // Verify that null is returned because the battery doesn't exist
        assertNull(foundBattery);
    }
}
