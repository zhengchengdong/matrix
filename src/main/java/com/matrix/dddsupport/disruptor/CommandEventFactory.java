package com.matrix.dddsupport.disruptor;

import com.lmax.disruptor.EventFactory;
import com.matrix.dddsupport.command.CommandEvent;

public class CommandEventFactory implements EventFactory<CommandEvent> {

	@Override
	public CommandEvent newInstance() {
		return new CommandEvent();
	}

}
