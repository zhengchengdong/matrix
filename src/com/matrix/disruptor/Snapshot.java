package com.matrix.disruptor;

import java.util.HashMap;
import java.util.Map;

public class Snapshot {
	private long createTime;
	private Map<String, Object> contentMap = new HashMap<>();

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Map<String, Object> getContentMap() {
		return contentMap;
	}

	public void setContentMap(Map<String, Object> contentMap) {
		this.contentMap = contentMap;
	}

}
