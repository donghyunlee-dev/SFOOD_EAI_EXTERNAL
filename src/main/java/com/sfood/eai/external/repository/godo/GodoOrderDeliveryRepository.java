package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoOrderDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GodoOrderDeliveryRepository extends JpaRepository <GodoOrderDelivery, String> {
    List<GodoOrderDelivery> findByOrderNo(String orderNo);
}
