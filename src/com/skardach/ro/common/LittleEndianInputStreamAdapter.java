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
	/**
	 * Create little endian wrapper on the input stream
	 * @param iStream stream to wrap
	 */
	public LittleEndianInputStreamAdapter(InputStream iStream) {
		_stream = iStream;
		_helper = ByteBuffer.allocate(4);
		_helper.order(ByteOrder.LITTLE_ENDIAN);
	}
	/**
	 * Read 32bit little endian integer from the stream.
	 * @return integer read from stream
	 * @throws IOException If there is a problem reading from stream.
	 */
	public int readInt() throws IOException {
		_helper.clear();
		int read = _stream.read(_helper.array());
		if(read != 4)
			throw new IOException("Not enough data to read an int");
		return _helper.getInt();
	}
	/**
	 * Read to output buffer.
	 * @param oBuffer buffer to read to. Cannot be null
	 * @return number of bytes read. @see java.nio.ByteBuffer#read(byte[])
	 * @throws IOException If there are any problems reading
	 */
	public int read(byte[] oBuffer) throws IOException {
		assert(_stream != null);
		return _stream.read(oBuffer);
	}
	/**
	 * Read 32bit little endian float value.
	 * @return big endian float
	 * @throws IOException If unable to read 4 bytes properly.
	 */
	public float readFloat() throws IOException {
		_helper.clear();
		int read = _stream.read(_helper.array());
		if(read != 4)
			throw new IOException("Not enough data to read an int");
		return _helper.getFloat();
	}

}
