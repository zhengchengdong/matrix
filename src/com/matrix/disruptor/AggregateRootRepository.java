package com.matrix.disruptor;

import java.util.HashMap;
import java.util.Map;

public class AggregateRootRepository {

	private Map<String, Object> aggregateRoots = new HashMap<>();

	public <T> T getAggregateRoot(String name) {
		return (T) aggregateRoots.get(name);
	}

	public Map<String, Object> getAggregateRoots() {
		return aggregateRoots;
	}

	public Snapshot creatSnapshot() {
		Snapshot snapshot = new Snapshot();
		snapshot.setCreateTime(System.currentTimeMillis());
		snapshot.getContentMap().putAll(aggregateRoots);
		return snapshot;
	}

}
