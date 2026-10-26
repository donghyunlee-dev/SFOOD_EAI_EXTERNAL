package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.CollectSchedule;
import com.sfood.eai.external.entity.id.CollectScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectScheduleRepository extends JpaRepository<CollectSchedule, CollectScheduleId> {
    @Query("select a from CollectSchedule a where a.collectType != '11'")
    List<CollectSchedule> findAll();
}
