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

    private Boolean isValid(Mission mission) {
        if(mission.getStartDate() == null) {
            return false;
        }
        if(mission.getStarship().getUrl() == null) {
            return false;
        }
        if(mission.getCaptains().isEmpty()) {
            return false;
        }
        if(mission.getPlanets().isEmpty()) {
            return false;
        }
        if(mission.getCrew() < 0) {
            return false;
        }
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
                    return false;
                }
            }
        }

        int totalCrew = mission.getCaptains().size() + mission.getCrew();
        String starshipMinCrewStr = mission.getStarship().getCrew();
        if(starshipMinCrewStr == null) {
            return false;
        }
        if(totalCrew < Integer.parseInt(starshipMinCrewStr)) {
            return false;
        }
        if(totalCrew > (mission.getCrew() + Integer.parseInt(mission.getStarship().getPassengers()))) {
            return false;
        }

        List<People> captains = mission.getCaptains();
        List<Mission> allMissions = this.repository.findAll(); 
        Date startDate = mission.getStartDate();
        Date endDate = mission.getEndDate();
        for(Mission ms: allMissions) {
            for(People captain: captains) {
                if(ms.getCaptains().contains(captain)) {
                    if(!(endDate.before(ms.getStartDate()) || startDate.after(ms.getEndDate()))) {
                        return false;
                    }
                    if(startDate == ms.getStartDate() && endDate == ms.getEndDate()) {
                        return false;
                    }
                }
            }
        }
        System.out.println("El captián no está ocupado");
        return true;
    }
}
