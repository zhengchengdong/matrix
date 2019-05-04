package com.matrix.disruptor;

import java.util.concurrent.CountDownLatch;

public class DeferredResult<T> {
	private final CountDownLatch latch = new CountDownLatch(1);

	private T result;

	private Exception e;

	public T getResult() throws Exception {
		latch.await();
		if (e != null) {
			throw e;
		}
		return result;
	}

	public void setResult(T result) {
		this.result = result;
		latch.countDown();
	}

	public void setException(Exception e) {
		this.e = e;
		latch.countDown();
	}

}
