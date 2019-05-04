package com.matrix.disruptor;

public class SnapshotFactory {

	private AggregateRootRepository aggregateRootRepository;

	public Snapshot createSnapshoot() {
		Snapshot snapshot = new Snapshot();
		snapshot.setCreateTime(System.currentTimeMillis());
		snapshot.getContentMap().putAll(aggregateRootRepository.getAggregateRoots());
		return snapshot;
	}

}
