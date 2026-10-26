package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.GoodsEdiPartner;
import com.sfood.eai.external.entity.id.goods.GoodsEdiPartnerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsEdiPartnerRepository extends JpaRepository<GoodsEdiPartner, GoodsEdiPartnerId> {
    Optional<GoodsEdiPartner> findByCompanyCodeAndMallCodeAndPartnerGoodsCodeAndExchangeFigure(String companyCode, String mallCode, String partnerGoodsCode, int exchangeFigure );
}
