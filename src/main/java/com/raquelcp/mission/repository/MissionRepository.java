package com.raquelcp.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raquelcp.mission.persistence.entity.Mission;
import com.raquelcp.mission.persistence.entity.People;

public interface MissionRepository extends JpaRepository<Mission, Long>{
    public List<Mission> findAllByCaptains(List<People> captains);
}
