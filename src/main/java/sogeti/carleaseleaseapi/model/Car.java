package sogeti.carleaseleaseapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String make;

    private String model;

    private String version;

    @Column(name = "number_of_doors")
    private int numberOfDoors;

    @Column(name = "co2_emission")
    private int co2Emission;

    @Column(name = "gross_price")
    private BigDecimal grossPrice;

    @Column(name = "nett_price")
    private BigDecimal nettPrice;

    public Car() {

    }
}
