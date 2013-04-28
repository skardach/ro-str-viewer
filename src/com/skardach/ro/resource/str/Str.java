package com.skardach.ro.resource.str;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Effect animation definition mapping contents of *.str files.
 * It contains all the fields from .str file. Some of them may
 * be probably removed (_layerCount, _reserved)?
 * @author Stanislaw Kardach
 *
 */
public class Str {
	/**
	 * Size of the reserved field.
	 */
	public static final int RESERVED_FIELD_SIZE = 16; // bytes
	/**
	 * @return Get version of the STR description.
	 */
	public synchronized int get_version() {
		return _version;
	}
	/**
	 * Set version of STR description
	 */
	public synchronized void set_version(int _version) {
		this._version = _version;
	}
	/**
	 * @return Get number of frames in this effect.
	 */
	public synchronized int get_frameCount() {
		return _frameCount;
	}
	/**
	 * Set number of frames in this effect.
	 */
	public synchronized void set_frameCount(int _frameCount) {
		this._frameCount = _frameCount;
	}
	/**
	 * @return Get frames per second for this effect.
	 */
	public synchronized int get_fps() {
		return _fps;
	}
	/**
	 * Set frames per second for this effect.
	 */
	public synchronized void set_fps(int _fps) {
		this._fps = _fps;
	}
	/**
	 * @return Get reserved bytes
	 */
	public synchronized byte[] get_reserved() {
		return _reserved;
	}
	/**
	 * @return Get list of layers in this effect
	 */
	public synchronized List<Layer> get_layers() {
		return _layers;
	}

	@Override
	public String toString() {
		return toString("");
	}
	/**
	 * String representation prepended by prefix
	 * @param iPrefix prefix to prepend
	 * @return String representation
	 */
	public String toString(String iPrefix) {
		String result =
			iPrefix + "Str [\n"+
			iPrefix + "  _version=" + _version + ",\n"+
			iPrefix + "  _frameCount=" + _frameCount + ",\n"+
			iPrefix + "  _fps=" + _fps + ",\n"+
			iPrefix + "  _reserved=" + Arrays.toString(_reserved) + ",\n"+
			iPrefix + "  _layers= [\n";
		int i = 1;
		for(Layer l : _layers)
		{
			result += l.toString(iPrefix + "    "+i+":") + "\n";
			i++;
		}
		result += iPrefix + "  ]\n";
		result += iPrefix + "]";
		return result;
	}

	int _version;
	int _frameCount;
	int _fps;
	byte _reserved[] = new byte[RESERVED_FIELD_SIZE];
	List<Layer> _layers = new ArrayList<Layer>();
}
