package com.proshore.powerplantsystem.services.battery;

import com.proshore.powerplantsystem.model.battery.Battery;
import com.proshore.powerplantsystem.payloads.battery.BatteriesInRangeResponse;
import com.proshore.powerplantsystem.payloads.battery.BatteryRangeRequestParams;

import java.util.List;

/**
 * The BatteryService interface defines for managing battery related operations.
 * Classes implementing this interface are responsible for implementing the business logic
 */
public interface BatteryService {

    /**
     * Creates a new battery record in the system.
     * @param battery The Battery object containing name, postcode, and capacity.
     * @return The created Battery object.
     */
    Battery createBattery(Battery battery);

    /**
     * Creates multiple battery records in the system.
     * @param batteries The list of Battery objects containing name, postcode, and capacity.
     * @return A list of created Battery objects.
     */
    List<Battery> createBatteries(List<Battery> batteries);

    /**
     * Retrieves a list of all battery records in the system.
     * @return A list of Battery objects.
     */
    List<Battery> getBatteries();

    /**
     * Retrieves a specific battery record by its unique battery Id.
     * @param batteryId The unique id of the Battery.
     * @return The Battery object.
     */
    Battery getBattery(Long batteryId);

    /**
     * Updates an existing battery record with new data.
     * @param batteryId The unique battery Id of the Battery to be updated.
     * @param battery   The updated Battery object containing name, postcode, and capacity.
     * @return The updated Battery object.
     */
    Battery updateBattery(Long batteryId, Battery battery);

    /**
     * Retrieves battery records within a specified postcode range along with statistics.
     * @param batteryRangeRequestParams The request parameters specifying the postcode range.
     * @return A response object containing a list of batteries within the range and statistics
     * such as total and average watt capacity.
     */
    BatteriesInRangeResponse getBatteriesInPostcodeRange(BatteryRangeRequestParams batteryRangeRequestParams);

}
