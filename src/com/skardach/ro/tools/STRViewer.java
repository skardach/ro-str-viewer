package com.skardach.ro.tools;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;


import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.skardach.ro.graphics.OpenGLWrapper;
import com.skardach.ro.graphics.Point3D;
import com.skardach.ro.graphics.RenderException;
import com.skardach.ro.graphics.Renderer;
import com.skardach.ro.graphics.STRRendererFactory;
import com.skardach.ro.resource.ResourceException;
import com.skardach.ro.resource.ResourceManager;
import com.skardach.ro.resource.str.Str;
import com.skardach.ro.resource.str.StrReader;
import com.skardach.ro.resource.str.StrReader.ParseException;
import com.skardach.ro.resource.str.test.SimpleTextureManager;

/**
 * Opens *.str file and tries to render it.
 * @author Stanislaw Kardach
 *
 */
public class STRViewer extends JFrame {
	private static final long serialVersionUID = -9077621002191789823L;
	/**
	 * Settings for effect rendering: where to put it on canvas and in what
	 * scale. This might be set somewhere else if this viewer is ever extended.
	 */
	private static final class Settings {
		public static final Point3D EFFECT_POSITION = new Point3D(0, 0, 0);
		public static final float EFFECT_ROTATION_X = 0f;
		public static final float EFFECT_ROTATION_Y = 0f;
		public static final float EFFECT_ROTATION_Z = 0f;
		public static final float EFFECT_SCALE_X = 1f;
		public static final float EFFECT_SCALE_Y = 1f;
		public static final float EFFECT_SCALE_Z = 1f;
	}
	// OpenGL settings wrapper
	OpenGLWrapper _glWrapper;
	// UI related
	JTextArea _infoArea;
	GLCanvas _canvas;
	
	// STR related
	StrReader _reader = new StrReader();
	Renderer _renderer;
	
	public static void main(String[] args) {
		GLProfile.initSingleton(true);
		STRViewer viewer = new STRViewer();
		viewer.start();
		if(viewer.isVisible())
			viewer.run();
		if(!viewer.isVisible())
			viewer.stop();
	}
	
	private void run() {
		_glWrapper.startAnimation();
	}

	public STRViewer() {
		setTitle("STRViewer");
		setSize(800, 600);
		getContentPane().setLayout(new GridLayout(2, 1));
		_glWrapper = OpenGLWrapper.createDesktopWrapper();
		_canvas = _glWrapper.createGLCanvasWithAnimator();
		getContentPane().add(_canvas);
		// Add text area
		_infoArea = new JTextArea();
		_infoArea.setEditable(false);
		JScrollPane p = new JScrollPane(_infoArea);
		getContentPane().add(p);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stop();
				// FIXME: I'm making a shortcut here... proper cleaning needed
				System.exit(0);
			}
		});
	}

	private void start() {
		// Choose STR
		//File strFile = chooseFile();
		File strFile = new File("/home/kardasan/dev/eclipse/ro-str_viewer/src/com/skardach/ro/resource/str/test/res/arrowstorm/stormgust.str");
		//File strFile = new File("/home/kardasan/dev/eclipse/ro-str_viewer/src/com/skardach/ro/resource/str/test/res/arrowstorm/arrowstorm.str");
		if(strFile != null) {
			try {
				// Read STR
				ResourceManager _rm = 
					new ResourceManager(
						new SimpleTextureManager(strFile.getParent()));
				InputStream stream = new FileInputStream(strFile);
				Str effect = _reader.readFromStream(_rm, stream);
				if(effect != null)
				{
					// fill in the STR details
					_infoArea.setText(effect.toString());
					// Initialize OpenGL
					// Prepare rendering objects
					_renderer = 
						STRRendererFactory.createRenderer(
							effect, 
							Settings.EFFECT_POSITION,
							Settings.EFFECT_ROTATION_X,
							Settings.EFFECT_ROTATION_Y,
							Settings.EFFECT_ROTATION_Z,
							Settings.EFFECT_SCALE_X,
							Settings.EFFECT_SCALE_Y,
							Settings.EFFECT_SCALE_Z);
					_glWrapper.registerRendererOnCanvas(_renderer, _canvas);
					// Display everything
					setVisible(true);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(
					this,
					String.format(
						"Error opening file %s: %s", 
						strFile, 
						e.getMessage()));
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(
					this,
					String.format(
						"Error parsing file %s: %s", 
						strFile, 
						e.getMessage()));
			} catch (ResourceException e) {
				JOptionPane.showMessageDialog(
					this,
					String.format(
						"Resource error while processing file %s: %s", 
						strFile, 
						e.getMessage()));
			} 
			catch (RenderException e) {
				JOptionPane.showMessageDialog(
					this,
					String.format(
						"Generic error while processing %s: %s", 
						strFile, 
						e.getMessage()));
			}
		}
	}

	private void stop() {
		setVisible(false);
		dispose();
		_glWrapper.stopAnimation();
		_glWrapper.destroyCanvas(_canvas);
	}

	private File chooseFile() {
		FileDialog fd = new FileDialog(this, "Select STR file");
		fd.setFilenameFilter(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("str"); 
			}
		});
		fd.setVisible(true);
		if(fd.getDirectory() != null && fd.getFile() != null)
			return new File(fd.getDirectory(),fd.getFile());
		return null;
	}

}
