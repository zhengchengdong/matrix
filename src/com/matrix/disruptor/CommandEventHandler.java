package com.matrix.disruptor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.lmax.disruptor.EventHandler;
import com.matrix.util.ByteArrayBuffer;
import com.matrix.util.ByteArrayBufferSerializer;
import com.matrix.util.FileUtil;
import com.matrix.util.JsonUtil;
import com.matrix.util.Unsafe;

public class CommandEventHandler implements EventHandler<CommandEvent> {
	private AggregateRootRepository aggregateRootRepository;

	private String snapshotFileBasePath = "./snapshot";

	private String jFileBasePath = ".";

	private JournalFile jFile;

	private ByteArrayBuffer byteArrayBuffer;

	private Map<String, int[]> resultObjectTypeFieldsTypeMap = new HashMap<>();

	private Map<String, long[]> resultObjectTypeFieldsAddressMap = new HashMap<>();

	public CommandEventHandler(AggregateRootRepository aggregateRootRepository, String snapshotFileBasePath,
			String jFileBasePath) {
		this.aggregateRootRepository = aggregateRootRepository;
		this.snapshotFileBasePath = snapshotFileBasePath;
		this.jFileBasePath = jFileBasePath;
	}

	@Override
	public void onEvent(CommandEvent event, long sequence, boolean endOfBatch) throws Exception {
		if (jFile == null) {
			String recentFileName = FileUtil.getRecentFileName(jFileBasePath);
			if (recentFileName == null || recentFileName.equals("")) {
				recentFileName = jFileBasePath + "/" + System.currentTimeMillis();
			}
			jFile = new JournalFile(recentFileName);
			byteArrayBuffer = new ByteArrayBuffer(1024 * 1024);
		}
		if (!event.isSnapshot()) {
			Command cmd = event.getCmd();
			DeferredResult result = event.getResult();
			Function function = event.getFunction();
			if (function != null) {
				try {
					Object v = function.run();
					recordJournalFile(cmd, v);
					result.setResult(v);
				} catch (Exception e) {
					result.setException(e);
				}
				return;
			}
			Process process = event.getProcess();
			if (process != null) {
				try {
					process.run();
					recordJournalFile(cmd, null);
					result.setResult(null);
				} catch (Exception e) {
					result.setException(e);
				}
				return;
			}
		} else {
			try {
				saveSnapshot();
				jFile.close();
				jFile = new JournalFile(jFileBasePath + "/" + System.currentTimeMillis());
			} catch (Throwable e) {
				System.exit(0);// 任何失败系统停机。
			}
		}
	}

	private void recordJournalFile(Command cmd, Object result) {
		byteArrayBuffer.reset();
		try {
			cmd.toByteArrayBuffer(byteArrayBuffer);
			if (result != null) {
				byteArrayBuffer.putByte((byte) 1);
				String resultType = result.getClass().getName();
				ByteArrayBufferSerializer.notNullStringToBuffer(resultType, byteArrayBuffer);
				int[] resultObjectFieldsTypeArray = resultObjectTypeFieldsTypeMap.get(resultType);
				long[] resultObjectFieldsAddressArray = resultObjectTypeFieldsAddressMap.get(resultType);
				if (resultObjectFieldsTypeArray == null) {
					Field[] resultFields = result.getClass().getFields();
					resultObjectFieldsTypeArray = new int[resultFields.length];
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
					resultObjectTypeFieldsTypeMap.put(resultType, resultObjectFieldsTypeArray);
					resultObjectTypeFieldsAddressMap.put(resultType, resultObjectFieldsAddressArray);
				}

				for (int i = 0; i < resultObjectFieldsTypeArray.length; i++) {
					int typeCode = resultObjectFieldsTypeArray[i];
					if (typeCode == 1) {
						byteArrayBuffer.putInt(Unsafe.getIntFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 2) {
						ByteArrayBufferSerializer.stringToBuffer(
								(String) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 3) {
						ByteArrayBufferSerializer.booleanToBuffer(
								Unsafe.getBooleanFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 4) {
						byteArrayBuffer.putLong(Unsafe.getLongFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 5) {
						byteArrayBuffer
								.putFloat(Unsafe.getFloatFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 6) {
						byteArrayBuffer
								.putShort(Unsafe.getShortFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 7) {
						byteArrayBuffer.putByte(Unsafe.getByteFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 8) {
						byteArrayBuffer.putChar(Unsafe.getCharFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 9) {
						byteArrayBuffer
								.putDouble(Unsafe.getDoubleFieldOfObject(result, resultObjectFieldsAddressArray[i]));
					} else if (typeCode == 10) {
						ByteArrayBufferSerializer.boxingIntegerToBuffer(
								(Integer) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 11) {
						ByteArrayBufferSerializer.boxingBooleanToBuffer(
								(Boolean) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 12) {
						ByteArrayBufferSerializer.boxingLongToBuffer(
								(Long) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 13) {
						ByteArrayBufferSerializer.boxingFloatToBuffer(
								(Float) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 14) {
						ByteArrayBufferSerializer.boxingShortToBuffer(
								(Short) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 15) {
						ByteArrayBufferSerializer.boxingByteToBuffer(
								(Byte) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 16) {
						ByteArrayBufferSerializer.boxingCharacterToBuffer(
								(Character) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else if (typeCode == 17) {
						ByteArrayBufferSerializer.boxingDoubleToBuffer(
								(Double) Unsafe.getObjectFieldOfObject(result, resultObjectFieldsAddressArray[i]),
								byteArrayBuffer);
					} else {
					}
				}
			} else {
				byteArrayBuffer.putByte((byte) 0);
			}

			jFile.write(byteArrayBuffer.toByteBuffer());
		} catch (Throwable e) {
			System.exit(0);// 任何失败系统停机。
		}
	}

	private void saveSnapshot() throws IOException {
		Snapshot snapshot = aggregateRootRepository.creatSnapshot();
		JsonUtil.saveObjToJsonFile(snapshotFileBasePath, snapshot.getCreateTime() + "", snapshot);
	}

}
