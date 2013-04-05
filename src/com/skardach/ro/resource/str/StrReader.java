package com.skardach.ro.resource.str;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.skardach.ro.common.LittleEndianInputStreamAdapter;
import com.skardach.ro.graphics.Color;
import com.skardach.ro.graphics.Point;
import com.skardach.ro.graphics.Rectangle;
import com.skardach.ro.graphics.Texture;
import com.skardach.ro.resource.ResourceException;
import com.skardach.ro.resource.ResourceManager;
import com.skardach.ro.resource.TextureManager;

public class StrReader {

	public class ParseException extends Exception {
		private static final long serialVersionUID = 7050197797878645891L;

		public ParseException(String string) {
			super(string);
		}

	}

	public Str readFromStream(InputStream iStream, ResourceManager iResourceManager) throws ParseException, ResourceException {
		if(iResourceManager == null)
			throw new ResourceException("No resource manager available");
		TextureManager textureManager = iResourceManager.getTextureManager();
		if(textureManager == null)
			throw new ResourceException("No texture manager available");
		Str result = new Str();
		try {
			// Prepare a data stream to read more easily
			LittleEndianInputStreamAdapter stream = new LittleEndianInputStreamAdapter(iStream);
			// read magic and bail of wrong
			byte magic[] = new byte[4];
			int read = stream.read(magic);
			if(read <= 0)
				return null;
			if(!Arrays.equals(magic, Str.MAGIC))
				throw new ParseException(String.format("Invalid magic: [%s], expected: [%s]", Arrays.toString(magic), Arrays.toString(Str.MAGIC)));
			// XXX: reading of int should work because according to api docs
			// readInt() should read 4 bytes and interpret them as an int.
			result.set_version(stream.readInt());
			if(result.get_version() != Str.SUPPORTED_VERSION)
				throw new ParseException(String.format("Unsupported version: 0x%X", result.get_version()));
			result.set_frameCount(stream.readInt());
			if(result.get_frameCount() < 0)
				throw new ParseException("Framecount less than 0");
			result.set_fps(stream.readInt());
			if(result.get_fps() <= 0)
				result.set_fps(Str.DEFAULT_FPS);
			// read layers
			int layerCount = stream.readInt();
			if(layerCount < 0)
				throw new ParseException("Layer count < 0");
			if(layerCount > Str.MAX_LAYER_COUNT)
				throw new ParseException("Too many layers: " + layerCount);
			read = stream.read(result.get_reserved());
			if(read != Str.RESERVED_FIELD_SIZE)
				throw new ParseException(String.format("Invalid reserved field. Size(%d) [%s]", read,  Arrays.toString(result.get_reserved())));
			for(int l = 0; l < layerCount; l++)
				result.get_layers().add(readLayer(textureManager, stream));
		} catch (IOException e) {
			throw new ParseException("Problem reading stream: " + e.getMessage());
		}
		return result;
	}

	private Layer readLayer(TextureManager textureManager,
			LittleEndianInputStreamAdapter stream) throws IOException, ParseException,
			ResourceException {
		Layer layer = new Layer();
		int textureCount = stream.readInt();
		if(textureCount > Layer.MAX_TEXTURE_COUNT)
			throw new ParseException("Too many textures per layer: " + textureCount);
		for(int t = 0; t < textureCount; t++)
			layer.get_textures().add(readTexture(textureManager, stream));
		int keyFrameCount = stream.readInt();
		if(keyFrameCount > Layer.MAX_KEYFRAME_COUNT)
			throw new ParseException("Too many key frames: " + keyFrameCount);
		for(int kf = 0; kf < keyFrameCount; kf++)
			layer.get_keyFrames().add(readKeyFrame(stream));
		return layer;
	}

	private Texture readTexture(TextureManager textureManager,
			LittleEndianInputStreamAdapter stream) throws IOException, ResourceException {
		byte textureNameBuffer[] = new byte[Texture.NAME_SIZE];
		stream.read(textureNameBuffer);
		String textureName = new String(textureNameBuffer, 0, Texture.NAME_SIZE).trim(); // this should use UTF8
		Texture texture = textureManager.getTexture(textureName);
		if(texture == null) // if no such texture found
			throw new ResourceException(String.format("Texture [%s] could not be located", textureName));
		return texture;
	}

	private KeyFrame readKeyFrame(LittleEndianInputStreamAdapter stream) throws IOException {
		KeyFrame keyFrame = new KeyFrame();
		keyFrame.set_framenum(stream.readInt());
		keyFrame.set_frameType(KeyFrameType.fromInt(stream.readInt()));
		float x = stream.readFloat();
		float y = stream.readFloat();
		keyFrame.set_position(new Point(x, y)); // TODO: Lightweight for points?
		keyFrame.set_u(stream.readFloat());
		keyFrame.set_v(stream.readFloat());
		keyFrame.set_us(stream.readFloat());
		keyFrame.set_vs(stream.readFloat());
		keyFrame.set_u2(stream.readFloat());
		keyFrame.set_v2(stream.readFloat());
		keyFrame.set_us2(stream.readFloat());
		keyFrame.set_vs2(stream.readFloat());
		// Rectangle corners' coordinates
		// x coordinates first
		float ax = stream.readFloat();
		float bx = stream.readFloat();
		float cx = stream.readFloat();
		float dx = stream.readFloat();
		// now y coordinates
		float ay = stream.readFloat();
		float by = stream.readFloat();
		float cy = stream.readFloat();
		float dy = stream.readFloat();
		keyFrame.set_rectangle(
			new Rectangle(
				new Point(ax, ay), 
				new Point(bx, by), 
				new Point(cx, cy), 
				new Point(dx, dy)));
		keyFrame.set_animationFrame(stream.readFloat());
		keyFrame.set_animationType(AnimationType.fromInt(stream.readInt()));
		keyFrame.set_animationDelta(stream.readFloat());
		keyFrame.set_rotation(stream.readFloat());
		// Read color
		float r = stream.readFloat();
		float g = stream.readFloat();
		float b = stream.readFloat();
		float alpha = stream.readFloat();
		keyFrame.set_color(new Color(r, g, b, alpha));
		keyFrame.set_sourceAlpha(stream.readInt());
		keyFrame.set_destAlpha(stream.readInt());
		keyFrame.set_multiTexturePreset(MultiTextureMode.fromInt(stream.readInt()));
		return keyFrame;
	}
}
