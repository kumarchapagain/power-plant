package com.proshore.powerplantsystem.battery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proshore.powerplantsystem.model.battery.Battery;
import com.proshore.powerplantsystem.payloads.battery.BatteriesInRangeResponse;
import com.proshore.powerplantsystem.payloads.battery.BatteryRangeRequestParams;
import com.proshore.powerplantsystem.services.battery.BatteryService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class defines unit tests for the BatteryController class. It uses Spring Boot's testing framework to simulate HTTP requests and
 * interactions with the BatteryController, while mocking the BatteryService to control its behavior during testing.
 * <p>
 * The tests cover various scenarios for creating batteries, validating input, retrieving batteries, and getting batteries within a postcode range.
 * <p>
 * It also demonstrates how to use Spring Boot's testing utilities, such as @AutoConfigureMockMvc for setting up the MockMvc instance and
 * @MockBean for mocking the BatteryService.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BatteryControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private BatteryService batteryService;

    /**
     * This method sets up the behavior of the BatteryService's createBattery and createBatteries methods using Mockito's.
     * It ensures that when createBattery is called with any Battery object, it returns a Battery object with an ID.
     * When createBatteries is called with any list of Batteries, it assigns unique IDs to each Battery and returns the list.
     */
    @BeforeEach
    public void setUp() {
        when(batteryService.createBattery(any(Battery.class))).thenAnswer(invocation -> {
            Battery battery = invocation.getArgument(0);
            battery.setId(1L); // Database generated id
            return battery;
        });

        when(batteryService.createBatteries(anyList())).thenAnswer(invocation -> {
            List<Battery> batteries = invocation.getArgument(0);
            for (Battery battery : batteries) {
                battery.setId(generateUniqueId());
            }
            return batteries;
        });
    }

    /**
     * This test case validates the creation of a single battery through an HTTP POST request to the "/battery/create" endpoint.
     * It asserts that the response contains the expected battery details and HTTP status code 200 (OK).
     */
    @Test
    public void testCreateBattery() throws Exception {
        Battery battery = new Battery();
        battery.setName("Cannington");
        battery.setPostcode("6107");
        battery.setCapacity(13500);

        mockMvc.perform(post("/battery/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(battery)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cannington"))
                .andExpect(jsonPath("$.postcode").value("6107"))
                .andExpect(jsonPath("$.capacity").value(13500));
    }

    /**
     * This test case validates the validation of input when creating a battery.
     * It sends a POST request with an empty name field, expecting a 400 (Bad Request) response with an error message.
     */
    @Test
    public void testCreateBatteryValidation() throws Exception {
        Battery battery = new Battery();
        battery.setName("");
        battery.setPostcode("6107");
        battery.setCapacity(13500);

        mockMvc.perform(post("/battery/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(battery)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", Is.is("Name is mandatory")));
    }

    /**
     * This test case validates the creation of multiple batteries through an HTTP POST request to the "/battery/batteries" endpoint.
     * It serializes a list of batteries to JSON, sends the request, deserializes the response back to a list of batteries, and asserts
     * that the number of saved batteries matches the number of input batteries.
     */
    @Test
    public void testCreateBatteries() throws Exception {
        List<Battery> batteryList = List.of(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500)
        );

        // Serialize the list of batteries to JSON
        String jsonBatteries = objectMapper.writeValueAsString(batteryList);
        MvcResult mvcResult = mockMvc.perform(post("/battery/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBatteries))
                .andExpect(status().isOk())
                .andReturn();

        // Deserialize the response back to a list of batteries
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<Battery> savedBatteries = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        // Ensure the correct number of batteries were saved
        assertEquals(batteryList.size(), savedBatteries.size());
    }

    /**
     * This test case validates the retrieval of batteries through an HTTP GET request to the "/battery/batteries" endpoint.
     * It mocks the behavior of the BatteryService's getBatteries method to return a list of batteries and asserts that the response
     * contains the expected number of batteries.
     */
    @Test
    public void testGetBatteries() throws Exception {
        List<Battery> batteryList = List.of(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500)
        );

        List<Battery> savedBatteries = batteryService.createBatteries(batteryList);

        given(batteryService.getBatteries()).willReturn(savedBatteries);

        mockMvc.perform(get("/battery/batteries")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name", Is.is("Cannington")));
    }

    /**
     * This test case validates the retrieval of batteries within a postcode range through an HTTP POST request to the "/battery/range" endpoint.
     * It mocks the behavior of the BatteryService's getBatteriesInPostcodeRange method to return a response containing a list of batteries
     * within the specified range and total and average watt capacity statistics.
     */
    @Test
    public void testGetBatteriesInPostcodeRange() throws Exception {
        List<Battery> batteryList = List.of(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500),
                new Battery("Koolan Island", "6733", 10000)
        );

        BatteryRangeRequestParams batteryRangeRequestParams = new BatteryRangeRequestParams();
        batteryRangeRequestParams.setStartPostcode("6050");

        batteryRangeRequestParams.setEndPostcode("6200");

        // Mock the behavior of the service method for getting batteries in a postcode range
        BatteriesInRangeResponse batteriesInRangeResponse = new BatteriesInRangeResponse();
        batteriesInRangeResponse.setBatteriesInRange(List.of(
                new Battery("Cannington", "6107", 13500),
                new Battery("Midland", "6057", 50500)
        ));
        batteriesInRangeResponse.totalWattCapacity = 64000;
        batteriesInRangeResponse.averageWattCapacity = 32000;
        when(batteryService.getBatteriesInPostcodeRange(any()))
                .thenReturn(batteriesInRangeResponse);

        mockMvc.perform(post("/battery/range")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batteryRangeRequestParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteriesInRange.length()").value(2))
                .andExpect(jsonPath("$.batteriesInRange[0].name").value("Cannington"))
                .andExpect(jsonPath("$.totalWattCapacity").value(64000))
                .andExpect(jsonPath("$.averageWattCapacity").value(32000));

    }

    private static final AtomicLong idCounter = new AtomicLong(1);

    /**
     * A utility method for generating unique IDs for Battery entities during testing.
     */
    public static Long generateUniqueId() {
        return idCounter.getAndIncrement();
    }
}
