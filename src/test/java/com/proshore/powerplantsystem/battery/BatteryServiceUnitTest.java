package com.proshore.powerplantsystem.battery;

import com.proshore.powerplantsystem.exceptions.APIException;
import com.proshore.powerplantsystem.exceptions.DataNotFoundException;
import com.proshore.powerplantsystem.model.battery.Battery;
import com.proshore.powerplantsystem.payloads.battery.BatteriesInRangeResponse;
import com.proshore.powerplantsystem.payloads.battery.BatteryRangeRequestParams;
import com.proshore.powerplantsystem.repositories.battery.BatteryRepository;
import com.proshore.powerplantsystem.services.battery.BatteryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BatteryServiceUnitTest {

    @InjectMocks
    private BatteryServiceImpl batteryService;

    @Mock
    private BatteryRepository batteryRepository;

    List<Battery> batteries = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        batteries = Arrays.asList(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500),
                new Battery("Mount Adams", "6525", 12000)
        );
    }

    @Test
    public void testCreateBattery() {
        Battery battery = new Battery("Cannington", "6107", 13500);

        when(batteryRepository.findByPostcode("6107")).thenReturn(null);
        when(batteryRepository.save(battery)).thenReturn(battery);

        Battery createdBattery = batteryService.createBattery(battery);

        verify(batteryRepository, times(1)).findByPostcode("6107");
        verify(batteryRepository, times(1)).save(battery);

        assertNotNull(createdBattery);
        assertEquals("Cannington", createdBattery.getName());
        assertEquals("6107", createdBattery.getPostcode());
        assertEquals(13500, createdBattery.getCapacity());
    }

    @Test
    public void testCreateBatteryAlreadyExists() {
        Battery existingBattery = new Battery("Cannington", "6107", 13500);
        Battery newBattery = new Battery("Midland", "6107", 50500);

        when(batteryRepository.findByPostcode("6107")).thenReturn(existingBattery);

        // Ensure that creating a new battery with the same postcode throws an APIException
        assertThrows(APIException.class, () -> batteryService.createBattery(newBattery));
    }

    @Test
    public void testCreateBatteries() {
        List<Battery> batteries = Arrays.asList(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500)
        );

        when(batteryRepository.saveAll(batteries)).thenReturn(batteries);

        List<Battery> savedBatteries = batteryService.createBatteries(batteries);

        verify(batteryRepository, times(1)).saveAll(batteries);

        assertNotNull(savedBatteries);
        assertEquals(2, savedBatteries.size());
        assertEquals("Cannington", savedBatteries.get(0).getName());
        assertEquals("Midland", savedBatteries.get(1).getName());
    }

    @Test
    public void testGetBattery() {
        Long batteryId = 1L;
        Battery battery = new Battery("Cannington", "6107", 13500);

        when(batteryRepository.findById(batteryId)).thenReturn(Optional.of(battery));

        Battery retrievedBattery = batteryService.getBattery(batteryId);

        verify(batteryRepository, times(1)).findById(batteryId);

        assertNotNull(retrievedBattery);
        assertEquals("Cannington", retrievedBattery.getName());
        assertEquals("6107", retrievedBattery.getPostcode());
        assertEquals(13500, retrievedBattery.getCapacity());
    }

    @Test
    public void testGetBatteryNotFound() {
        Long batteryId = 1L;

        when(batteryRepository.findById(batteryId)).thenReturn(Optional.empty());

        // Retrieve a non-existent battery throws a DataNotFoundException
        assertThrows(DataNotFoundException.class, () -> batteryService.getBattery(batteryId));
    }

    @Test
    public void testGetBatteriesInPostcodeRange() {
        List<Battery> batteries = Arrays.asList(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500)
        );

        BatteryRangeRequestParams requestParams = new BatteryRangeRequestParams("6050", "6200");

        when(batteryRepository.findAll()).thenReturn(batteries);

        BatteriesInRangeResponse response = batteryService.getBatteriesInPostcodeRange(requestParams);

        verify(batteryRepository, times(1)).findAll();

        assertNotNull(response);
        assertNotNull(response.getBatteriesInRange());
        assertEquals(2, response.getBatteriesInRange().size());
        assertEquals("Cannington", response.getBatteriesInRange().get(0).getName());
        assertEquals("Midland", response.getBatteriesInRange().get(1).getName());
        assertEquals(64000, response.getTotalWattCapacity());
        assertEquals(32000, response.getAverageWattCapacity());
    }

    @Test
    public void testGetBatteriesInPostcodeRangeForNoBatteries() {

        BatteryRangeRequestParams requestParams = new BatteryRangeRequestParams("6050", "6200");

        when(batteryRepository.findAll()).thenReturn(batteries);

        BatteriesInRangeResponse response = batteryService.getBatteriesInPostcodeRange(requestParams);

        verify(batteryRepository, times(1)).findAll();

        assertNotNull(response);
        assertNotNull(response.getBatteriesInRange());
        assertEquals(2, response.getBatteriesInRange().size());
        assertEquals(64000, response.getTotalWattCapacity());
        assertEquals(32000, response.getAverageWattCapacity());
    }

    @Test
    public void testCalculateBatteriesInPostcodeRange() {

        List<Battery> batteriesInRange = BatteryServiceImpl
                .calculateBatteriesInPostcodeRange(batteries, "6050", "6200");

        assertEquals(2, batteriesInRange.size());
    }

    @Test
    public void testCalculateTotalWattCapacity() {

        int totalWattCapacity = BatteryServiceImpl
                .calculateTotalWattCapacity(batteries);

        assertEquals(76000, totalWattCapacity);
    }

    @Test
    public void testCalculateAverageWattCapacity() {

        double totalWattCapacity = BatteryServiceImpl.calculateAverageWattCapacity(batteries);

        assertEquals(25333.333333333332, totalWattCapacity);
    }
}
