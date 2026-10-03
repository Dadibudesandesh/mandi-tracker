
package com.manditracker.api.price;

import com.manditracker.api.crop.Crop;
import com.manditracker.api.mandi.Mandi;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mandi_prices")
public class MandiPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mandi_id", nullable = false)
    private Mandi mandi;

    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @Column(name = "min_price", precision = 12, scale = 2)
    private BigDecimal minPrice;

    @Column(name = "max_price", precision = 12, scale = 2)
    private BigDecimal maxPrice;

    @Column(name = "modal_price", precision = 12, scale = 2)
    private BigDecimal modalPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal arrivals;

    @Column(nullable = false, length = 30)
    private String unit = "QUINTAL";

    @Column(length = 100)
    private String source;

    @Column(name = "source_record_id", length = 150)
    private String sourceRecordId;

    @Column(name = "imported_at", nullable = false)
    private LocalDateTime importedAt;

    public MandiPrice() {
    }

    @PrePersist
    protected void onCreate() {
        if (importedAt == null) {
            importedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Crop getCrop() { return crop; }
    public void setCrop(Crop crop) { this.crop = crop; }

    public Mandi getMandi() { return mandi; }
    public void setMandi(Mandi mandi) { this.mandi = mandi; }

    public LocalDate getPriceDate() { return priceDate; }
    public void setPriceDate(LocalDate priceDate) {
        this.priceDate = priceDate;
    }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getModalPrice() { return modalPrice; }
    public void setModalPrice(BigDecimal modalPrice) {
        this.modalPrice = modalPrice;
    }

    public BigDecimal getArrivals() { return arrivals; }
    public void setArrivals(BigDecimal arrivals) {
        this.arrivals = arrivals;
    }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getSourceRecordId() { return sourceRecordId; }
    public void setSourceRecordId(String sourceRecordId) {
        this.sourceRecordId = sourceRecordId;
    }

    public LocalDateTime getImportedAt() { return importedAt; }
    public void setImportedAt(LocalDateTime importedAt) {
        this.importedAt = importedAt;
    }
}
