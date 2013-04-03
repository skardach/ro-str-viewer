package com.skardach.ro.str;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Effect animation definition mapping contents of *.str files.
 * It contains all the fields from .str file. Some of them may
 * be probably removed (_layerCount, _reserved)?
 * @author Stan Kardach
 *
 */
public class Str {
	public static final byte MAGIC[] = {'S', 'T', 'R', 'M'};
	public static final int SUPPORTED_VERSION = 148;
	public static final int DEFAULT_FPS = 60;
	public static final int RESERVED_FIELD_SIZE = 16; // bytes
	/*
	 * Maximum count of things. Unlike in roint implementation,
	 * I do now use malloc to reserve space so I'm not bound by
	 * size_t. 
	 */
	public static final int MAX_LAYER_COUNT = Integer.MAX_VALUE;
	
	public synchronized int get_version() {
		return _version;
	}
	public synchronized void set_version(int _version) {
		this._version = _version;
	}
	public synchronized int get_frameCount() {
		return _frameCount;
	}
	public synchronized void set_frameCount(int _frameCount) {
		this._frameCount = _frameCount;
	}
	public synchronized int get_fps() {
		return _fps;
	}
	public synchronized void set_fps(int _fps) {
		this._fps = _fps;
	}
	public synchronized byte[] get_reserved() {
		return _reserved;
	}
	public synchronized List<Layer> get_layers() {
		return _layers;
	}
	
	public Str() {
		_reserved = new byte[RESERVED_FIELD_SIZE];
		_layers = new ArrayList<Layer>();
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	
	public String toString(String iPrefix) {
		String result = 
			iPrefix + "Str [\n"+
			iPrefix + "  _version=" + _version + ",\n"+
			iPrefix + "  _frameCount=" + _frameCount + ",\n"+
			iPrefix + "  _fps=" + _fps + ",\n"+
			iPrefix + "  _reserved=" + Arrays.toString(_reserved) + ",\n"+
			iPrefix + "  _layers= [\n";
		for(Layer l : _layers)
		{
			result += l.toString(iPrefix + "    ") + "\n";
		}
		result += iPrefix + "  ]\n";
		result += iPrefix + "]";
		return result;
	}

	int _version;
	int _frameCount;
	int _fps;
	byte _reserved[]; // should be 16 bytes
	List<Layer> _layers;
}
