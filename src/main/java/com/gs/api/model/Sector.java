package com.gs.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sectors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @OneToOne(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private Sensor sensor;

    @OneToMany(mappedBy = "sector")
    private List<Notification> notifications = new ArrayList<>();
}
