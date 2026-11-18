
package com.manditracker.api.crop;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "name_mr", nullable = false, length = 100)
    private String nameMr;

    @Column(length = 60)
    private String category;

    @Column(nullable = false, length = 20)
    private String unit = "QUINTAL";

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getNameMr() { return nameMr; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public boolean isActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setNameMr(String nameMr) { this.nameMr = nameMr; }
    public void setCategory(String category) { this.category = category; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setActive(boolean active) { this.active = active; }
}
