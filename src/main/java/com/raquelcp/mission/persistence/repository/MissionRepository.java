package com.raquelcp.mission.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raquelcp.mission.persistence.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long>{
    
}
