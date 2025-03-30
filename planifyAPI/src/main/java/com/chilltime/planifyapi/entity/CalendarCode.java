package com.chilltime.planifyapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CalendarCode {
    @ManyToOne()
    private Calendar calendar;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private boolean used;

    @Version
    private long version;
}
