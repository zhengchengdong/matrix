package com.matrix.util;

import java.nio.ByteBuffer;

public class ByteArrayBuffer {

	private static sun.misc.Unsafe unsafe = Unsafe.theUnsafe;

	private byte[] innerByteArray;

	private int position;

	private int limit;

	private int mark;

	public static ByteArrayBuffer fromByteBuffer(ByteBuffer byteBuffer) {
		int size = byteBuffer.remaining();
		ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(size);
		byteBuffer.get(byteArrayBuffer.innerByteArray);
		return byteArrayBuffer;
	}

	public ByteArrayBuffer(int size) {
		innerByteArray = new byte[size];
		limit = size;
	}

	public ByteArrayBuffer(byte[] innerByteArray) {
		this.innerByteArray = innerByteArray;
	}

	public boolean hasRemaining() {
		return position < limit;
	}

	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(innerByteArray, 0, position);
	}

	public void putInt(int value) {
		unsafe.putInt(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position += 4;
	}

	public void putCharArray(char[] charArray) {
		int bytes = (charArray.length << 1);
		unsafe.copyMemory(charArray, ((long) sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET), innerByteArray,
				((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, bytes);
		position += bytes;
	}

	public void putByte(byte value) {
		unsafe.putByte(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position++;
	}

	public void putChar(char value) {
		unsafe.putChar(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position += 2;
	}

	public void putDouble(double value) {
		unsafe.putDouble(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position += 8;
	}

	public void putFloat(float value) {
		unsafe.putFloat(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position += 4;
	}

	public void putLong(long value) {
		unsafe.putLong(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position += 8;
	}

	public void putShort(short value) {
		unsafe.putShort(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, value);
		position += 2;
	}

	public byte[] getInnerByteArray() {
		return innerByteArray;
	}

	public int getPosition() {
		return position;
	}

	public int getInt() {
		int value = unsafe.getInt(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position += 4;
		return value;
	}

	public void getCharArray(char[] charArray) {
		unsafe.copyMemory(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, charArray,
				((long) sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET), (charArray.length << 1));
		position += (charArray.length << 1);
	}

	public void getIntArray(int[] intArray) {
		unsafe.copyMemory(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, intArray,
				((long) sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET), (intArray.length << 2));
		position += (intArray.length << 2);
	}

	public byte getByte() {
		byte value = unsafe.getByte(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position++;
		return value;
	}

	public void putIntArray(int[] intArray) {
		int bytes = (intArray.length << 2);
		unsafe.copyMemory(intArray, ((long) sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET), innerByteArray,
				((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position, bytes);
		position += bytes;
	}

	public char getChar() {
		char value = unsafe.getChar(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position += 2;
		return value;
	}

	public double getDouble() {
		double value = unsafe.getDouble(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position += 8;
		return value;
	}

	public float getFloat() {
		float value = unsafe.getFloat(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position += 4;
		return value;
	}

	public long getLong() {
		long value = unsafe.getLong(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position += 8;
		return value;
	}

	public short getShort() {
		short value = unsafe.getShort(innerByteArray, ((long) sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + position);
		position += 2;
		return value;
	}

	public void reset() {
		position = 0;
		mark = -1;
	}

	public void markAndSkip(int bytesToSkip) {
		mark = position;
		position += bytesToSkip;
	}

	public void putIntAtMark(int value) {
		unsafe.putInt(innerByteArray, ((long) (sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET) + mark), value);
	}

}
