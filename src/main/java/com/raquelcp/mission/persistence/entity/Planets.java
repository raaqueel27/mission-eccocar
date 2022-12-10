package com.raquelcp.mission.persistence.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Data
@Entity
public class Planets {

    @Id
    private String url;
    private String name;
    private Double diameter;

    @ManyToMany(mappedBy = "planets")
    private Set<Mission> missions;
    
}
