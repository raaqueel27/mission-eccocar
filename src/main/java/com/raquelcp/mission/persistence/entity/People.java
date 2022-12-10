package com.raquelcp.mission.persistence.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class People {
    
    @Id
    private String url;
    private String name;
    
    @ManyToMany(mappedBy = "captains")
    private Set<Mission> missions;
}
