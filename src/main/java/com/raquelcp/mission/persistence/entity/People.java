package com.raquelcp.mission.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raquelcp.mission.util.CustomMissionSerializer;

import lombok.Data;

@Data
@Entity
public class People {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String name;
    
    @ManyToMany(mappedBy = "captains")
    @JsonSerialize(using = CustomMissionSerializer.class)
    private Set<Mission> missions = new HashSet<Mission>();

}
