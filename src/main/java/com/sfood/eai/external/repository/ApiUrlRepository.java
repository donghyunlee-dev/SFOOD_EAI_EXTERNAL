package com.sfood.eai.external.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.ApiUrl;
import com.sfood.eai.external.entity.id.ApiUrlId;

@Repository
public interface ApiUrlRepository extends JpaRepository <ApiUrl, ApiUrlId> {

}
