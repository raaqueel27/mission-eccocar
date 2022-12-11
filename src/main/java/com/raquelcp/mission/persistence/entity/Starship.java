package com.raquelcp.mission.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Starship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String name;
    private String crew;
    private String passengers;
    @ElementCollection
    private List<String> pilots;
    @OneToOne(mappedBy = "starship", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Mission mission;
    
}
