package com.skardach.ro.tools;

import java.awt.Button;
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


import javax.media.opengl.awt.GLCanvas;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	/**
	 * Main method. Initialise OpenGL and run the viewer.
	 * @param args
	 */
	public static void main(String[] args) {
		OpenGLWrapper.initOpenGL();
		STRViewer viewer = new STRViewer();
		viewer.start();
		if(viewer.isVisible())
			viewer.run();
		if(!viewer.isVisible())
			viewer.stop();
	}

	private void run() {
		_canvas.requestFocus();
	}
	/**
	 * Construct the viewer window.
	 */
	public STRViewer() {
		setTitle("STRViewer");
		getContentPane().setLayout(new GridLayout(2, 1));
		_glWrapper = OpenGLWrapper.createDesktopWrapper(60);
		// OpenGL canvas
		_canvas = _glWrapper.createGLCanvasWithAnimator();
		add(_canvas);
		// Add text area
		_infoArea = new JTextArea();
		_infoArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(_infoArea);
		JPanel panel = new JPanel(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(scrollPane);
		// vertical stack panel for start/stop, pause/resume buttons
		JPanel buttonPanel = new JPanel(true);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		Button startStopAnimationButton = new Button("Start");
		startStopAnimationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Button b = (Button)e.getSource();
				if(b.getLabel() == "Start") { // TODO: this could be a state
					_glWrapper.resetAnimation();
					_glWrapper.startAnimation();
					b.setLabel("Stop");
				} else {
					_glWrapper.stopAnimation();
					b.setLabel("Start");
				}
			}
		});
		Button pauseResumeAnimationButton = new Button("Pause");
		pauseResumeAnimationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Button b = (Button)e.getSource();
				if(b.getLabel() == "Pause") { // TODO: this could be a state
					_glWrapper.stopAnimation();
					b.setLabel("Resume");
				} else {
					_glWrapper.startAnimation();
					b.setLabel("Pause");
				}
			}
		});
		buttonPanel.add(startStopAnimationButton);
		buttonPanel.add(pauseResumeAnimationButton);
		panel.add(buttonPanel);
		getContentPane().add(panel);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stop();
				// FIXME: I'm making a shortcut here... proper cleaning needed
				System.exit(0);
			}
		});
		pack();
		setSize(800, 600);
	}

	private void start() {
		// Choose STR
		File strFile = chooseFile();
		if(strFile != null) {
			try {
				// Read STR
				ResourceManager rm =
					new ResourceManager(
						new SimpleTextureManager(strFile.getParent()));
				InputStream stream = new FileInputStream(strFile);
				StrReader reader = new StrReader();
				Str effect = reader.readFromStream(rm, stream);
				if(effect != null)
				{
					// fill in the STR details
					_infoArea.setText(effect.toString());
					// Initialise OpenGL
					// Prepare rendering objects
					Renderer renderer =
						STRRendererFactory.createEffectRenderer(
							effect,
							Settings.EFFECT_POSITION,
							Settings.EFFECT_ROTATION_X,
							Settings.EFFECT_ROTATION_Y,
							Settings.EFFECT_ROTATION_Z,
							Settings.EFFECT_SCALE_X,
							Settings.EFFECT_SCALE_Y,
							Settings.EFFECT_SCALE_Z,
							60,
							true);
					// Pre-load textures
					_glWrapper.registerRendererOnCanvas(renderer, _canvas);
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
		_glWrapper.stopAnimation();
		_glWrapper.destroyCanvas(_canvas);
		dispose();
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
