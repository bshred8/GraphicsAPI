package com.thehorriblehacker.graphics.v1;

import java.awt.Graphics2D;

import javax.swing.JFrame;

import static com.thehorriblehacker.graphics.v1.GraphicsAPIInit.*;

/**
 * This class handles enabling of draw modes and <code>Enable.ARRAY_DRAWING</code> and <code>Enable.LIGHT_DRAWING</code>. <code>Enable</code> enum should be used only in <code>apiEnable</code>. <code>DrawModes</code> enum should only be used in <code>apiBegin()</code>.
 * @author Brady Gunter (thehorriblehacker/bshred8)
 */
public class GraphicsAPIHandler {

	public static void apiBegin(DrawModes mode) {
		drawMode = mode;
	}
	
	public static void apiEnable(Enable mode) {
		switch(mode) {
		case ARRAY_DRAWING:
			drawArrays = true;
			break;
		case LIGHT_DRAWING:
			lighting = true;
			break;
		default:
			break;
		}
	}
	
	public static void apiEnd() {
		lighting = false;
		drawArrays = false;
		drawMode = DrawModes.DRAW_NOTHING;
	}
	
	/**
	 * @return Returns the JFrame object
	 */
	public static JFrame apiGetWindow() {
		return frame;
	}
	
	/**
	 * @return Returns the Graphics2D object
	 */
	public static Graphics2D apiGetGraphics() {
		return g;
	}
	
}