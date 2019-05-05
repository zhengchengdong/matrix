package com.matrix.disruptor;

import com.matrix.util.ByteArrayBuffer;
import com.matrix.util.ByteArrayBufferSerializer;

public class Command {

	private String type;

	private String method;

	private Object[] parameters;

	private long executeTime = System.currentTimeMillis();

	public void toByteArrayBuffer(ByteArrayBuffer buffer) throws Exception {
		ByteArrayBufferSerializer.notNullStringToBuffer(type, buffer);
		ByteArrayBufferSerializer.notNullStringToBuffer(method, buffer);
		if (parameters != null) {
			buffer.putByte((byte) 1);
			buffer.putInt(parameters.length);
			for (int i = 0; i < parameters.length; i++) {
				Object parameter = parameters[i];
				if (parameter != null) {
					buffer.putByte((byte) 1);
					if (parameter instanceof Integer) {
						buffer.putInt(1);
						buffer.putInt((Integer) parameter);
					} else if (parameter instanceof String) {
						buffer.putInt(2);
						ByteArrayBufferSerializer.notNullStringToBuffer((String) parameter, buffer);
					} else if (parameter instanceof Boolean) {
						buffer.putInt(3);
						ByteArrayBufferSerializer.notNullBoxingBooleanToBuffer((Boolean) parameter, buffer);
					} else if (parameter instanceof Long) {
						buffer.putInt(4);
						buffer.putLong((Long) parameter);
					} else if (parameter instanceof Float) {
						buffer.putInt(5);
						buffer.putFloat((Float) parameter);
					} else if (parameter instanceof Short) {
						buffer.putInt(6);
						buffer.putShort((Short) parameter);
					} else if (parameter instanceof Byte) {
						buffer.putInt(7);
						buffer.putByte((Byte) parameter);
					} else if (parameter instanceof Character) {
						buffer.putInt(8);
						buffer.putChar((Character) parameter);
					} else if (parameter instanceof Double) {
						buffer.putInt(9);
						buffer.putDouble((Double) parameter);
					} else {
						throw new UnsupportedTypeException();
					}
				} else {
					buffer.putByte((byte) 0);
				}
			}
		} else {
			buffer.putByte((byte) 0);
		}
		buffer.putLong(executeTime);
	}

	public void fillByByteArrayBuffer(ByteArrayBuffer buffer) throws Exception {
		type = ByteArrayBufferSerializer.notNullStringFromBuffer(buffer);
		method = ByteArrayBufferSerializer.notNullStringFromBuffer(buffer);
		byte notNull = buffer.getByte();
		if (notNull == 1) {
			int parametersSize = buffer.getInt();
			parameters = new Object[parametersSize];
			for (int i = 0; i < parametersSize; i++) {
				byte parameterNotNull = buffer.getByte();
				if (parameterNotNull == 1) {
					int typeCode = buffer.getInt();
					if (typeCode == 1) {
						parameters[i] = buffer.getInt();
					} else if (typeCode == 2) {
						parameters[i] = ByteArrayBufferSerializer.notNullStringFromBuffer(buffer);
					} else if (typeCode == 3) {
						parameters[i] = ByteArrayBufferSerializer.notNullBoxingBooleanFromBuffer(buffer);
					} else if (typeCode == 4) {
						parameters[i] = buffer.getLong();
					} else if (typeCode == 5) {
						parameters[i] = buffer.getFloat();
					} else if (typeCode == 6) {
						parameters[i] = buffer.getShort();
					} else if (typeCode == 7) {
						parameters[i] = buffer.getByte();
					} else if (typeCode == 8) {
						parameters[i] = buffer.getChar();
					} else if (typeCode == 9) {
						parameters[i] = buffer.getDouble();
					} else {
						throw new UnsupportedTypeException();
					}
				}
			}
		}
		executeTime = buffer.getLong();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

}
