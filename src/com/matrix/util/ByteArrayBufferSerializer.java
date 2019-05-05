package com.matrix.util;

public class ByteArrayBufferSerializer {

	public static void notNullStringToBuffer(String str, ByteArrayBuffer buffer) {
		char[] charArray = Unsafe.getValueOfString(str);
		buffer.putInt(charArray.length);
		buffer.putCharArray(charArray);
	}

	public static String notNullStringFromBuffer(ByteArrayBuffer buffer) {

		int charArrayLength = buffer.getInt();
		char[] charArray = new char[charArrayLength];
		buffer.getCharArray(charArray);
		return new String(charArray);
	}

	/**
	 * 第一个字节标记是不是null
	 */
	public static void stringToBuffer(String str, ByteArrayBuffer buffer) {
		if (str != null) {
			buffer.putByte((byte) 1);
			char[] charArray = Unsafe.getValueOfString(str);
			buffer.putInt(charArray.length);
			buffer.putCharArray(charArray);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfStringToBuffer(String str) {
		int size = 1;
		if (str != null) {
			size += 4;
			char[] charArray = Unsafe.getValueOfString(str);
			size += (charArray.length << 1);
		}
		return size;
	}

	public static int sizeOfNotNullStringToBuffer(String str) {
		int size = 4;
		char[] charArray = Unsafe.getValueOfString(str);
		size += (charArray.length << 1);
		return size;
	}

	public static String stringFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			int charArrayLength = buffer.getInt();
			char[] charArray = new char[charArrayLength];
			buffer.getCharArray(charArray);
			return new String(charArray);
		} else {
			return null;
		}
	}

	/**
	 * 认为数组很短，只用一个字节保存数组长度
	 */
	public static void shortIntArrayToBuffer(int[] array, ByteArrayBuffer buffer) {
		if (array != null) {
			buffer.putByte((byte) 1);
			byte length = (byte) array.length;
			buffer.putByte(length);
			buffer.putIntArray(array);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfShortIntArrayToBuffer(int[] array) {
		int size = 1;
		if (array != null) {
			size++;
			size += (array.length << 2);
		}
		return size;
	}

	public static void notNullShortIntArrayToBuffer(int[] array, ByteArrayBuffer buffer) {
		byte length = (byte) array.length;
		buffer.putByte(length);
		buffer.putIntArray(array);
	}

	public static int sizeOfNotNullShortIntArrayToBuffer(int[] array) {
		int size = 1;
		size += (array.length << 2);
		return size;
	}

	public static int[] shortIntArrayFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			byte length = buffer.getByte();
			int[] array = new int[length];
			buffer.getIntArray(array);
			return array;
		} else {
			return null;
		}
	}

	public static int[] notNullShortIntArrayFromBuffer(ByteArrayBuffer buffer) {
		byte length = buffer.getByte();
		int[] array = new int[length];
		buffer.getIntArray(array);
		return array;
	}

	public static boolean booleanFromBuffer(ByteArrayBuffer buffer) {
		return buffer.getByte() == 1;
	}

	public static void booleanToBuffer(boolean value, ByteArrayBuffer buffer) {
		if (value) {
			buffer.putByte((byte) 1);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static Boolean boxingBooleanFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getByte() == 1;
		} else {
			return null;
		}
	}

	public static Boolean notNullBoxingBooleanFromBuffer(ByteArrayBuffer buffer) {
		return buffer.getByte() == 1;
	}

	public static void boxingBooleanToBuffer(Boolean b, ByteArrayBuffer buffer) {
		if (b != null) {
			buffer.putByte((byte) 1);
			buffer.putByte((byte) (b ? 1 : 0));
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static void notNullBoxingBooleanToBuffer(Boolean b, ByteArrayBuffer buffer) {
		buffer.putByte((byte) (b ? 1 : 0));
	}

	public static int sizeOfBoxingBooleanToBuffer(Boolean b) {
		int size = 1;
		if (b != null) {
			size++;
		}
		return size;
	}

	public static Byte boxingByteFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getByte();
		} else {
			return null;
		}
	}

	public static void boxingByteToBuffer(Byte b, ByteArrayBuffer buffer) {
		if (b != null) {
			buffer.putByte((byte) 1);
			buffer.putByte(b);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingByteToBuffer(Byte b) {
		int size = 1;
		if (b != null) {
			size++;
		}
		return size;
	}

	public static Character boxingCharacterFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getChar();
		} else {
			return null;
		}
	}

	public static void boxingCharacterToBuffer(Character c, ByteArrayBuffer buffer) {
		if (c != null) {
			buffer.putByte((byte) 1);
			buffer.putChar(c);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingCharacterToBuffer(Character c) {
		int size = 1;
		if (c != null) {
			size += 2;
		}
		return size;
	}

	public static Double boxingDoubleFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getDouble();
		} else {
			return null;
		}
	}

	public static void boxingDoubleToBuffer(Double d, ByteArrayBuffer buffer) {
		if (d != null) {
			buffer.putByte((byte) 1);
			buffer.putDouble(d);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingDoubleToBuffer(Double d) {
		int size = 1;
		if (d != null) {
			size += 8;
		}
		return size;
	}

	public static Float boxingFloatFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getFloat();
		} else {
			return null;
		}
	}

	public static void boxingFloatToBuffer(Float f, ByteArrayBuffer buffer) {
		if (f != null) {
			buffer.putByte((byte) 1);
			buffer.putFloat(f);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingFloatToBuffer(Float f) {
		int size = 1;
		if (f != null) {
			size += 4;
		}
		return size;
	}

	public static Integer boxingIntegerFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getInt();
		} else {
			return null;
		}
	}

	public static void boxingIntegerToBuffer(Integer i, ByteArrayBuffer buffer) {
		if (i != null) {
			buffer.putByte((byte) 1);
			buffer.putInt(i);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingIntegerToBuffer(Integer i) {
		int size = 1;
		if (i != null) {
			size += 4;
		}
		return size;
	}

	public static Long boxingLongFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getLong();
		} else {
			return null;
		}
	}

	public static void boxingLongToBuffer(Long l, ByteArrayBuffer buffer) {
		if (l != null) {
			buffer.putByte((byte) 1);
			buffer.putLong(l);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingLongToBuffer(Long l) {
		int size = 1;
		if (l != null) {
			size += 8;
		}
		return size;
	}

	public static Short boxingShortFromBuffer(ByteArrayBuffer buffer) {
		byte flg = buffer.getByte();
		if (flg == 1) {
			return buffer.getShort();
		} else {
			return null;
		}
	}

	public static void boxingShortToBuffer(Short s, ByteArrayBuffer buffer) {
		if (s != null) {
			buffer.putByte((byte) 1);
			buffer.putShort(s);
		} else {
			buffer.putByte((byte) 0);
		}
	}

	public static int sizeOfBoxingShortToBuffer(Short s) {
		int size = 1;
		if (s != null) {
			size += 2;
		}
		return size;
	}

}
