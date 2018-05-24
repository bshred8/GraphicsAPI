package com.thehorriblehacker.graphics.v1;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This class initializes everything that is needed to render to the screen in this Graphics API. Call one of these apiInit methods before attempting to perform ANY actions with the API.
 * @author Brady Gunter (thehorriblehacker/bshred8)
 */
public class GraphicsAPIInit {

	protected static boolean drawArrays = false;
	protected static boolean lighting = false;
	
	protected static int[] verticesX = new int[3];
	protected static int[] verticesY = new int[3];
	
	protected static DrawModes drawMode = DrawModes.DRAW_NOTHING;
	protected static Graphics2D g;
	protected static BufferStrategy bs;
	protected static JFrame frame;
	
	/**
	 * @param c The Canvas that the rendering will be done onto. The Graphics API will add the canvas to the created JFrame for you. It is easiest to have your main class extend Canvas.
	 * @param windowWidth The height of the window to be created.
	 * @param windowHeight The width of the window to be created.
	 * @param title The title of the window to be created.
	 * @param undecorated Whether or not the window will be undecorated or not.
	 * @return Returns <code>Return.NO_ERROR</code> if no error has been found and operation was successful.
	 */
	public static Return apiInit(Canvas c, final int windowWidth, final int windowHeight, final String title, boolean undecorated) {
		System.out.println("Creating window.");
		frame = new JFrame(title);
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(c);
		frame.setBackground(Color.BLACK);
		frame.setResizable(false);
		frame.setUndecorated(undecorated);
		frame.setVisible(true);
		if(frame.isVisible()) {
			System.out.println("Window creation successful.");
		}
		
		c.createBufferStrategy(2);
		bs = c.getBufferStrategy();
		g = (Graphics2D)bs.getDrawGraphics();
		if(bs == null || frame == null || g == null) {
			throw new NullPointerException("GraphicsAPI :: BufferStrategy, JFrame, or Graphics2D object is equal to null!");
		} else {
			System.out.println("Initalization complete.");
			return Return.NO_ERROR;
		}
	}
	
	/**
	 * @param c The canvas to be rendered to and placed onto the JFrame. The Graphics API adds the Canvas to the JFrame for you.
	 * @param window The window that you created that you wish the Canvas to be added to. This window will be where rendering goes on throughout your usage of the Graphics API.
	 * @return Returns <code>Return.NO_ERROR</code> if no error has been found and operation was successful.
	 */
	public static Return apiInit(Canvas c, JFrame window) {
		frame = window;
		window.add(c);
		c.createBufferStrategy(2);
		bs = c.getBufferStrategy();
		g = (Graphics2D)bs.getDrawGraphics();
		
		if(bs == null || window == null || g == null || frame == null) {
			throw new NullPointerException("GraphicsAPI :: BufferStrategy, JFrame, or Graphics2D object is equal to null!");
		} else {
			return Return.NO_ERROR;
		}
	}
	
}