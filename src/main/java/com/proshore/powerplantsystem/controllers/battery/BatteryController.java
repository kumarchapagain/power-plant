package com.proshore.powerplantsystem.controllers.battery;

import com.proshore.powerplantsystem.model.battery.Battery;
import com.proshore.powerplantsystem.payloads.battery.BatteriesInRangeResponse;
import com.proshore.powerplantsystem.payloads.battery.BatteryRangeRequestParams;
import com.proshore.powerplantsystem.services.battery.BatteryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The BatteryController class defines a REST API controller responsible for managing battery-related operations.
 * It handles incoming HTTP requests related to batteries and delegates the business logic to the BatteryService.
 */
@RestController
@RequestMapping("/battery")
public class BatteryController {

    private final BatteryService batteryService;

    /**
     * Constructor for BatteryController, injecting the BatteryService dependency.
     * @param batteryService The BatteryService responsible for handling battery operations.
     */
    public BatteryController(final BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    /**
     * Endpoint for creating a single battery record.
     * @param battery The Battery object to be created.
     * @return ResponseEntity with the created Battery object and a 200 (OK) status code.
     */
    @PostMapping("/create")
    public ResponseEntity<Battery> createBattery(@Valid @RequestBody Battery battery) {
        Battery savedBattery = batteryService.createBattery(battery);
        return new ResponseEntity<>(savedBattery, HttpStatus.OK);
    }

    /**
     * Endpoint for creating multiple battery records.
     * @param batteries The list of Battery objects to be created.
     * @return ResponseEntity with a list of created Battery objects and a 200 (OK) status code.
     */
    @PostMapping("/batteries")
    public ResponseEntity<List<Battery>> createBatteries(@Valid @RequestBody List<Battery> batteries) {
        List<Battery> savedBatteries = batteryService.createBatteries(batteries);
        return new ResponseEntity<>(savedBatteries, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving a list of all battery records.
     * @return ResponseEntity with a list of Battery objects and a 200 (OK) status code.
     */
    @GetMapping("/batteries")
    public ResponseEntity<List<Battery>> getBatteries() {
        List<Battery> batteries = batteryService.getBatteries();
        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving battery records within a specified postcode range.
     * @param batteryRangeRequestParams The request parameters containing startPostcode and endPostcode.
     * @return ResponseEntity with a BatteriesInRangeResponse containing the filtered battery list and statistics, along with a 200 (OK) status code.
     */
    @PostMapping("/range")
    public ResponseEntity<BatteriesInRangeResponse> getBatteriesInPostcodeRange(@Valid @RequestBody BatteryRangeRequestParams batteryRangeRequestParams) {
        BatteriesInRangeResponse batteriesInRangeResponses = batteryService.getBatteriesInPostcodeRange(batteryRangeRequestParams);
        return new ResponseEntity<>(batteriesInRangeResponses, HttpStatus.OK);
    }
}
