package com.taobao.arthas.common.dto;

import java.util.List;

public class HeartBeatRespDto extends ManageBaseDto{
	private List<ManageTaskDto> taskDtoList;

	public List<ManageTaskDto> getTaskDtoList() {
		return taskDtoList;
	}

	public void setTaskDtoList(List<ManageTaskDto> taskDtoList) {
		this.taskDtoList = taskDtoList;
	}

}
