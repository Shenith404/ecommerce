package com.example.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deal extends BaseModel {

    private double discount;

    @OneToOne
    @JsonBackReference
    private HomeCategory category;

}
