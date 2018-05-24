package com.thehorriblehacker.graphics.v1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static com.thehorriblehacker.graphics.v1.GraphicsAPIInit.*;
import static com.thehorriblehacker.graphics.v1.GraphicsAPIHandler.*;

/**
 * This class is where all the rendering to the Canvas is done.
 * @author Brady Gunter (thehorriblehacker/bshred8)
 */

public class GraphicsAPIMain {
	
	/**
	 * Takes the internal arrays of the Graphics API and puts the vertices from them onto the screen.
	 * @return Returns <code>Return.DREW_ARRAYS</code> if no errors have been encountered AND <code>drawArrays</code> boolean is true. Set this by calling <code>apiEnable(Enable.DRAW_ARRAYS);</code>
	 */
	public static Return apiRenderArrays() {
		if(drawArrays) {
			apiRender(new Polygon(verticesX, verticesY, verticesX.length));
			apiClearArrays();
			return Return.DREW_ARRAYS;
		} else {
			return Return.DREW_NOTHING;
		}
	}
	
	private static float fadeInNum = 0;
	private static float fadeOutNum = 1;
	private static Color newColor;
	/**
	 * Renders the shape from the array input to the screen at a fading in or out rate until it is either equal to 1.0f or 0.0f.
	 * @param color The color you'd like your shape to be rendered as
	 * @param fadeRate The fade in or out rate. This number should be less than 1.0f and greater than 0
	 * @param fadeIn Whether or not the object is fading in or out. If this is <code>false</code>, then the object will fade out. If it is <code>true</code> then it will fade in.
	 * @return Returns <code>Return.DREW_ARRAYS</code> when arrays have been drawn and if <code>drawArrays</code> boolean is set to true. Set this to true by calling <code>apiEnable(Enable.ARRAY_DRAWING);</code>
	 */
	public static Return apiRenderArrays(Color color, float fadeRate, boolean fadeIn) {
		if(drawArrays) {
			if(fadeIn) {
				fadeInNum += fadeRate;
				if(fadeInNum >= 1.0f) fadeInNum = 1.0f;
				newColor = new Color(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, fadeInNum);
			} else {
				fadeOutNum -= fadeRate;
				if(fadeOutNum <= 0) fadeOutNum = 0;
				newColor = new Color(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, fadeOutNum);
			}
			apiSetColor(newColor);
			apiRender(new Polygon(verticesX, verticesY, verticesX.length));
			apiClearArrays();
			return Return.DREW_ARRAYS;
		} else {
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	 * Renders a rectangle by taking two triangles in the form of (X, Y).
	 * @param vertices Takes vertices of the rectangle you wish to render. These should be in the format of X and Y per vertex. This API renders rectangles in the form of two triangles. This means there should be a total of 12 elements, and not 8.
	 * @return Returns the result of <code>apiRenderTriangle();</code>. This result should be <code>Return.DREW_ARRAYS</code> if Array Drawing is enabled.
	 */
	public static Return apiRenderRectangle(int[] vertices) {
		int[] tempVertices = new int[vertices.length / 2];
		
		tempVertices[0] = vertices[0];
		tempVertices[1] = vertices[1];
		
		tempVertices[2] = vertices[2];
		tempVertices[3] = vertices[3];
		
		tempVertices[4] = vertices[4];
		tempVertices[5] = vertices[5];
		
		apiRenderTriangle(tempVertices);
		
		tempVertices[0] = vertices[6];
		tempVertices[1] = vertices[7];
		
		tempVertices[2] = vertices[8];
		tempVertices[3] = vertices[9];
		
		tempVertices[4] = vertices[10];
		tempVertices[5] = vertices[11];
		
		return apiRenderTriangle(tempVertices);
	}
	
	/**
	 * Renders a triangle from inputted vertices in (X, Y) format.
	 * @param vertices The vertices to be rendered to the screen. This array should contain a total of 6 elements, taking in X and Y for each vertex.
	 * @return returns the result of <code>apiRenderArrays();</code>. This is typically <code>Return.DREW_ARRAYS</code> if Array Drawing is enabled.
	 */
	public static Return apiRenderTriangle(int[] vertices) {
		verticesX[0] = vertices[0];
		verticesY[0] = vertices[1];
		
		verticesX[1] = vertices[2];
		verticesY[1] = vertices[3];
		
		verticesX[2] = vertices[4];
		verticesY[2] = vertices[5];
		
		return apiRenderArrays();
	}
	
	/**
	 * Adds a vertex to an automated index in the arrays. If more than 3 vertices are added, it will start over and overrite the previous.
	 * @param x The x position of the vertex
	 * @param y The y position of the vertex
	 * @return Returns <code>Return.NO_ERROR</code>
	 */
	private static int index = 0;
	public static Return apiVertex2i(int x, int y) {
		index += 1;
		if(index == 3) index = 0;
		verticesX[index] = x;
		verticesY[index] = y;
		return Return.NO_ERROR;
	}
	
	/**
	 * Renders an image to the screen by taking the path to the image and loading it for you.
	 * @param path The path to the image that is to be rendered. This should include folders and the file itself.
	 * @param x The top left horizontal position of the image.
	 * @param y The top left vertical position of the image.
	 * @return Returns <code>Return.ERROR</code> if <code>g = null</code>. Otherwise, it will return <code>Return.DREW_IMAGE</code> or <code>Return.DREW_NOTHING</code>
	 */
	public static Return apiRenderImage(String path, int x, int y) {
		if(g == null) return Return.ERROR;
		if(drawMode == DrawModes.DRAW_IMAGE) {
			try {
				g.drawImage(ImageIO.read(new File(path)), x, y, null);
				return Return.DREW_IMAGE;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Return.DREW_IMAGE;
		} else {
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	* Renders an inputted image at the X and Y coordinates.
	* @param image Takes the image to be rendered to the screen.
	* @param x the top left horizontal position of the image to be rendered.
	* @param y the top left vertical position of the image to be rendered.
	* @return Returns <code>Return.DREW_IMAGE</code> if <code>drawMode == DrawModes.DRAW_IMAGE</code>, otherwise <code>Return.DREW_NOTHING</code> Set this by calling upon <code>apiBegin();</code> in GraphicsAPIHandler
	*/
	public static Return apiRenderImage(Image image, int x, int y) {
		if(g == null) return Return.ERROR;
		if(drawMode == DrawModes.DRAW_IMAGE) {
			g.drawImage(image, x, y, null);
			return Return.DREW_IMAGE;
		} else {
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	* Renders the inputted image with the transform applied to it.
	* @param image The image to be rendered to the screen.
	* @param at The transformation of the image to be rendered to the screen. See <code>AffineTransform</code>
	* @return Returns <code>Return.DREW_IMAGE</code> if <code>drawMode == DrawModes.DRAW_IMAGE</code>, otherwise <code>Return.DREW_NOTHING</code> Set this by calling upon <code>apiBegin();</code> in GraphicsAPIHandler
	*/
	public static Return apiRenderImage(Image image, AffineTransform at) {
		if(g == null) return Return.ERROR;
		if(drawMode == DrawModes.DRAW_IMAGE) {
			g.drawImage(image, at, null);
			return Return.DREW_IMAGE;
		} else {
			return Return.DREW_NOTHING;
		}
	}
	
	/** 
	* Renders an image by loading it in through the path and applying an <code>AffineTransform</code> to it.
	* @param path The path to the image. This should include the image file itself. Example <code>"my/folder/image.png"</code>
	* @param at The transform to the applied to the image at rendering time.
	* @return Returns <code>Return.DREW_IMAGE</code> if <code>drawMode == DrawModes.DRAW_IMAGE</code>, otherwise, it will return <code>Return.DREW_NOTHING</code>.
	*/
	public static Return apiRenderImage(String path, AffineTransform at) {
		if(g == null) return Return.ERROR;
		if(drawMode == DrawModes.DRAW_IMAGE) {
			try {
				g.drawImage(ImageIO.read(new File(path)), at, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Return.DREW_IMAGE;
		} else {
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	* Clears the entire screen to a specified color.
	* @return returns <code>Return.DREW</code>
	*/
	public static Return apiClear(Color c) {
		if(g == null) return Return.ERROR;
		apiSetColor(c);
		apiBegin(DrawModes.DRAW_FILL);
		apiRenderRectangle(0, 0, frame.getWidth(), frame.getHeight());
		apiEnd();
		return Return.DREW;
	}
	
	/**
	* Shows the BufferStrategy to the JFrame.
	* @return Returns <code>Return.ERROR</code> if <code>bs == null</code>. Otherwise, it returns <code>Return.NO_ERROR</code>
	*/
	public static Return apiShow() {
		if(bs == null) {
			return Return.ERROR;
		} else {
			bs.show();
			return Return.NO_ERROR;
		}
	}
	
	/**
 	* Renders a specified shape to the screen. the shape is rendered depending on the <code>drawMode</code> that is set by calling <code>apiBegin();</code>
	* @param shape Takes in a shape that is to be rendered to the screen.
	* @return Returns either <code>Return.DREW</code> or <code>Return.DREW_NOTHING</code>
	*/
	public static Return apiRender(Shape shape) {
		switch(drawMode) {
		case DRAW_NOTHING:
			return Return.DREW_NOTHING;
		case DRAW_FILL:
			g.fill(shape);
			return Return.DREW;
		case DRAW_LINES:
			g.draw(shape);
			return Return.DREW;
		default:
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	* Renders a rectangle to the screen depending on an X, Y, Width, and Height variable.
	* @param x The top left X point of the rectangle that is to be rendered to the screen.
	* @param y The top left Y point of the rectangle that is to be rendered to the screen.
	* @param width The width of the rectangle that is to be rendered to the screen.
	* @param height The height of the rectangle that is to be rendered to the screen.
	* @return Returns either <code>Return.DREW_NOTHING</code> or <code>Return.DREW</code>
	*/
	public static Return apiRenderRectangle(int x, int y, int width, int height) {
		switch(drawMode) {
		case DRAW_NOTHING:
			return Return.DREW_NOTHING;
		case DRAW_FILL:
			g.fill(new Rectangle(x, y, width, height));
			return Return.DREW;
		case DRAW_LINES:
			g.draw(new Rectangle(x, y, width, height));
			return Return.DREW;
		default:
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	* Renders a polygon to the screen depending on a set of X and Y vertices.
	* @param verticesX The horizontal position of all vertices of the polygon to be rendered. Should correspond with verticesY.
	* @param verticesY The vertical position of all vertices of the polygon to be rendered. Should correspond with verticesX.
	* @return Returns either <code>Return.DREW_NOTHING</code> or <code>Return.DREW</code>
	*/
	public static Return apiRenderPolygon(int[] verticesX, int[] verticesY, int numVertices) {
		switch(drawMode) {
		case DRAW_NOTHING:
			return Return.DREW_NOTHING;
		case DRAW_FILL:
			g.fill(new Polygon(verticesX, verticesY, numVertices));
			return Return.DREW;
		case DRAW_LINES:
			g.draw(new Polygon(verticesX, verticesY, numVertices));
			return Return.DREW;
		default:
			return Return.DREW_NOTHING;
		}
	}
	
	/**
	* Set the color for further rendering of shapes. Does not affect images.
	* @param c The drawing color. Does nothing other than save this for further rendering.
	* @return Returns either <code>Return.ERROR</code> if <code>g == null</code> or <code>Return.NO_ERROR</code> otherwise.
	*/
	public static Return apiSetColor(Color c) {
		if(g == null) {
			return Return.ERROR;
		} else {
			g.setColor(c);
			return Return.NO_ERROR;
		}
	}
	
	/**
	* Upload a custom array. Takes ONLY triangles in (X, Y) format.
	* @param vertices The vertices to be uploaded to the internal arrays.
	* @return Returns <code>Return.NO_ERROR</code>
	*/
	public static Return apiUploadArrays(int[] vertices) {
		verticesX[0] = vertices[0];
		verticesY[0] = vertices[1];
		
		verticesX[1] = vertices[2];
		verticesY[1] = vertices[3];
		
		verticesX[2] = vertices[4];
		verticesY[2] = vertices[5];
		
		return Return.NO_ERROR;
	}
	
	/**
	* Renders text to the screen with a specified font, the text to be rendered, and the X and Y coordinate in which to render them.
	* @param font The font that the text is to be rendered in.
	* @param text The text that is to be rendered to the screen.
	* @param x The X position of the text to be rendered.
	* @param y The Y position of the text to be rendered.
	*/
	public static Return apiRenderText(Font font, String text, int x, int y) {
		g.setFont(font);
		g.drawString(text, x, y);
		return Return.DREW;
	}
	
}