package com.taobao.arthas.manage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.taobao.arthas.manage.dao.domain.AppConnectionDo;

public interface AppConnectionDao extends JpaRepository<AppConnectionDo, Long> {

}
