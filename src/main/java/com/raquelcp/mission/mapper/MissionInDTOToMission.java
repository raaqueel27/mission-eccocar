package com.raquelcp.mission.mapper;

import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.raquelcp.mission.persistence.entity.Mission;
import com.raquelcp.mission.persistence.entity.People;
import com.raquelcp.mission.persistence.entity.Planets;
import com.raquelcp.mission.persistence.entity.Starship;
import com.raquelcp.mission.service.dto.MissionInDTO;

@Component
public class MissionInDTOToMission implements IMapper<MissionInDTO, Mission>{
    
    @Override
    public Mission map(MissionInDTO in) {
        Mission mission = new Mission();
        RestTemplate restTemplate = new RestTemplate();
        
        mission.setStartDate(in.getStartDate());
        mission.setCrew(in.getCrew());
        System.out.println("Fecha de inicio y Tripulacion iniciadas");

        ArrayList<Planets> planets = new ArrayList<Planets>();
        ArrayList<People> captains = new ArrayList<People>();
        Starship starship = new Starship();

        if(!in.getPlanets().isEmpty()) {
            for(String planetUrl: in.getPlanets()) {
                var response = restTemplate.getForEntity(planetUrl, Planets.class);
                if(response.getStatusCode() == HttpStatus.OK) {
                    planets.add(response.getBody());
                }
            }
        }
        mission.setPlanets(planets);
        System.out.println("Planetas iniciados");

        if(!in.getCaptains().isEmpty()) {
            for(String captainUrl: in.getCaptains()) {
                var response = restTemplate.getForEntity(captainUrl, People.class);
                if(response.getStatusCode() == HttpStatus.OK) {
                    captains.add(response.getBody());
                }
            }
        }
        mission.setCaptains(captains);
        System.out.println("Capitanes iniciados");

        if(in.getStarship() != null) {
            var response = restTemplate.getForEntity(in.getStarship(), Starship.class);
            
            if(response.getStatusCode() == HttpStatus.OK) {
                starship = response.getBody();
            }
        }
        mission.setStarship(starship);
        System.out.println("Nave iniciada");

        
        //Mission duration calculation
        double totalDiameter = 0;
        int totalMinutes = 0;
        for(Planets planet: planets) {
            totalDiameter += planet.getDiameter();
        }
        double traveled = 0;
        int numCrew = mission.getCrew();
        int numCaptains = mission.getCaptains().size();
        while(traveled < totalDiameter) {
            traveled += numCrew*10;
            traveled += numCaptains*100;
            totalMinutes += 60;
        }
        int totalHours = totalMinutes/60;
        if(totalMinutes%60 == 0) {
            totalHours++;
        }

        mission.setDuration(totalHours);
        System.out.println("Duración iniciada");

        //End Date Calculation
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mission.getStartDate());
        calendar.add(Calendar.HOUR_OF_DAY, totalHours);

        mission.setEndDate(calendar.getTime());
        System.out.println("Fecha de fin niciada");

        return mission;
    }
}
