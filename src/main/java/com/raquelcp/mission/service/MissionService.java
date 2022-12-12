package com.raquelcp.mission.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            System.out.println("No se ha encontrado ninguna misión con esos capitanes");
        }
        return missions;
    }

    private Boolean isValid(Mission mission) {
        if(mission.getStartDate() == null) {
            System.out.println("No hay fecha de inicio.");
            return false;
        } 
        if(mission.getStarship().getUrl() == null) {
            System.out.println("No hay nave");
            return false;
        }
        if(mission.getCaptains().isEmpty()) {
            System.out.println("No hay capitanes");
            return false;
        }
        if(mission.getPlanets().isEmpty()) {
            System.out.println("No hay planetas");
            return false;
        }
        if(mission.getCrew() < 0) {
            System.out.println("Número de tripulación no válido");
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
                    System.out.println("El piloto de la nave no está presente");
                    return false;
                }
            }
        }

        int totalCrew = mission.getCaptains().size() + mission.getCrew();
        int starshipCrew = Integer.parseInt(mission.getStarship().getCrew());
        if(totalCrew < starshipCrew) {
            System.out.println("La tripulación total es menor que la mínima requerida en la nave.");
            return false;
        }
        if(totalCrew > (starshipCrew + Integer.parseInt(mission.getStarship().getPassengers()))) {
            System.out.println(totalCrew + " > " + starshipCrew + " + " + mission.getStarship().getPassengers());
            System.out.println("La tripulación total es mayor que la suma de los pasajeros de la nave y la tripulación de la misión.");
            return false;
        }

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
                        System.out.println("El capitán está ocupado en esas fechas");
                        return false;
                    }
                    if(startDate == ms.getStartDate() && endDate == ms.getEndDate()) {
                        System.out.println("El capitán está ocupado en esas fechas");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
