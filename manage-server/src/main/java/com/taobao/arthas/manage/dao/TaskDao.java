package com.taobao.arthas.manage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.arthas.manage.dao.domain.TaskDo;

/**
 * 任务
 * @author zhaojindong
 *
 */
@Repository
public interface TaskDao extends JpaRepository<TaskDo, Long> {
	
	/**
	 * 根据app client id 和status查询任务list
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 16:26:52
	 */
	@Query("select t from TaskDo t where appClientId = :appClientId and status = :status")
	List<TaskDo> getByAppIdAndStatus(@Param("appClientId") Long appClientId, @Param("status") Integer status);
	
	/**
	 * 更新任务的状态
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 16:30:16
	 */
	@Transactional
	@Modifying
	@Query("update TaskDo set status = :status where id = :id")
	int updateStatus( @Param("id") Long id, @Param("status")  Integer status);
	
	/**
	 * 更新任务的执行结果字段
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 16:30:16
	 */
	@Transactional
	@Modifying
	@Query("update TaskDo set cmdResult = :cmdResult where id = :id")
	int updateCmdResult( @Param("id") Long id, @Param("cmdResult")  String cmdResult);

}
