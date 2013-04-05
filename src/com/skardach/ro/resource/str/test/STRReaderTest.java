package com.skardach.ro.resource.str.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.skardach.ro.resource.ResourceException;
import com.skardach.ro.resource.ResourceManager;
import com.skardach.ro.resource.str.Str;
import com.skardach.ro.resource.str.StrReader;
import com.skardach.ro.resource.str.StrReader.ParseException;

public class STRReaderTest {

	@Test
	public void testEmptyStream() {
		StrReader sut = new StrReader();
		ByteArrayInputStream empty = null;
		// 1. Empty stream: return null
		try {
			empty = new ByteArrayInputStream("".getBytes());
			SimpleTextureManager stm = new SimpleTextureManager();
			ResourceManager rm = new ResourceManager(stm);
			Str result = sut.readFromStream(empty, rm);
			assertNull("Read STR from an empty stream.", result);
		} catch (ParseException e) {
			fail(e.getMessage());
		} catch (ResourceException e) {
			fail(e.getMessage());
		} finally {
			try {
				if(empty != null)
					empty.close();
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}
	}

	@Test
	public void testReadingCorrectFile() {
		StrReader sut = new StrReader();
		// 2. Correct: Read arrowstorm.str and check stuff
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File("bin/com/skardach/ro/str/test/res/arrowstorm/arrowstorm.str"));
			SimpleTextureManager stm = new SimpleTextureManager();
			ResourceManager rm = new ResourceManager(stm);
			Str result = sut.readFromStream(fis, rm);
			assertNotNull("Null returned...", result);
			System.out.println(result.toString());
		} catch (ParseException e) {
			fail(e.getMessage());
		} catch (ResourceException e) {
			fail(e.getMessage());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} finally {
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}
	}
	// 3. Mismatched magic: throw StrReader.ParseException 
	// 4. Unsupported version: throw StrReader.ParseException
	// 5. Framecount < 0: throw StrReader.ParseException
	// 6. fps <= 0: read as default (60)
	// 7. Layer count mismatch: throw StrReader.ParseException
	// 8. Reserved bytes mismatched (TODO: is it needed?)
	// 9. Texture count mismatch: throw StrReader.ParseException
	// 10. Key frame count mismatch: throw StrReader.ParseException
	// 11. Key frames in wrong order: stable sort them by framenum
}
