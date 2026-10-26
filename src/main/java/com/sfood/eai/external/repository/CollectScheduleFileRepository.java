package com.sfood.eai.external.repository;


import com.sfood.eai.external.entity.CollectSchedule;
import com.sfood.eai.external.entity.id.CollectScheduleId;

import java.util.List;
import java.util.Optional;

public interface CollectScheduleFileRepository {
    List<CollectSchedule> findAll();
    Optional<CollectSchedule> findById(CollectScheduleId id);
}
