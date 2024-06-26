package com.proshore.powerplantsystem.model.battery;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Post code is mandatory")
    private String postcode;

    private Integer capacity;

    public Battery(String name, String postcode, int capacity) {
        this.name = name;
        this.postcode = postcode;
        this.capacity = capacity;
    }
}
