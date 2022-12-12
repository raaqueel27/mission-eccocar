package com.raquelcp.mission.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.raquelcp.mission.mapper.MissionInDTOToMission;
import com.raquelcp.mission.persistence.entity.Mission;
import com.raquelcp.mission.persistence.entity.People;
import com.raquelcp.mission.repository.MissionRepository;
import com.raquelcp.mission.service.dto.MissionInDTO;

@Service
@Component
public class MissionService {

    private final MissionRepository repository;
    private final MissionInDTOToMission missionMapper;

    public MissionService(MissionRepository repository, MissionInDTOToMission missionMapper) {
        this.repository = repository;
        this.missionMapper = missionMapper;
    }
    
    public Mission createMission(MissionInDTO missionInDTO) {
        Mission mission = missionMapper.map(missionInDTO);
        if(isValid(mission)) {
            return this.repository.saveAndFlush(mission);  
        }
        return null;
    }

    public List<Mission> getAll() {
        return this.repository.findAll();
    }

    public List<Mission> getAllByCaptains(String captainsId) {
        //Checks if the missions have the captains indicated
        String[] captainsIdArray = captainsId.split(", ");
        List<Mission> allMissions = this.repository.findAll();
        List<Mission> missions = new ArrayList<Mission>();
        for(Mission ms: allMissions) {
            List<People> captains = ms.getCaptains();
            for(People captain: captains) {
                String captainId = String.valueOf(captain.getId());
                for(int i = 0; i < captainsIdArray.length; i++) {
                    if(captainId.equals(captainsIdArray[i])) {
                        missions.add(ms);
                    }
                }
            }
        }
        if(missions != null) {
            System.out.println("No mission found with those captains");
        }
        return missions;
    }

    private Boolean isValid(Mission mission) {

        //Checks if the mission has and Start Date
        if(mission.getStartDate() == null) {
            System.out.println("No start date.");
            return false;
        } 

        //Checks if the mission has an starship
        if(mission.getStarship().getUrl() == null) {
            System.out.println("No starship");
            return false;
        }

        //Check if the mission has at leats one captain
        if(mission.getCaptains().isEmpty()) {
            System.out.println("No captains");
            return false;
        }

        //Checks if the mission has at least one planet
        if(mission.getPlanets().isEmpty()) {
            System.out.println("No planets");
            return false;
        }

        //Checks if the crew is a valid number
        if(mission.getCrew() < 0) {
            System.out.println("Invalid crew number");
            return false;
        }

        //Checks if the starship pilots are present as captains (In case the starship has pilots)
        List<String> pilotsUrl = mission.getStarship().getPilots();
        boolean isPresent = false;
        RestTemplate response = new RestTemplate();
        ArrayList<People> pilots = new ArrayList<People>();
        if(pilotsUrl != null) {
            if(!pilotsUrl.isEmpty()) {
                for(String pilotUrl: pilotsUrl) {
                    pilots.add(response.getForEntity(pilotUrl, People.class).getBody());
                }
                if(!pilots.isEmpty() && pilots != null) {
                    for(People p: pilots) {
                        if(mission.getCaptains().contains(p)) {
                            isPresent = true;
                        }
                    }
                }
                if(!isPresent) {
                    System.out.println("The pilot of the ship is not present");
                    return false;
                }
            }
        }

        //Checks if captains + crew is greatter than the starship crew
        int totalCrew = mission.getCaptains().size() + mission.getCrew();
        int starshipCrew = Integer.parseInt(mission.getStarship().getCrew());
        if(totalCrew < starshipCrew) {
            System.out.println("The total crew is less than the minimum required on the ship");
            return false;
        }

        //Checks if captains + crew is smaller than the total crew of the starship
        if(totalCrew > (starshipCrew + Integer.parseInt(mission.getStarship().getPassengers()))) {
            System.out.println(totalCrew + " > " + starshipCrew + " + " + mission.getStarship().getPassengers());
            System.out.println("The total crew is greater than the sum of the ship's passengers and the mission crew");
            return false;
        }

        //Checks if the captain is busy at the new mission hours 
        List<People> captains = mission.getCaptains();
        ArrayList<String> captainsUrl = new ArrayList<String>();
        for(People captain: captains)  {
            captainsUrl.add(captain.getUrl());
        }
        List<Mission> allMissions = this.repository.findAll(); 
        Date startDate = mission.getStartDate();
        Date endDate = mission.getEndDate();
        for(Mission ms: allMissions) {
            for(People captain: captains) {
                String captainUrl = captain.getUrl();
                if(captainsUrl.contains(captainUrl)) {
                    if(!(endDate.before(ms.getStartDate()) || startDate.after(ms.getEndDate()))) {
                        System.out.println("The captain is busy on those dates");
                        return false;
                    }
                    if(startDate == ms.getStartDate() && endDate == ms.getEndDate()) {
                        System.out.println("The captain is busy on those dates");
                        return false;
                    }
                }
            }
        }

        return true; //It's a valid mission
    }
}
