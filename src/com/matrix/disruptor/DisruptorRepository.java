package com.matrix.disruptor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class DisruptorRepository {

	Map<String, Disruptor<CommandEvent>> nameDisruptorMap = new HashMap<>();

	public void createDisruptor(String disruptorName, AggregateRootRepository aggregateRootRepository,
			String snapshotFileBasePath, String jFileBasePath) {
		Disruptor<CommandEvent> disruptor = new Disruptor<>(new CommandEventFactory(), 1024 * 1024,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new SleepingWaitStrategy());
		disruptor.handleEventsWith(new CommandEventHandler(aggregateRootRepository,
				snapshotFileBasePath + File.separatorChar + disruptorName + File.separatorChar,
				jFileBasePath + File.separatorChar + disruptorName + File.separatorChar));
		disruptor.start();
		nameDisruptorMap.put(disruptorName, disruptor);
	}

	public Disruptor<CommandEvent> getDisruptor(String disruptorName) {
		return nameDisruptorMap.get(disruptorName);
	}

}
