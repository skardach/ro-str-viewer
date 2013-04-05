package com.skardach.ro.tools;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.skardach.ro.graphics.STRRenderer;
import com.skardach.ro.resource.ResourceException;
import com.skardach.ro.resource.ResourceManager;
import com.skardach.ro.resource.str.Str;
import com.skardach.ro.resource.str.StrReader;
import com.skardach.ro.resource.str.StrReader.ParseException;
import com.skardach.ro.resource.str.test.SimpleTextureManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Opens *.str file and tries to render it.
 * @author Stanislaw Kardach
 *
 */
public class STRViewer extends JFrame {
	private static final long serialVersionUID = -9077621002191789823L;
	// UI related
	JTextArea _infoArea;
	GLCanvas _canvas;
	
	// STR related
	StrReader _reader = new StrReader();
	ResourceManager _rm = new ResourceManager(new SimpleTextureManager());
	STRRenderer _renderer;

	public static void main(String[] args) {
		STRViewer viewer = new STRViewer();
		viewer.start();
		if(viewer.isVisible())
			viewer.run();
		if(!viewer.isVisible())
			viewer.stop();
	}
	
	private void run() {
		if(_renderer != null)
		{
			_renderer.renderFrame(this);
		}
	}

	public STRViewer() {
		setTitle("STRViewer");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 600);
		getContentPane().setLayout(new GridLayout(2, 1));
		// TODO: Try with GL2ES1 -> that's the Android one, right?
		GLProfile profile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(profile);
		_canvas = new GLCanvas(capabilities);
		add(_canvas);
		_infoArea = new JTextArea();
		_infoArea.setEditable(false);
		JScrollPane p = new JScrollPane(_infoArea);
		getContentPane().add(p);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
	}

	private void start() {
		// Choose STR
		File strFile = chooseFile();
		if(strFile != null) {
			try {
				// Read STR
				InputStream stream = new FileInputStream(strFile);
				Str effect = _reader.readFromStream(stream, _rm);
				if(effect != null)
				{
					// fill in the STR details
					_infoArea.setText(effect.toString());
					// Prepare rendering objects
					_renderer = new STRRenderer(effect);
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
		}
	}

	private void stop() {
		setVisible(false);
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
