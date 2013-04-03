package com.skardach.ro.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Wrapper for InputStream with helper methods for extracting
 * data encoded in little endian.
 * @author Stanislaw Kardach
 *
 */
public class LittleEndianInputStreamAdapter {
	InputStream _stream;
	ByteBuffer _helper;

	public LittleEndianInputStreamAdapter(InputStream iStream) {
		_stream = iStream;
		_helper = ByteBuffer.allocate(4);
		_helper.order(ByteOrder.LITTLE_ENDIAN);
	}

	public int readInt() throws IOException {
		_helper.clear();
		int read = _stream.read(_helper.array());
		if(read != 4)
			throw new IOException("Not enough data to read an int");
		return _helper.getInt();
	}

	public int read(byte[] oBuffer) throws IOException {
		return _stream.read(oBuffer);
	}

	public float readFloat() throws IOException {
		_helper.clear();
		int read = _stream.read(_helper.array());
		if(read != 4)
			throw new IOException("Not enough data to read an int");
		return _helper.getFloat();
	}

}
