package com.chilltime.planifyapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    private LocalTime time;

    private String location;

    private boolean active;

    @Version
    private long version;

    @ManyToMany(mappedBy = "events")
    @JsonBackReference
    private Set<Calendar> calendars;
}
