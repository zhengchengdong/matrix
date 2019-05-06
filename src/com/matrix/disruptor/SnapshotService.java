package com.matrix.disruptor;

import com.lmax.disruptor.dsl.Disruptor;

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
