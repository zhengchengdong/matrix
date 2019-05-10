package com.matrix.dddsupport.disruptor;

import com.lmax.disruptor.EventHandler;
import com.matrix.dddsupport.aggregate.AggregateRootRepository;
import com.matrix.dddsupport.command.CommandEvent;
import com.matrix.dddsupport.execute.CommandEventHandler;

public class DisruptorCommandEventHandler implements EventHandler<CommandEvent> {
	private CommandEventHandler commandEventHandler;

	public DisruptorCommandEventHandler(AggregateRootRepository aggregateRootRepository, String snapshotFileBasePath,
			String jFileBasePath) {
		this.commandEventHandler = new CommandEventHandler(aggregateRootRepository, snapshotFileBasePath,
				jFileBasePath);
	}

	@Override
	public void onEvent(CommandEvent event, long sequence, boolean endOfBatch) throws Exception {
		commandEventHandler.onEvent(event);
	}

}
