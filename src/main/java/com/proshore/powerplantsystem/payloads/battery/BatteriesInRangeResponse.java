package com.proshore.powerplantsystem.payloads.battery;

import com.proshore.powerplantsystem.model.battery.Battery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatteriesInRangeResponse {

	public List<Battery> batteriesInRange;
	public int totalWattCapacity = 0;
	public double averageWattCapacity = 0.0D;
}
