package com.matrix.disruptor;

import java.io.File;

import com.matrix.util.JsonUtil;

public class AggregateRootRepositorySnapshotRecoverer {

	public static AggregateRootRepository recover(String snapshotFileBasePath, String disruptorName) throws Exception {
		AggregateRootRepository repository = new AggregateRootRepository();
		Snapshot snapshot = (Snapshot) JsonUtil.objRecoverFromRecentJsonFile(
				snapshotFileBasePath + File.separatorChar + disruptorName, Snapshot.class);
		if (snapshot != null) {
			repository.getAggregateRoots().putAll(snapshot.getContentMap());
		}
		return repository;
	}

}
