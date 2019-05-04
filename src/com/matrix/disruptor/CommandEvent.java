package com.matrix.disruptor;

public class CommandEvent {

	private Command cmd;

	private Function function;

	private Process process;

	private DeferredResult result;

	private boolean snapshot = false;

	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public DeferredResult getResult() {
		return result;
	}

	public void setResult(DeferredResult result) {
		this.result = result;
	}

	public boolean isSnapshot() {
		return snapshot;
	}

	public void setSnapshot(boolean snapshot) {
		this.snapshot = snapshot;
	}

}
