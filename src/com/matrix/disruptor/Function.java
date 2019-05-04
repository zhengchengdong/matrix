package com.matrix.disruptor;

public interface Function<V> {
	V run() throws Exception;
}
