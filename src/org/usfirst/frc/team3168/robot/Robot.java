package org.usfirst.frc.team3168.robot;

import java.io.Console;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.SD540;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.XboxController;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String DefaultAuto = "Default";
	final String CustomAuto = "My Auto";
	String AutoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	Joystick LeftJoystick,RightJoystick;
	XboxController Xbox;
	Victor Right, Left, WinchMotor;
	Spark FlapperMotor;
	double yRight, yLeft;
	 
	boolean UseJoySticks =true; // Are we using Joysticks?/
	boolean UseXbox = !UseJoySticks; // Are we using the XBOX Controller?/
	boolean DebugMode = false,Go1,Go2;
	double DeadZone = 0.1; // Deadzone for the Joysticks and XBOX Controller/
	boolean Winch,FlapperGo,FlapperReturn,WinchReturn;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() //On Startup 
	{
		chooser.addDefault("Default Auto", DefaultAuto);
		chooser.addObject("My Auto", CustomAuto);
		edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData("Auto choices", chooser);
		
		Right = new Victor(0);
		Left = new Victor(1);
		FlapperMotor = new Spark(2);
		WinchMotor = new Victor(3);
	
		CameraServer.getInstance().startAutomaticCapture(); //Starts up video streaming
		if (UseJoySticks==true)
		{
			/**  Joy Sticks initalized here  */
			LeftJoystick = new Joystick(0);
			RightJoystick = new Joystick(1);
			DriverStation.reportWarning("Using Joysticks.",DebugMode);
		}else if (UseXbox==true) {
			Xbox= new XboxController(0);
			DriverStation.reportWarning("Set up XBOX Controllers. @NATHAN @ABHISHEK",DebugMode);
			}
			
		}
		
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//AutoSelected = chooser.getSelected();
		AutoSelected = edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.getString("Auto Selector",DefaultAuto);
		DriverStation.reportWarning("Auto selected: " + AutoSelected,DebugMode);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (AutoSelected) {
		case DefaultAuto:
		case CustomAuto:
		default:
			Right.set(.5f);
			Left.set(.5f);
			// Put custom auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		DriverStation.reportWarning("Teleop Periodic",DebugMode);
		Interface();
	}
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		Interface();
	}
	
	void Interface()
	{
		if (UseJoySticks==true){
		   // Used when config allows the usage of the Joysticks
		   yLeft = LeftJoystick.getY();
		   yRight = RightJoystick.getY();	
		   FlapperGo = RightJoystick.getRawButton(1);
		   FlapperReturn = RightJoystick.getRawButton(2);
		   Winch = LeftJoystick.getRawButton(1);
		  // WinchReturn = LeftJoystick.getRawButton(2);
		   Go1 = LeftJoystick.getRawButton(3);
		   Robot.SmartDashboard.PutString("Controller Type :","Joystick");
		   
		}else if (UseXbox==true && UseJoySticks==false) //Use when XBOX is allowed
		{
			
			yLeft = Xbox.getY(GenericHID.Hand.kLeft); //Fetches Left Joystick
			yRight =Xbox.getY(GenericHID.Hand.kRight); //Fetches Right Joystick
			FlapperReturn = Xbox.getBumper(GenericHID.Hand.kLeft); //Left Bumber Sets Go
			FlapperGo= Xbox.getTrigger(GenericHID.Hand.kLeft);
			WinchReturn = Xbox.getBumper(GenericHID.Hand.kRight); //Right Bumber reverses Winch
			Winch = Xbox.getTrigger(GenericHID.Hand.kRight);
			Robot.SmartDashboard.PutString("Controller Type :","XBOX");
		}
		if (Go1)
		{
			Left.setRaw(100);
		}
		if (Left.isAlive() == false || Right.isAlive() == false) 
		{
			DriverStation.reportError("Left : " + Left.isAlive() +"; Right : " + Right.isAlive(),DebugMode);
		}
		
		if (UseXbox==true || UseJoySticks == true) 
		{
			try{
			TankDrive(yLeft,yRight);  //Tank Drive Code
			OtherControls(FlapperGo, FlapperReturn, Winch, WinchReturn);  //Other Controls
			}
			catch (Exception ex)
			{
				DriverStation.reportWarning("Error: " + ex, DebugMode);
			}
		}
	}
	
	public void TankDrive(double yLeft,double yRight) //Tank Drive Controls
	{
		//DriverStation.reportWarning("TD Left: " + yLeft +" Right " + yRight, DebugMode);
		if(Math.abs(yRight) < DeadZone)
        {
        	Right.set(0);
        	//DriverStation.reportError("R NO GO", false);
        }
        else
        {
        	Right.set(yRight/2);
        	//DriverStation.reportError("R GO", false);
        }
		
      if(Math.abs(yLeft) < DeadZone)
        {
        	Left.set(0);
        	//DriverStation.reportError("L NO GO", false);
        }
        else
        {
        	Left.set(-yLeft/2);
        	//DriverStation.reportError("L GO", false);
        }
		
	}
	
	public void OtherControls(boolean FlapperGo,boolean FlapperReturn,boolean Winch,boolean WinchReturn) //Non Tank Drive Controls
	{
	      if(FlapperGo==true){FlapperMotor.set(.5);}
	      if(FlapperReturn==true){FlapperMotor.set(-0.5);}
	      if(Winch==true)
	      	{
	      		WinchMotor.set(-.33);
	      	}
	      if(Winch==false)
	      	{
	    	  	WinchMotor.set(0);
	      	}
	      //if(WinchReturn==true){WinchMotor.set(-.5);}
	      if(FlapperGo==false && FlapperReturn==false)
	      {
	    	  FlapperMotor.set(0); //Should turn off the Flapper Motor
	      }
	}
	
	private void when(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public static class SmartDashboard
	{
		public static void PutNumber(String Message,int Number)
		{
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(Message,Number);
			
		}
		public static void PutString(String Caption,String Message)
		{
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString(Caption,Message);
		}
		
	}
}

