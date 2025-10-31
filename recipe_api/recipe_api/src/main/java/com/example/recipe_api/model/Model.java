package com.example.recipe_api.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "menu")
@Data
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuisine;
    private String title;
    private Double rating;
    private Integer prep_time;
    private Integer cook_time;
    private Integer total_time;

    @Column(length = 2000)
    private String description;

    // ✅ Keep JSON as actual JSON when returning response
    @JsonRawValue
    @Column(columnDefinition = "json")
    private String nutrients;

    private String serves;
}
