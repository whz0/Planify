package com.chilltime.planifyapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String type;
    private boolean active;

    @Version
    private long version;

    @Transient
    private Long id_client;

    @ManyToOne
    @JsonBackReference
    private Client client;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "calendar_event",
            joinColumns = @JoinColumn(name = "calendar_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonManagedReference
    private Set<Event> events;

}