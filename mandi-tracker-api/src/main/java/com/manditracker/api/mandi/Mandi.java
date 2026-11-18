
package com.manditracker.api.mandi;

import jakarta.persistence.*;

@Entity
@Table(name = "mandis")
public class Mandi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "name_mr", length = 150)
    private String nameMr;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(name = "district_mr", length = 100)
    private String districtMr;

    @Column(nullable = false, length = 100)
    private String state = "Maharashtra";

    @Column(name = "market_code", length = 100)
    private String marketCode;

    @Column(nullable = false)
    private boolean active = true;

    public Mandi() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameMr() { return nameMr; }
    public void setNameMr(String nameMr) { this.nameMr = nameMr; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictMr() { return districtMr; }
    public void setDistrictMr(String districtMr) {
        this.districtMr = districtMr;
    }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getMarketCode() { return marketCode; }
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
    }
}
