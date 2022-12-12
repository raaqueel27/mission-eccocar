package com.raquelcp.mission.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raquelcp.mission.persistence.entity.Mission;
import com.raquelcp.mission.persistence.entity.People;
import com.raquelcp.mission.service.MissionService;
import com.raquelcp.mission.service.dto.MissionInDTO;

@RestController
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }
    
    @PostMapping
    public Mission createMission(@RequestBody MissionInDTO missionInDTO) {
        return this.missionService.createMission(missionInDTO);
    }

    @GetMapping
    public List<Mission> getAll() {
        return this.missionService.getAll();
    }

    @GetMapping("/captains/{captains}")
    public List<Mission> getAllByCaptains(@PathVariable String captains) {
        return this.missionService.getAllByCaptains(captains);
    }
}
