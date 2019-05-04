package com.matrix.util;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class Unsafe {
	public static sun.misc.Unsafe theUnsafe;

	private static long FIELD_OFFSET_String_value;

	static {
		try {
			final PrivilegedExceptionAction<sun.misc.Unsafe> action = new PrivilegedExceptionAction<sun.misc.Unsafe>() {
				public sun.misc.Unsafe run() throws Exception {
					Field theUnsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
					theUnsafe.setAccessible(true);
					return (sun.misc.Unsafe) theUnsafe.get(null);
				}
			};

			theUnsafe = AccessController.doPrivileged(action);

			FIELD_OFFSET_String_value = theUnsafe.objectFieldOffset(String.class.getDeclaredField("value"));

		} catch (Exception e) {
			throw new RuntimeException("Unable to load unsafe", e);
		}
	}

	public static long getFieldOffset(Class type, String field) throws Exception {
		return theUnsafe.objectFieldOffset(type.getDeclaredField(field));
	}

	public static long getFieldOffset(Field field) throws Exception {
		return theUnsafe.objectFieldOffset(field);
	}

	public static Object getObjectFieldOfObject(Object obj, long fieldOffset) {
		return theUnsafe.getObject(obj, fieldOffset);
	}

	public static void setObjectFieldOfObject(Object obj, long fieldOffset, Object value) {
		theUnsafe.putObject(obj, fieldOffset, value);
	}

	public static void setBooleanFieldOfObject(Object obj, long fieldOffset, boolean value) {
		theUnsafe.putBoolean(obj, fieldOffset, value);
	}

	public static void setByteFieldOfObject(Object obj, long fieldOffset, byte value) {
		theUnsafe.putByte(obj, fieldOffset, value);
	}

	public static void setCharFieldOfObject(Object obj, long fieldOffset, char value) {
		theUnsafe.putChar(obj, fieldOffset, value);
	}

	public static void setShortFieldOfObject(Object obj, long fieldOffset, short value) {
		theUnsafe.putShort(obj, fieldOffset, value);
	}

	public static void setIntFieldOfObject(Object obj, long fieldOffset, int value) {
		theUnsafe.putInt(obj, fieldOffset, value);
	}

	public static void setFloatFieldOfObject(Object obj, long fieldOffset, float value) {
		theUnsafe.putFloat(obj, fieldOffset, value);
	}

	public static void setLongFieldOfObject(Object obj, long fieldOffset, long value) {
		theUnsafe.putLong(obj, fieldOffset, value);
	}

	public static void setDoubleFieldOfObject(Object obj, long fieldOffset, double value) {
		theUnsafe.putDouble(obj, fieldOffset, value);
	}

	public static int getIntFieldOfObject(Object obj, long fieldOffset) {
		return theUnsafe.getInt(obj, fieldOffset);
	}

	public static boolean getBooleanFieldOfObject(Object obj, long fieldOffset) {
		return theUnsafe.getBoolean(obj, fieldOffset);
	}

	public static char[] getValueOfString(String str) {
		return (char[]) theUnsafe.getObject(str, FIELD_OFFSET_String_value);
	}

}
