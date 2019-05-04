package com.matrix.util;

import java.nio.ByteBuffer;

/**
 * 可反复重用的ByteBuffer。省去申请空间的开销。非线程安全。
 * 
 * @author zheng chengdong
 *
 */
public class ReuseByteBuffer {

	private ByteBuffer byteBuffer;

	public ReuseByteBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	public ByteBuffer take() {
		byteBuffer.clear();
		return byteBuffer;
	}

}
