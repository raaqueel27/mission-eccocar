package com.raquelcp.mission.service;

import org.springframework.stereotype.Service;

import com.raquelcp.mission.mapper.MissionInDTOToMission;
import com.raquelcp.mission.persistence.entity.Mission;
import com.raquelcp.mission.persistence.repository.MissionRepository;
import com.raquelcp.mission.service.dto.MissionInDTO;

@Service
public class MissionService {

    private final MissionRepository repository;
    private final MissionInDTOToMission missionMapper;

    public MissionService(MissionRepository repository, MissionInDTOToMission missionMapper) {
        this.repository = repository;
        this.missionMapper = missionMapper;
    }
    
    public Mission createMission(MissionInDTO missionInDTO) {
        Mission mission = missionMapper.map(missionInDTO);
        return this.repository.save(mission);    
    }
}
