package com.raquelcp.mission.persistence.entity;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date startDate;
    private Date endDate;
    private Integer duration;
    private Starship starship;
    @ElementCollection(targetClass=People.class)
    private ArrayList<People> captains;
    private Integer crew;
    @ElementCollection(targetClass=Planets.class)
    private ArrayList<Planets> planets;
    
}
