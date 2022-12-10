package com.raquelcp.mission.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
            return this.repository.save(mission);  
        }
        return null;
    }

    private Boolean isValid(Mission mission) {
        if(mission.getStartDate() == null) {
            return false;
        }
        if(mission.getStarship() == null) {
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
        List<People> pilots = mission.getStarship().getPilots();
        boolean isPresent = false;
        if(!pilots.isEmpty() && pilots != null) {
            for(People pilot: pilots) {
                if(mission.getCaptains().contains(pilot)) {
                    isPresent = true;
                }
            }
        }
        if(!isPresent) {
            return false;
        }
        int totalCrew = mission.getCaptains().size() + mission.getCrew();
        int starshipMinCrew = Integer.parseInt(mission.getStarship().getCrew());
        if(totalCrew < starshipMinCrew) {
            return false;
        }
        if(totalCrew > (mission.getCrew() + Integer.parseInt(mission.getStarship().getCrew()))) {
            return false;
        }

        List<People> captains = mission.getCaptains();
        List<Mission> missionsOfCaptain = this.repository.findByCaptains(captains); 
        Date startDate = mission.getStartDate();
        Date endDate = mission.getEndDate();
        for(Mission ms: missionsOfCaptain) {
            if(startDate.before(ms.getStartDate()) && endDate.before(ms.getStartDate())) {
                return false;
            } else if(startDate.after(ms.getEndDate()) && endDate.before(ms.getEndDate())) {
                return false;
            }
        }
        return true;
    }
}
