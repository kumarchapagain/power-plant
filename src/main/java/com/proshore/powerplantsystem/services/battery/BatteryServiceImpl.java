package com.proshore.powerplantsystem.services.battery;

import com.proshore.powerplantsystem.exceptions.APIException;
import com.proshore.powerplantsystem.exceptions.DataNotFoundException;
import com.proshore.powerplantsystem.model.battery.Battery;
import com.proshore.powerplantsystem.payloads.battery.BatteriesInRangeResponse;
import com.proshore.powerplantsystem.payloads.battery.BatteryRangeRequestParams;
import com.proshore.powerplantsystem.repositories.battery.BatteryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class BatteryServiceImpl implements BatteryService {

	private final BatteryRepository batteryRepository;

	public BatteryServiceImpl(final BatteryRepository batteryRepository) {
		this.batteryRepository = batteryRepository;
	}

	@Override
	public Battery createBattery(Battery battery) {
		Battery batteryFromDb = batteryRepository.findByPostcode(battery.getPostcode());
		if (batteryFromDb != null) {
			throw new APIException("Battery already exists with battery post code: " + batteryFromDb.getPostcode());
		}
		log.info("Saving battery with post code: " + battery.getPostcode());
		return batteryRepository.save(battery);
	}

	@Override
	public List<Battery> createBatteries(List<Battery> batteries) {
		return batteryRepository.saveAll(batteries);
	}

	@Override
	public List<Battery> getBatteries() {
		return batteryRepository.findAll();
	}

	@Override
	public Battery getBattery(Long batteryId) {
		return batteryRepository.findById(batteryId)
				.orElseThrow(() -> new DataNotFoundException("Battery", "batteryId", batteryId));
	}

	@Override
	public Battery updateBattery(Long batteryId, Battery battery) {
		return null;
	}

	@Override
	public BatteriesInRangeResponse getBatteriesInPostcodeRange(BatteryRangeRequestParams batteryRangeRequestParams) {
		BatteriesInRangeResponse batteriesInRangeResponses = new BatteriesInRangeResponse();
		List<Battery> batteries = batteryRepository.findAll();
		List<Battery> batteriesInRange = calculateBatteriesInPostcodeRange(batteries, batteryRangeRequestParams.startPostcode, batteryRangeRequestParams.endPostcode);
		batteriesInRangeResponses.batteriesInRange = batteriesInRange;
		batteriesInRangeResponses.totalWattCapacity = calculateTotalWattCapacity(batteriesInRange);
		batteriesInRangeResponses.averageWattCapacity = calculateAverageWattCapacity(batteriesInRange);
		return batteriesInRangeResponses;
	}

	/**
	 * Filters a list of batteries to include only those within a specified postcode range
	 * and sorts them alphabetically by name.
	 *
	 * @param batteries     The list of Battery objects to filter.
	 * @param startPostCode The starting postcode of the range (inclusive).
	 * @param endPostcode   The ending postcode of the range (inclusive).
	 * @return A filtered and sorted list of Battery objects within the specified postcode range.
	 */
	public static List<Battery> calculateBatteriesInPostcodeRange(List<Battery> batteries, String startPostCode, String endPostcode) {
		return batteries.stream()
				.filter(battery -> battery.getPostcode().compareTo(startPostCode) >= 0 && battery.getPostcode().compareTo(endPostcode) <= 0)
				.sorted(Comparator.comparing(Battery::getName))
				.collect(Collectors.toList());
	}

	/**
	 * Calculates the total watt capacity of a list of batteries.
	 * @param batteriesInRange The list of Battery objects for which to calculate the total watt capacity.
	 * @return The total watt capacity of the batteries in the list.
	 */
	public static int calculateTotalWattCapacity(List<Battery> batteriesInRange) {
		return batteriesInRange.stream().mapToInt(Battery::getCapacity).sum();
	}

	/**
	 * Calculates the average watt capacity of a list of batteries.
	 * @param batteriesInRange The list of Battery objects for which to calculate the average watt capacity.
	 * @return The average watt capacity of the batteries in the list. If the list is empty, it returns 0.0.
	 */
	public static double calculateAverageWattCapacity(List<Battery> batteriesInRange) {
		return batteriesInRange.stream().mapToInt(Battery::getCapacity).average().orElse(0.0);
	}
}
