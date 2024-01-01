package com.vytsablinskas.flavorfare.database.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String extraInformation;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;
}