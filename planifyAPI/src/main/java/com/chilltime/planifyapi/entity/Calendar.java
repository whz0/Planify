package com.chilltime.planifyapi.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JsonIdentityReference(alwaysAsId = true)  // This will serialize client as its ID
    private Client client;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "calendar_event",
            joinColumns = @JoinColumn(name = "calendar_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> events;

    @OneToMany(mappedBy = "calendar")
    @JsonIgnore  // Ignore this field during serialization
    private Set<CalendarCode> codes;

    // Add this to have a proper codigo property in JSON
    @Transient
    @JsonProperty("codigo")
    public String getCodigo() {
        if (codes == null || codes.isEmpty()) {
            return null;
        }
        // Return the first active code or null
        return codes.stream()
                .filter(code -> code.isUsed())
                .map(CalendarCode::getCode)
                .findFirst()
                .orElse(null);
    }
}