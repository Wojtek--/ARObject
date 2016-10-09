package com.mgr;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MemUtil {
	public static FloatBuffer makeFloatBufferFromArray(float[] arr) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arr.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(arr);
		floatBuffer.position(0);
		return floatBuffer;
	}

	public static FloatBuffer makeFloatBuffer(int size) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.position(0);
		return floatBuffer;
	}

	public static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arr.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(arr);
		floatBuffer.position(0);
		return floatBuffer;
	}

}
