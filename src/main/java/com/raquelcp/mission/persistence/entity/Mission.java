package com.raquelcp.mission.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startDate;

    private Date endDate;

    private Integer duration;

    @JoinColumn(name = "starship")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Starship starship;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(
            name = "captains",
            joinColumns = {@JoinColumn(name = "mission_id")},
            inverseJoinColumns = {@JoinColumn(name = "people_url")}
    )
    private List<People> captains;

    private Integer crew;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(
            name = "mission_planets",
            joinColumns = {@JoinColumn(name = "mission_id")},
            inverseJoinColumns = {@JoinColumn(name = "planet_url")}
    )
    private List<Planets> planets;

    @Override
    public String toString() {
        return "Mission{" +
        "id=" + id +
        ", startDate='" + startDate + '\'' +
        ", endDate=" + endDate +
        ", duration=" + duration +
        '}';
    }   
    
}
