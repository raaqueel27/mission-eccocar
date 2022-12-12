package com.raquelcp.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raquelcp.mission.persistence.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long>{
    public List<Mission> findAllByCaptains(String captains);
}
