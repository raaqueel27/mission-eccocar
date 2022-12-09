package com.raquelcp.mission.persistence.entity;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Starship {

    private String url;
    private String name;
    private Integer crew;
    private ArrayList<String> pasajeros;
    
}
