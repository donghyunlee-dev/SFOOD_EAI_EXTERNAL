package com.sfood.eai.external.repository.sabang;

import com.sfood.eai.external.entity.id.sabang.SabangOrderId;
import com.sfood.eai.external.entity.sabang.SabangOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SabangOrderRepository extends JpaRepository <SabangOrder, SabangOrderId> {
    List<SabangOrder> findAllByOrderId(String id);
}
