package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.OmsServerInfo;
import com.sfood.eai.external.entity.id.OmsServerInfoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OmsServerInfoRepository extends JpaRepository<OmsServerInfo, OmsServerInfoId> {

    @Query("select a from OmsServerInfo a where a.id.applicationName = :appName and a.useYn='Y' order by a.id.priority")
    List<OmsServerInfo> findByNameInUse(@Param("appName") String appName);
}
