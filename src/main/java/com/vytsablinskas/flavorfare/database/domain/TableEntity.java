package com.vytsablinskas.flavorfare.database.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="tables")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer tableId;

    private Integer size;

    private Integer count;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;
}