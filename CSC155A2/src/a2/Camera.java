package a2;

import graphicslib3D.Matrix3D;
import graphicslib3D.MatrixStack;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class Camera {

	private static Point3D Cam_loc = new Point3D(5, 5, 15);
	
	private Vector3D U = new Vector3D(1, 0, 0);
	private Vector3D V = new Vector3D(0, 1, 0);
	private Vector3D N = new Vector3D(0, 0, 1);
	
	private Matrix3D U_rot = new Matrix3D();
	private Matrix3D V_rot = new Matrix3D();
	private Matrix3D N_rot = new Matrix3D();
	
	private static int mode = 0;
	
	private double amount = 1.0;
	protected double panAmount = 2.0;
	
	public Camera()
	{
		
	}
	
	public int cameraMode()
	{
		return mode;
	}
	

	
	public void setMode(int currentMode)
	{
		mode = currentMode;
	}
	
	public double getCamX()
	{
		return Cam_loc.getX();
	}
	
	public double getCamY()
	{
		return Cam_loc.getY();
	}
	
	public double getCamZ()
	{
		return Cam_loc.getZ();
	}
	
	public void setCamX(double x)
	{
		Point3D U_mov = new Point3D (U.normalize());
		U_mov = U_mov.mult(x);
		Cam_loc = Cam_loc.add(U_mov);
		//Cam_loc.setX(Cam_loc.getX() +x);
	}
	
	public void setCamY(double y)
	{
		Point3D V_mov = new Point3D(V.normalize());
		V_mov = V_mov.mult(y);
		Cam_loc = Cam_loc.add(V_mov);
		//Cam_loc.setY(Cam_loc.getY() + y);
	}
	
	public void setCamZ(double z)
	{
		Point3D N_mov = new Point3D(N.normalize());
		N_mov = N_mov.mult(z);
		Cam_loc = Cam_loc.add(N_mov);
		//Cam_loc.setZ(Cam_loc.getZ() + z);
	}
	
	public double computeView(double panDegree, int active)
	{
		if(mode == 1 && active == 1)
		{
			mode = 0;
			panDegree -= panAmount;
			
		}
		else if(mode == 2 && active == 1)
		{
			mode = 0;
			panDegree += panAmount;
		}
		else if(mode == 3 && active == 2)
		{
			mode = 0;
			panDegree += panAmount;
		}
		else if(mode == 4 && active == 2)
		{
			mode = 0;
			panDegree -= panAmount;
		}
		return panDegree;
	}

}
