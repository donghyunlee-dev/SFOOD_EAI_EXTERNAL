package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GodoOrderInfoRepository extends JpaRepository <GodoOrderInfo, String> {
    List<GodoOrderInfo> findByOrderNo(String orderNo);
}
