package a2;

import graphicslib3D.*;
import java.io.*;
import java.nio.*;
import javax.swing.*;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_CCW;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Starter extends JFrame implements GLEventListener, KeyListener
{	private GLCanvas myCanvas;
	private int rendering_program;
	private int vao[] = new int[1];
	private int vbo[] = new int[10];

	private float sphLocX, sphLocY, sphLocZ;
	
	private GLSLUtils util = new GLSLUtils();
	private int earthTexture;
	private Texture joglEarthTexture;
	
	private int sunTexture;
	private Texture joglSunTexture;
	
	private int moonTexture;
	private Texture joglMoonTexture;
	
	private int customTexture;
	private Texture joglCustomTexture;
	
	private int redTexture;
	private Texture joglRedTexture;
	
	private int blueTexture;
	private Texture joglBlueTexture;
	
	private int greenTexture;
	private Texture joglGreenTexture;

	private Sphere mySphere = new Sphere(24);
	private	MatrixStack mvStack = new MatrixStack(20);
	Camera Cam = new Camera();
	
	forwardAction forwardListener = new forwardAction(Cam);
	backwardAction backwardListener = new backwardAction(Cam);
	leftAction leftListener = new leftAction(Cam);
	rightAction rightListener = new rightAction(Cam);
	upwardAction upwardListener = new upwardAction(Cam);
	downwardAction downwardListener = new downwardAction(Cam);
	
	upArrowAction upArrowListener = new upArrowAction(Cam);
	downArrowAction downArrowListener = new downArrowAction(Cam);
	leftArrowAction leftArrowListener = new leftArrowAction(Cam);
	rightArrowAction rightArrowListener = new rightArrowAction(Cam);
	
	spaceAction spaceListener = new spaceAction();
	private int mode = 0;

	private double degree = 3.0;
	private double planet1Degree = 0.0;
	private double planet2Degree = 15.0;
	private double moon1Degree = 10.0;
	private double moon2Degree = 20.0;
	private double moon3Degree = 30.0;
	private double moon4Degree = 40.0;
	
	private double planet1Rotation = 0.0f;
	private double planet2Rotation = 0.0f;
	private double moon1Rotation = 0.0f;
	private double moon2Rotation = 0.0f;
	private double moon3Rotation = 0.0f;
	private double moon4Rotation = 0.0f;
	
	private static double panDegreeUpDown = 0.0;
	private static double panDegreeRightLeft = 0.0;

	
	public Starter()
	{	setTitle("CSC155 Assignment#2 Steven Xiong");
		setSize(800, 800);
		myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		getContentPane().add(myCanvas);
		this.setVisible(true);
		
		// Codes to help with key press
		JComponent contentPane = (JComponent) this.getContentPane();
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = contentPane.getInputMap(mapName);
		
		
		
		
		// settings up 'w' key press	MOVE FORWARD
		KeyStroke wKey = KeyStroke.getKeyStroke('w');
		imap.put(wKey, "moveForward");
		
		
		ActionMap forwardKey = contentPane.getActionMap();
		forwardKey.put("moveForward", forwardListener);
		this.requestFocus();
		// end of the 'w' key press 
		
		// settings up 's' key press	MOVE BACKWARD
		KeyStroke sKey = KeyStroke.getKeyStroke('s');
		imap.put(sKey, "moveBackward");
		
		ActionMap backwardKey = contentPane.getActionMap();
		backwardKey.put("moveBackward", backwardListener);
		this.requestFocus();
		// end of the 's' key press
		 
		// setting up 'd' key press		MOVE RIGHT
		KeyStroke dKey = KeyStroke.getKeyStroke('d');
		imap.put(dKey, "moveRight");
		
		ActionMap rightKey = contentPane.getActionMap();
		rightKey.put("moveRight", rightListener);
		this.requestFocus();
		// end of the 'd' key press
		
		// setting up 'a' key press		MOVE LEFT
		KeyStroke aKey = KeyStroke.getKeyStroke('a');
		imap.put(aKey, "moveLeft");
		
		ActionMap leftKey = contentPane.getActionMap();
		leftKey.put("moveLeft", leftListener);
		this.requestFocus();
		// end of 'a' key press
		
		// setting up 'q' key press		MOVE UP
		KeyStroke qKey = KeyStroke.getKeyStroke('q');
		imap.put(qKey, "moveUp");
		
		ActionMap upKey = contentPane.getActionMap();
		upKey.put("moveUp", upwardListener);
		this.requestFocus();
		// end of 'q' key press
		
		// setting up 'e' key press		MOVE DOWN
		KeyStroke eKey = KeyStroke.getKeyStroke('e');
		imap.put(eKey, "moveDown");
		
		ActionMap downKey = contentPane.getActionMap();
		downKey.put("moveDown", downwardListener);
		this.requestFocus();
		// end of 'e' key press
		
		// setting up UPARROW key press
		KeyStroke upPanKey = KeyStroke.getKeyStroke("UP");
		imap.put(upPanKey, "panUp");
		
		ActionMap panUpKey = contentPane.getActionMap();
		panUpKey.put("panUp", upArrowListener);
		this.requestFocus();
		// end of UPARROW key press
		
		// setting up DOWNARROW key press
		KeyStroke downPanKey = KeyStroke.getKeyStroke("DOWN");
		imap.put(downPanKey, "panDown");
		
		ActionMap panDownKey = contentPane.getActionMap();
		panDownKey.put("panDown", downArrowListener);
		this.requestFocus();
		// end of DOWNARROW key press
		
		// setting up RIGHTARROW key press
		KeyStroke rightPanKey = KeyStroke.getKeyStroke("RIGHT");
		imap.put(rightPanKey, "panRight");
		
		ActionMap panRightKey = contentPane.getActionMap();
		panRightKey.put("panRight", rightArrowListener);
		this.requestFocus();
		// end of RIGHTARROW key press
		
		// setting up LEFTARROW key press
		KeyStroke leftPanKey = KeyStroke.getKeyStroke("LEFT");
		imap.put(leftPanKey, "panLeft");
		
		ActionMap panLeftKey = contentPane.getActionMap();
		panLeftKey.put("panLeft", leftArrowListener);
		this.requestFocus();
		// end of LEFTARROW key press
		
		// setting up SPACE key press
		KeyStroke spaceKey = KeyStroke.getKeyStroke("SPACE");
		imap.put(spaceKey, "spaceAxes");
		
		ActionMap spaceAxesKey = contentPane.getActionMap();
		spaceAxesKey.put("spaceAxes", spaceListener);
		this.requestFocus();
		// end of SPACE key press
		
		
		
		//System.out.println("Test Point#0");
		FPSAnimator animator = new FPSAnimator(myCanvas, 50);
		animator.start();
	}

	public void display(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glClear(GL_DEPTH_BUFFER_BIT);
		float bkg[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
		gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);
	
		gl.glClear(GL_DEPTH_BUFFER_BIT);

		gl.glUseProgram(rendering_program);

		int mv_loc = gl.glGetUniformLocation(rendering_program, "mv_matrix");
		int proj_loc = gl.glGetUniformLocation(rendering_program, "proj_matrix");

		float aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
		Matrix3D pMat = perspective(60.0f, aspect, 0.1f, 1000.0f);
		mvStack.pushMatrix();	// 1st push
		
		panDegreeUpDown = Cam.computeView(panDegreeUpDown, 1);
		panDegreeRightLeft = Cam.computeView(panDegreeRightLeft,2 );
		
		mvStack.rotate(panDegreeUpDown, 1.0, 0.0, 0.0);
		mvStack.rotate(panDegreeRightLeft, 0.0, 1.0, 0.0);
		
		mvStack.translate(-Cam.getCamX(), -Cam.getCamY(), -Cam.getCamZ());	
		mvStack.pushMatrix();	// 2nd push

		
		double amt = (double)(System.currentTimeMillis())/1000.0;
		gl.glUniformMatrix4fv(proj_loc, 1, false, pMat.getFloatValues(), 0);
		
	
		//---------- Z AXES
		if(spaceListener.axesMode() == 1)
		{
				mvStack.pushMatrix();
				mvStack.translate(0.0, 0.0, 0.0);
				
				gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
				gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
				gl.glEnableVertexAttribArray(0);
				
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
				gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
				gl.glEnableVertexAttribArray(0);
				
				// TEXTURE FUNCTION ONLY
				gl.glActiveTexture(GL_TEXTURE0);
				gl.glBindTexture(GL_TEXTURE_2D, blueTexture);
				

				gl.glEnable(GL_DEPTH_TEST);
				gl.glFrontFace(GL_CCW);
				//  END OF TEXTURE FUNCTINOS
				//System.out.println("Test Point#1");
				gl.glDrawArrays(GL_LINES, 0, 2);
				//gl.glDrawArrays(GL_TRIANGLES, 0, 24);
				mvStack.popMatrix();
				
				// ----------------- X AXES
				mvStack.pushMatrix();  //5th push
				mvStack.translate(0.0, 0.0, 0.0);
				gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
				gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
				gl.glEnableVertexAttribArray(0);

				
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
				gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
				gl.glEnableVertexAttribArray(0);
				
				// TEXTURE FUNCTION ONLY
				gl.glActiveTexture(GL_TEXTURE0);
				gl.glBindTexture(GL_TEXTURE_2D, redTexture);
				

				gl.glEnable(GL_DEPTH_TEST);
				gl.glFrontFace(GL_CCW);
				//  END OF TEXTURE FUNCTINOS
				//System.out.println("Test Point#1");
				gl.glDrawArrays(GL_LINES, 0, 2);
				//gl.glDrawArrays(GL_TRIANGLES, 0, 24);
				//mvStack.popMatrix();	// 6thd pop	
				mvStack.popMatrix();	// 5thd pop	
				
				// ----------------- Y AXES
				mvStack.pushMatrix();  //5th push
				mvStack.translate(0.0, 0.0, 0.0);
				gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
				gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
				gl.glEnableVertexAttribArray(0);

				
				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
				gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
				gl.glEnableVertexAttribArray(0);
				
				// TEXTURE FUNCTION ONLY
				gl.glActiveTexture(GL_TEXTURE0);
				gl.glBindTexture(GL_TEXTURE_2D, greenTexture);
				

				gl.glEnable(GL_DEPTH_TEST);
				gl.glFrontFace(GL_CCW);
				//  END OF TEXTURE FUNCTINOS
				//System.out.println("Test Point#1");
				gl.glDrawArrays(GL_LINES, 0, 2);
				//gl.glDrawArrays(GL_TRIANGLES, 0, 24);
				mvStack.popMatrix();	// 5thd pop	
		}
		
		// ----------------- SUN  PLANET
		mvStack.translate(sphLocX, sphLocY, sphLocZ);
		mvStack.pushMatrix();	// 3rd push
		degree += 1.0;
	
		//mvStack.rotate((System.currentTimeMillis()*0.5)/5.0,0.5,1.0,0.0);
		mvStack.rotate(degree,0.5,0.5,0.0);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);
		
		// TEXTURE FUNCTION ONLY
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, sunTexture);
		

		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#1");
		int numVerts = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVerts);
		mvStack.popMatrix();	// 3rd pop
		
		// ----------------- earth planet
		planet1Degree += 0.7;
		planet1Rotation += 0.02;
		mvStack.pushMatrix();	// 3rd push
		mvStack.translate(Math.sin(planet1Rotation)*4.0f, 0.0f, Math.cos(planet1Rotation)*3.0f);
		mvStack.pushMatrix();	// 4td push
		mvStack.rotate(planet1Degree,-0.7,0.4,0.0);
		mvStack.scale(0.55, 0.55, 0.55);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		
		// TEXTURE FUNCTION ONLY
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, earthTexture);
		

		//gl.glEnable(GL_CULL_FACE);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#11");
		int numVertsEarth = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsEarth);
		mvStack.popMatrix();	// 4th pop
		
		// -------------- moon planet
		moon1Degree += 0.4;
		moon1Rotation += 0.04;
		mvStack.pushMatrix();   //4th push
		mvStack.translate(0.0f, Math.sin(moon1Rotation)*1.0f, Math.cos(moon1Rotation)*1.0f);
		mvStack.rotate(moon1Degree,0.4,-0.2,0.0);
		mvStack.scale(0.25, 0.25, 0.25);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
		

		//gl.glEnable(GL_CULL_FACE);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#12");
		int numVertsMoon = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsMoon);
		mvStack.popMatrix();	// 4th pop
		
		// -------------- 2nd moon planet
		moon2Degree += 0.6;
		moon2Rotation += 0.04;
		mvStack.pushMatrix();   //4th push
		mvStack.translate(Math.sin(moon2Rotation)*1.3f, Math.sin(moon2Rotation)*1.3f, Math.cos(moon2Rotation)*1.3f);
		mvStack.rotate(moon2Degree,-0.4,0.4,0.0);
		mvStack.scale(0.25, 0.25, 0.25);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
		

		//gl.glEnable(GL_CULL_FACE);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#13");
		int numVertsMoon2 = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsMoon2);
		mvStack.popMatrix();	// 4th pop
		mvStack.popMatrix();	// 1st pop
		
		
		// ---------------- 2nd planet
		
		planet2Degree += 1.5;
		planet2Rotation += 0.035;
		mvStack.pushMatrix();	// 4td push
		mvStack.translate(-Math.cos(planet2Rotation)*4.5f, Math.sin(planet2Rotation)*4.0f, Math.cos(planet2Rotation)*0.5f);
		mvStack.pushMatrix();	// 5th push
		mvStack.rotate(planet2Degree,-0.7,0.4,0.0);
		mvStack.scale(0.5, 0.5, 0.5);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		
		// TEXTURE FUNCTION ONLY
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, earthTexture);
		

		//gl.glEnable(GL_CULL_FACE);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#11");
		int numVertsEarth2 = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsEarth2);
		mvStack.popMatrix();	// 5th pop
		
		// -------------- 3rd moon planet
		moon3Degree += 0.2;
		moon3Rotation += 0.04;
		mvStack.pushMatrix();   //5th push
		mvStack.translate(Math.cos(moon3Rotation)*1.9f,Math.sin(moon3Rotation)*1.f, Math.cos(moon3Rotation)*0.7f);
		mvStack.rotate(moon3Degree,0.5,0.3,1.0);
		mvStack.scale(0.25, 0.25, 0.25);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
		

		//gl.glEnable(GL_CULL_FACE);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#12");
		int numVertsMoon3 = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsMoon3);
		mvStack.popMatrix();	// 5th pop
		
		// -------------- 4th moon planet
		moon4Degree += 3.7;
		moon4Rotation += 0.03;
		mvStack.pushMatrix();	//5th push
		mvStack.translate(-Math.sin(moon4Rotation)*1.8f, Math.sin(moon4Rotation)*0.7f, -Math.cos(moon4Rotation)*1.3f);
		mvStack.rotate(moon4Degree,0.4,-0.2,1.0);
		mvStack.scale(0.25, 0.25, 0.25);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);
		
		// TEXTURE FUNCTION ONLY
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, customTexture);
		

		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		//System.out.println("Test Point#1");
		
		gl.glDrawArrays(GL_TRIANGLES, 0, 24);
		mvStack.popMatrix();	// 5thd pop	
		mvStack.popMatrix(); 	 // 4th 
		mvStack.popMatrix();	// 3rd pop
		mvStack.popMatrix();	// 2nd pop
		
		

		
	}

	public void init(GLAutoDrawable drawable)
	{	
		//System.out.println("Test Point#2");
		GL4 gl = (GL4) GLContext.getCurrentGL();
		rendering_program = createShaderProgram();
		setupVertices();
		//cameraX = 5.0f; cameraY = 5.0f; cameraZ = 5.0f;
		sphLocX = 5.0f; sphLocY = 5.0f; sphLocZ = 5.0f;
		//earthLocX = 0.0f; earthLocY = 0.0f; earthLocZ = 0.0f;
		//moonLocX = 0.0f; moonLocY = 0.0f; moonLocZ = 0.0f;
		
		joglMoonTexture = loadTexture("moon.jpg");
		moonTexture = joglMoonTexture.getTextureObject();
		
		joglEarthTexture = loadTexture("earth.jpg");
		earthTexture = joglEarthTexture.getTextureObject();
		
		joglSunTexture = loadTexture("sun.jpg");
		sunTexture = joglSunTexture.getTextureObject();
		
		joglCustomTexture = loadTexture("steven.jpg");
		customTexture = joglCustomTexture.getTextureObject();
		
		joglRedTexture = loadTexture("red.jpg");
		redTexture = joglRedTexture.getTextureObject();
		
		joglBlueTexture = loadTexture("Blue.jpg");
		blueTexture = joglBlueTexture.getTextureObject();

		joglGreenTexture = loadTexture("green.jpg");
		greenTexture = joglGreenTexture.getTextureObject();
		
		
	}

	private void setupVertices()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		//System.out.println("Test Point#3");
		Vertex3D[] vertices = mySphere.getVertices();
		int[] indices = mySphere.getIndices();
		
		float[] pvalues = new float[indices.length*3];
		float[] tvalues = new float[indices.length*2];
		float[] nvalues = new float[indices.length*3];
		
		for (int i=0; i<indices.length; i++)
		{	pvalues[i*3] = (float) (vertices[indices[i]]).getX();
			pvalues[i*3+1] = (float) (vertices[indices[i]]).getY();
			pvalues[i*3+2] = (float) (vertices[indices[i]]).getZ();
			tvalues[i*2] = (float) (vertices[indices[i]]).getS();
			tvalues[i*2+1] = (float) (vertices[indices[i]]).getT();
			nvalues[i*3] = (float) (vertices[indices[i]]).getNormalX();
			nvalues[i*3+1]= (float)(vertices[indices[i]]).getNormalY();
			nvalues[i*3+2]=(float) (vertices[indices[i]]).getNormalZ();
		}
		
		float[] diamond_positions =
		{	
				
			// upside pyramid 4 triangles
			-1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 4.0f,
			-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 4.0f, 
			1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 4.0f, 
			1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 4.0f, 
			
			
			// upside down pyramid 4 triangles
			-1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -4.0f,
			-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -4.0f, 
			1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -4.0f, 
			1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -4.0f, 
		};
		
		float[] texture_coordinates =
		{	
			// upside pyramid 4 triangles
			0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
			
			// upside down pyramid 4 triangles
			0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f
			
		
		};
		
		float[] lineZ_coordinates =
		{
				
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 30.0f	
				
			/*	
			0.1f, 0.0f, 0.0f, 0.1f, 0.1f, 0.0f, 0.1f, 0.1f, 20.0f,
			0.1f, 0.0f, 0.0f, 0.1f, 0.1f, 20.0f, 0.1f, 0.0f, 20.0f, 
			
			0.0f, 0.1f, 0.0f, 0.1f, 0.1f, 0.0f, 0.0f, 0.1f, 20.0f, 
			0.1f, 0.1f, 0.0f, 0.0f, 0.1f, 20.0f, 0.1f, 0.1f, 20.0f, 
			
			0.0f, 0.1f, 0.0f, 0.0f, 0.1f, 20.0f, 0.0f, 0.0f, 0.0f, 
			0.0f, 0.0f, 20.0f, 0.0f, 0.1f, 20.0f, 0.0f, 0.0f, 0.0f, 
			
			0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 20.0f, 
			0.0f, 0.0f, 20.0f, 0.1f, 0.0f, 20.0f, 0.0f, 0.0f, 0.0f
			*/
		};
		
		float[] lineX_coordinates =
		{
			0.0f, 0.0f, 0.0f, 30.0f, 0.0f, 0.0f
			/*
			20.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.1f, 
			20.0f, 0.0f, 0.1f, 20.0f, 0.1f, 0.1f, 0.0f, 0.1f, 0.1f, 
			
			20.0f, 0.1f, 0.1f, 0.0f, 0.1f, 0.1f, 0.0f, 0.1f, 0.0f, 
			20.0f, 0.1f, 0.1f, 20.0f, 0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 
			
			0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 20.0f, 0.0f, 0.0f, 
			20.0f, 0.0f, 0.0f, 20.0f, 0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 
			
			0.0f, 0.0f, 0.1f, 20.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 
			20.0f, 0.0f, 0.1f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
			*/
		};
		
		float[] lineY_coordinates =
		{
			0.0f, 0.0f, 0.0f, 0.0f, 30.0f, 0.0f
			/*
			0.1f, 0.0f, 0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 20.0f, 0.1f, 
			0.1f, 0.0f, 0.1f, 0.1f, 20.0f, 0.0f, 0.0f, 20.0f, 0.1f, 
			
			0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.1f, 20.0f, 0.1f, 
			0.1f, 0.0f, 0.0f, 0.1f, 20.0f, 0.0f, 0.1f, 20.0f, 0.1f, 
			
			0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 0.0f, 
			0.1f, 0.0f, 0.0f, 0.1f, 20.0f, 0.0f, 0.0f, 20.0f, 0.0f, 
			
			0.0f, 0.0f, 0.1f, 0.0f, 20.0f, 0.1f, 0.0f, 0.0f, 0.0f, 
			0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 0.0f, 0.0f, 20.0f, 0.1f
			*/
		};
		
		float[] line_texture =
		{
				0.0f, 0.0f, 1.0f, 0.0f
				/*
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f 
				*/
		};
	
		gl.glGenVertexArrays(vao.length, vao, 0);
		gl.glBindVertexArray(vao[0]);
		gl.glGenBuffers(10, vbo, 0);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		FloatBuffer vertBuf = Buffers.newDirectFloatBuffer(pvalues);
		gl.glBufferData(GL_ARRAY_BUFFER, vertBuf.limit()*4, vertBuf, GL_STATIC_DRAW);

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		FloatBuffer texBuf = Buffers.newDirectFloatBuffer(tvalues);
		gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit()*4, texBuf, GL_STATIC_DRAW);

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		FloatBuffer norBuf = Buffers.newDirectFloatBuffer(nvalues);
		gl.glBufferData(GL_ARRAY_BUFFER, norBuf.limit()*4,norBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
		FloatBuffer diamondBuf = Buffers.newDirectFloatBuffer(diamond_positions);
		gl.glBufferData(GL_ARRAY_BUFFER, diamondBuf.limit()*4, diamondBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
		FloatBuffer textureBuf = Buffers.newDirectFloatBuffer(diamond_positions);
		gl.glBufferData(GL_ARRAY_BUFFER, textureBuf.limit()*4, textureBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
		FloatBuffer xBuf = Buffers.newDirectFloatBuffer(lineX_coordinates);
		gl.glBufferData(GL_ARRAY_BUFFER, xBuf.limit()*4, xBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
		FloatBuffer zBuf = Buffers.newDirectFloatBuffer(lineZ_coordinates);
		gl.glBufferData(GL_ARRAY_BUFFER, zBuf.limit()*4, zBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
		FloatBuffer yBuf = Buffers.newDirectFloatBuffer(lineY_coordinates);
		gl.glBufferData(GL_ARRAY_BUFFER, yBuf.limit()*4, yBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
		FloatBuffer lineTextureBuf = Buffers.newDirectFloatBuffer(line_texture);
		gl.glBufferData(GL_ARRAY_BUFFER, lineTextureBuf.limit()*4, lineTextureBuf, GL_STATIC_DRAW);
		
		/*
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
		FloatBuffer cubeBuf = Buffers.newDirectFloatBuffer(diamond_positions);
		gl.glBufferData(GL_ARRAY_BUFFER, cubeBuf.limit()*4, cubeBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
		FloatBuffer texBufs = Buffers.newDirectFloatBuffer(texture_coordinates);
		gl.glBufferData(GL_ARRAY_BUFFER, texBufs.limit()*4, texBufs, GL_STATIC_DRAW);
		*/
	}

	private Matrix3D perspective(float fovy, float aspect, float n, float f)
	{	float q = 1.0f / ((float) Math.tan(Math.toRadians(0.5f * fovy)));
		float A = q / aspect;
		float B = (n + f) / (n - f);
		float C = (2.0f * n * f) / (n - f);
		Matrix3D r = new Matrix3D();
		r.setElementAt(0,0,A);
		r.setElementAt(1,1,q);
		r.setElementAt(2,2,B);
		r.setElementAt(3,2,-1.0f);
		r.setElementAt(2,3,C);
		r.setElementAt(3,3,0.0f);
		return r;
	}

	public static void main(String[] args) { new Starter(); }
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	public void dispose(GLAutoDrawable drawable) {}

	private int createShaderProgram()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
	//System.out.println("Test Point#4");
		String vshaderSource[] = util.readShaderSource("vert.shader");
		String fshaderSource[] = util.readShaderSource("frag.shader");

		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

		gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
		gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);
		gl.glCompileShader(vShader);
		gl.glCompileShader(fShader);

		int vfprogram = gl.glCreateProgram();
		gl.glAttachShader(vfprogram, vShader);
		gl.glAttachShader(vfprogram, fShader);
		gl.glLinkProgram(vfprogram);
		return vfprogram;
	}

	public Texture loadTexture(String textureFileName)
	{	Texture tex = null;
		try { tex = TextureIO.newTexture(new File(textureFileName), false); }
		catch (Exception e) { e.printStackTrace(); }
		return tex;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Key pressed = " + e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

/*
//----------------- moon planet
		mvStack.pushMatrix();	// 4th push
		mvStack.translate(earthLocX, earthLocY, earthLocZ);
		mvStack.pushMatrix();	// 5th push
		
		mvStack.translate(Math.sin(amt)*4.0f, 0.0f, Math.cos(amt)*4.0f);
		mvStack.rotate((System.currentTimeMillis()*10.0)/5.0,0.2,0.3,0.0);
		mvStack.scale(0.25, 0.25, 0.25);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);


		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);
		
		// TEXTURE FUNCTION ONLY
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
		

		gl.glEnable(GL_CULL_FACE);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		System.out.println("Test Point#11");
		int numVertsMoon = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsMoon);
		mvStack.popMatrix();	// 5th pop
		mvStack.popMatrix();	// 4th pop
		
		
----- 2nd moon
mvStack.pushMatrix();   //5th push
		mvStack.translate(-Math.sin(amt)*1.8f, 0.2f, Math.cos(amt)*0.7f);
		mvStack.rotate((System.currentTimeMillis())/20.0,0.4,0.2,1.0);
		mvStack.scale(0.25, 0.25, 0.25);
		gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
		

		//gl.glEnable(GL_CULL_FACE);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_CCW);
		//  END OF TEXTURE FUNCTINOS
		System.out.println("Test Point#13");
		int numVertsMoon4 = mySphere.getIndices().length;
		gl.glDrawArrays(GL_TRIANGLES, 0, numVertsMoon4);
		mvStack.popMatrix();	// 5th pop		
		

*/