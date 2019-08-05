package com.taobao.arthas.manage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.arthas.manage.dao.domain.AppClientDo;

@Repository
public interface AppClientDao extends JpaRepository<AppClientDo, Long> {
	@Transactional
	@Modifying
	@Query("update AppClientDo set isAttached = :isAttached where id = :id")
	int updateAttachStatus( @Param("id") Long id, @Param("isAttached") Boolean isAttached);
}
