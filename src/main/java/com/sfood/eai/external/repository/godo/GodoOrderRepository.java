package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoOrder;
import com.sfood.eai.external.entity.id.godo.GodoOrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GodoOrderRepository extends JpaRepository <GodoOrder, GodoOrderId>, GodoOrderRepositoryCustom {

}
