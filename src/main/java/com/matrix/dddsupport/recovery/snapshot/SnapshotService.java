package com.matrix.dddsupport.recovery.snapshot;

import com.lmax.disruptor.dsl.Disruptor;
import com.matrix.dddsupport.command.CommandEvent;
import com.matrix.dddsupport.disruptor.DisruptorRepository;
import com.matrix.dddsupport.execute.DeferredResult;

public class SnapshotService {
	private DisruptorRepository disruptorRepository;

	public SnapshotService(DisruptorRepository disruptorRepository) {
		this.disruptorRepository = disruptorRepository;
	}

	public void saveSnapshot(String disruptorName) throws Exception {
		DeferredResult deferredResult = new DeferredResult();
		Disruptor<CommandEvent> disruptor = disruptorRepository.getDisruptor(disruptorName);
		disruptor.publishEvent((event, sequence) -> {
			event.setResult(deferredResult);
			event.setSnapshot(true);
		});
		deferredResult.getResult();
	}

}
