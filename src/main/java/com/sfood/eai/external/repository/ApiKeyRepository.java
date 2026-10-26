package com.sfood.eai.external.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.ApiKey;
import com.sfood.eai.external.entity.id.ApiKeyId;

@Repository
public interface ApiKeyRepository extends JpaRepository <ApiKey, ApiKeyId> {

	List<ApiKey> findByCompanyCodeAndMallCode(String companyCode, String mallCode);

}
