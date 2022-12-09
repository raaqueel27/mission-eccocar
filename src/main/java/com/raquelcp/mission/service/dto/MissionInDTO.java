package com.raquelcp.mission.service.dto;

import java.util.ArrayList;
import java.util.Date;

import lombok.Data;

@Data
public class MissionInDTO {
    private Date startDate;
    private String starship;
    private ArrayList<String> captains;
    private Integer crew;
    private ArrayList<String> planets;
}
