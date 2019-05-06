package com.matrix.disruptor;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matrix.util.ByteArrayBuffer;
import com.matrix.util.ByteArrayBufferSerializer;
import com.matrix.util.FileUtil;
import com.matrix.util.Unsafe;

public class CommandReplayer {

	private static Map<String, int[]> resultObjectTypeFieldsTypeMap = new HashMap<>();

	private static Map<String, long[]> resultObjectTypeFieldsAddressMap = new HashMap<>();

	public static void replay(String jFileBasePath, String disruptorName, Object... services) throws Exception {
		Map<String, Object> typeServiceMap = new HashMap<>();
		for (int i = 0; i < services.length; i++) {
			Object service = services[i];
			typeServiceMap.put(service.getClass().getName(), service);
		}

		String fileName = FileUtil.getRecentFileName(jFileBasePath + File.separatorChar + disruptorName);
		List<Command> commands = new ArrayList<>();
		if (fileName != null) {
			RandomAccessFile file = new RandomAccessFile(fileName, "r");
			FileChannel channel = file.getChannel();
			long size = channel.size();
			ByteBuffer buffer = ByteBuffer.allocate((int) size);// TODO 超过int最大值的问题
			channel.read(buffer);
			buffer.flip();
			ByteArrayBuffer byteArrayBuffer = ByteArrayBuffer.fromByteBuffer(buffer);
			while (byteArrayBuffer.hasRemaining()) {
				Command command = new Command();
				command.fillByByteArrayBuffer(byteArrayBuffer);
				commands.add(command);
				Object returnValue = null;
				byte returnValueNotNullFlg = byteArrayBuffer.getByte();
				if (returnValueNotNullFlg == 1) {
					int returnTypeCode = byteArrayBuffer.getInt();
					if (returnTypeCode == 1) {
						returnValue = byteArrayBuffer.getInt();
					} else if (returnTypeCode == 2) {
						returnValue = ByteArrayBufferSerializer.notNullStringFromBuffer(byteArrayBuffer);
					} else if (returnTypeCode == 3) {
						returnValue = ByteArrayBufferSerializer.booleanFromBuffer(byteArrayBuffer);
					} else if (returnTypeCode == 4) {
						returnValue = byteArrayBuffer.getLong();
					} else if (returnTypeCode == 5) {
						returnValue = byteArrayBuffer.getFloat();
					} else if (returnTypeCode == 6) {
						returnValue = byteArrayBuffer.getShort();
					} else if (returnTypeCode == 7) {
						returnValue = byteArrayBuffer.getByte();
					} else if (returnTypeCode == 8) {
						returnValue = byteArrayBuffer.getChar();
					} else if (returnTypeCode == 9) {
						returnValue = byteArrayBuffer.getDouble();
					} else if (returnTypeCode == 10) {
						String returnValueType = ByteArrayBufferSerializer.notNullStringFromBuffer(byteArrayBuffer);
						int[] resultObjectFieldsTypeArray = resultObjectTypeFieldsTypeMap.get(returnValueType);
						long[] resultObjectFieldsAddressArray = resultObjectTypeFieldsAddressMap.get(returnValueType);
						if (resultObjectFieldsTypeArray == null) {
							Field[] resultFields = Class.forName(returnValueType).getDeclaredFields();
							resultObjectFieldsTypeArray = new int[resultFields.length];
							resultObjectFieldsAddressArray = new long[resultFields.length];
							for (int i = 0; i < resultFields.length; i++) {
								Field field = resultFields[i];
								resultObjectFieldsAddressArray[i] = Unsafe.getFieldOffset(field);
								if (field.getType().equals(int.class)) {
									resultObjectFieldsTypeArray[i] = 1;
								} else if (field.getType().equals(String.class)) {
									resultObjectFieldsTypeArray[i] = 2;
								} else if (field.getType().equals(boolean.class)) {
									resultObjectFieldsTypeArray[i] = 3;
								} else if (field.getType().equals(long.class)) {
									resultObjectFieldsTypeArray[i] = 4;
								} else if (field.getType().equals(float.class)) {
									resultObjectFieldsTypeArray[i] = 5;
								} else if (field.getType().equals(short.class)) {
									resultObjectFieldsTypeArray[i] = 6;
								} else if (field.getType().equals(byte.class)) {
									resultObjectFieldsTypeArray[i] = 7;
								} else if (field.getType().equals(char.class)) {
									resultObjectFieldsTypeArray[i] = 8;
								} else if (field.getType().equals(double.class)) {
									resultObjectFieldsTypeArray[i] = 9;
								} else if (field.getType().equals(Integer.class)) {
									resultObjectFieldsTypeArray[i] = 10;
								} else if (field.getType().equals(Boolean.class)) {
									resultObjectFieldsTypeArray[i] = 11;
								} else if (field.getType().equals(Long.class)) {
									resultObjectFieldsTypeArray[i] = 12;
								} else if (field.getType().equals(Float.class)) {
									resultObjectFieldsTypeArray[i] = 13;
								} else if (field.getType().equals(Short.class)) {
									resultObjectFieldsTypeArray[i] = 14;
								} else if (field.getType().equals(Byte.class)) {
									resultObjectFieldsTypeArray[i] = 15;
								} else if (field.getType().equals(Character.class)) {
									resultObjectFieldsTypeArray[i] = 16;
								} else if (field.getType().equals(Double.class)) {
									resultObjectFieldsTypeArray[i] = 17;
								} else {
								}
							}
							resultObjectTypeFieldsTypeMap.put(returnValueType, resultObjectFieldsTypeArray);
							resultObjectTypeFieldsAddressMap.put(returnValueType, resultObjectFieldsAddressArray);
						}

						returnValue = Class.forName(returnValueType).newInstance();
						for (int i = 0; i < resultObjectFieldsTypeArray.length; i++) {
							int typeCode = resultObjectFieldsTypeArray[i];
							if (typeCode == 1) {
								Unsafe.setIntFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getInt());
							} else if (typeCode == 2) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.stringFromBuffer(byteArrayBuffer));
							} else if (typeCode == 3) {
								Unsafe.setBooleanFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.booleanFromBuffer(byteArrayBuffer));
							} else if (typeCode == 4) {
								Unsafe.setLongFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getLong());
							} else if (typeCode == 5) {
								Unsafe.setFloatFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getFloat());
							} else if (typeCode == 6) {
								Unsafe.setShortFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getShort());
							} else if (typeCode == 7) {
								Unsafe.setByteFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getByte());
							} else if (typeCode == 8) {
								Unsafe.setCharFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getChar());
							} else if (typeCode == 9) {
								Unsafe.setDoubleFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										byteArrayBuffer.getDouble());
							} else if (typeCode == 10) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingIntegerFromBuffer(byteArrayBuffer));
							} else if (typeCode == 11) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingBooleanFromBuffer(byteArrayBuffer));
							} else if (typeCode == 12) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingLongFromBuffer(byteArrayBuffer));
							} else if (typeCode == 13) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingFloatFromBuffer(byteArrayBuffer));
							} else if (typeCode == 14) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingShortFromBuffer(byteArrayBuffer));
							} else if (typeCode == 15) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingByteFromBuffer(byteArrayBuffer));
							} else if (typeCode == 16) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingCharacterFromBuffer(byteArrayBuffer));
							} else if (typeCode == 17) {
								Unsafe.setObjectFieldOfObject(returnValue, resultObjectFieldsAddressArray[i],
										ByteArrayBufferSerializer.boxingDoubleFromBuffer(byteArrayBuffer));
							} else {
							}
						}

					} else {
					}

				}
			}
		}

		for (Command cmd : commands) {
			Object service = typeServiceMap.get(cmd.getType());
			if (cmd.getParameters() != null && cmd.getParameters().length > 0) {
				try {
					service.getClass().getMethod(cmd.getMethod(), cmd.getParameterTypes()).invoke(service,
							cmd.getParameters());
				} catch (Exception e) {
				}
			} else {
				try {
					service.getClass().getMethod(cmd.getMethod()).invoke(service);
				} catch (Exception e) {
				}
			}
		}
	}
}
