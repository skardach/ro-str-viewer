package com.skardach.ro;

import java.awt.FileDialog;

import javax.imageio.stream.FileImageInputStream;

import com.skardach.ro.str.Str;
import com.skardach.ro.str.StrReader;

/**
 * Opens *.str file and tries to render it.
 * @author Stanislaw Kardach
 *
 */
public class STRViewer {

	public STRViewer() {
	}

	public static void main(String[] args) {
		String filename = args[1];
		StrReader reader = new StrReader();
		//Str effect = reader.readFromStream(new FileImageInputStream(f), iResourceManager)
	}

}
