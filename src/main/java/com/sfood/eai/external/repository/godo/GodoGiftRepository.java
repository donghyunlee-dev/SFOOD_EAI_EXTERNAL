package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoGift;
import com.sfood.eai.external.entity.id.godo.GodoGiftId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GodoGiftRepository extends JpaRepository<GodoGift, GodoGiftId> {
    List<GodoGift> findByOrderNo(String orderNo);
}
