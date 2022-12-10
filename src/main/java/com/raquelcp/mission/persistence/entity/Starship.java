package com.raquelcp.mission.persistence.entity;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Starship {
    
    @Id
    private String url;
    private String name;
    private String crew;
    @ElementCollection(targetClass = People.class)
    private List<People> pasajeros;
    
}
