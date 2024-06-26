package com.proshore.powerplantsystem.payloads.battery;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatteryRangeRequestParams {

    @NotBlank(message = "Start post code is mandatory")
    public String startPostcode;

    @NotBlank(message = "End post code is mandatory")
    public String endPostcode;
}
