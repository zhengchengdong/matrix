package com.matrix.disruptor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lmax.disruptor.dsl.Disruptor;
import com.matrix.util.Unsafe;

public abstract class DisruptorServiceBase {

	private DisruptorRepository disruptorRepository;
	private String serviceImplType;

	private Map<String, String> callTypeMethodNameMap = new ConcurrentHashMap<>();
	private Map<String, long[]> callTypeParametersAddressMap = new ConcurrentHashMap<>();

	protected DisruptorServiceBase(Object serviceImpl) {
		serviceImplType = serviceImpl.getClass().getName();
	}

	protected <T> T executeFunction(String disruptorName, Function<T> function) throws Exception {
		Command cmd = createCommand(function);
		DeferredResult<T> deferredResult = new DeferredResult<>();
		Disruptor<CommandEvent> disruptor = disruptorRepository.getDisruptor(disruptorName);
		disruptor.publishEvent((event, sequence) -> {
			event.setCmd(cmd);
			event.setFunction(function);
			event.setResult(deferredResult);
		});
		return deferredResult.getResult();
	}

	protected void executeRunnable(String disruptorName, Process process) throws Exception {
		Command cmd = createCommand(process);
		DeferredResult deferredResult = new DeferredResult();
		Disruptor<CommandEvent> disruptor = disruptorRepository.getDisruptor(disruptorName);
		disruptor.publishEvent((event, sequence) -> {
			event.setCmd(cmd);
			event.setProcess(process);
			event.setResult(deferredResult);
		});
		deferredResult.getResult();
	}

	private Command createCommand(Object call) throws Exception {
		Command cmd = new Command();
		cmd.setType(serviceImplType);

		String callType = call.getClass().getName();
		String methodName = callTypeMethodNameMap.get(callType);
		if (methodName == null) {
			methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			callTypeMethodNameMap.put(callType, methodName);
		}
		cmd.setMethod(methodName);

		long[] parametersAddressArray = callTypeParametersAddressMap.get(callType);
		if (parametersAddressArray == null) {
			Field[] fieldsInCall = call.getClass().getDeclaredFields();
			parametersAddressArray = new long[fieldsInCall.length - 1];
			for (int i = 1; i < fieldsInCall.length; i++) {
				parametersAddressArray[i - 1] = Unsafe.getFieldOffset(fieldsInCall[i]);
			}
			callTypeParametersAddressMap.put(callType, parametersAddressArray);
		}
		Object[] parameters = new Object[parametersAddressArray.length];
		for (int i = 0; i < parametersAddressArray.length; i++) {
			parameters[i] = Unsafe.getObjectFieldOfObject(call, parametersAddressArray[i]);
		}
		cmd.setParameters(parameters);
		return cmd;
	}

}
