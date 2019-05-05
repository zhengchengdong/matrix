package com.matrix.disruptor;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class DisruptorRepository {

	public void createDisruptor(String disruptorName, AggregateRootRepository aggregateRootRepository,
			String snapshotFileBasePath, String jFileBasePath) {
		Disruptor<CommandEvent> disruptor = new Disruptor<>(new CommandEventFactory(), 1024 * 1024,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new SleepingWaitStrategy());
		disruptor.handleEventsWith(new CommandEventHandler());
		put(disruptorName, disruptor);
		disruptor.start();
	}

	public Disruptor<CommandEvent> getDisruptor(String disruptorName) {
		// TODO Auto-generated method stub
		return null;
	}

}
