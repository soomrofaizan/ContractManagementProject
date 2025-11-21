package com.contractor.ContractorApplication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "project_items")
public class ProjectItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double rent;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    // Constructors
    public ProjectItem() {}

    public ProjectItem(LocalDate date, String item, Double rate, Integer quantity,
                       Double rent, Double totalAmount) {
        this.date = date;
        this.item = item;
        this.rate = rate;
        this.quantity = quantity;
        this.rent = rent;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getRent() { return rent; }
    public void setRent(Double rent) { this.rent = rent; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}