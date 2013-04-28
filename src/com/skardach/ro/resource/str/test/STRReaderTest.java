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
/**
 * Tests for STRReader class.
 * @author Stanislaw Kardach
 *
 */
public class STRReaderTest {
	/**
	 * Test reading empty stream
	 */
	@Test
	public void testEmptyStream() {
		StrReader sut = new StrReader();
		ByteArrayInputStream empty = null;
		// 1. Empty stream: return null
		try {
			empty = new ByteArrayInputStream("".getBytes());
			SimpleTextureManager stm = new SimpleTextureManager("");
			ResourceManager rm = new ResourceManager(stm);
			Str result = sut.readFromStream(rm, empty);
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
	/**
	 * Test whether reading a correct str file does not crash us.
	 * FIXME: there should be a verification of the output at the end.
	 */
	@Test
	public void testReadingCorrectFile() {
		StrReader sut = new StrReader();
		// 2. Correct: Read arrowstorm.str and check stuff
		FileInputStream fis = null;
		try {
			File f = new File("bin/com/skardach/ro/resource/str/test/res/arrowstorm/arrowstorm.str");
			fis = new FileInputStream(f);
			SimpleTextureManager stm =
				new SimpleTextureManager(f.getAbsolutePath());
			ResourceManager rm = new ResourceManager(stm);
			Str result = sut.readFromStream(rm, fis);
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
	// 9. Texture count mismatch: throw StrReader.ParseException
	// 10. Key frame count mismatch: throw StrReader.ParseException
	// 11. Key frames in wrong order: stable sort them by framenum
}
