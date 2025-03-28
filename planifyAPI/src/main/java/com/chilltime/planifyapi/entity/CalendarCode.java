package com.chilltime.planifyapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CalendarCode {
    @OneToOne(mappedBy = "codigo")
    private Calendar calendario;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private boolean usado = false;

    @Version
    private long version;
}
