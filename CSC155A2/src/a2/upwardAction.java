package a2;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class upwardAction extends AbstractAction{

	private static double amount = 0.0;
	Camera Cam;
	
	public upwardAction(Camera oldCamera)
	{
		Cam = oldCamera;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		amount = 1.0;
		Cam.setCamY(amount);
		//System.out.println("SUCESS");
	}
	
	public float upwardAdd()
	{
		if(amount == 1.0f)
		{
			amount = 0.0f;
			return 1.0f;
		}
		else
		{
			return 0.0f;
		}
	}

}
