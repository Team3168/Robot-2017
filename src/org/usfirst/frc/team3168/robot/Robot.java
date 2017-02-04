package org.usfirst.frc.team3168.robot;

import java.io.Console;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.SD540;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
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
	Victor FrontLeft, FrontRight, BackLeft, BackRight,WinchMotor,FlapperMotor;
	double yRight, yLeft;
	 
	boolean UseJoySticks =true; // Are we using Joysticks?/
	boolean UseXbox = false; // Are we using the XBOX Controller?/
	boolean DebugMode = false;
	double DeadZone = 0.5; // Deadzone for the Joysticks and XBOX Controller/
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
		FrontLeft = new Victor(0);
		FrontRight = new Victor(1);
		BackLeft = new Victor(3);
		BackRight = new Victor(4);
		
		CameraServer.getInstance().startAutomaticCapture(); //Starts up video streaming
		
		if (UseJoySticks==true)
		{
			System.out.println("Using Joysticks");
			/**  Joy Sticks initalized here  */
			LeftJoystick = new Joystick(0);
			RightJoystick = new Joystick(1);
		}else if (UseXbox==true) {
			Xbox= new XboxController(0);
			System.out.println("Set up XBOX Controllers. @NATHAN @ABHISHEK");
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
		System.out.println("Auto selected: " + AutoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (AutoSelected) {
		case CustomAuto:
			FrontLeft.set(.5);
			FrontRight.set(.5);
			BackLeft.set(.5);
			BackRight.set(.5);
			// Put custom auto code here
			break;
		case DefaultAuto:
			FrontLeft.set(.5);
			FrontRight.set(.5);
			BackLeft.set(.5);
			BackRight.set(.5);
		default:
			FrontLeft.set(.5);
			FrontRight.set(.5);
			BackLeft.set(.5);
			BackRight.set(.5);
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		System.out.println("Teleop Periodic");
		if (UseJoySticks==true){
			// Used when config allows the usage of the Joysticks
			yLeft = LeftJoystick.getY();
		    yRight = -RightJoystick.getY();	
		   FlapperGo = RightJoystick.getRawButton(1);
		   FlapperReturn = RightJoystick.getRawButton(2);
		   Winch = LeftJoystick.getRawButton(1);
		   WinchReturn = LeftJoystick.getRawButton(2);
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
		
		if (UseXbox==true || UseJoySticks == true) 
		{
			TankDrive(yLeft,yRight);  //Tank Drive Code
			OtherControls(FlapperGo, FlapperReturn, Winch, WinchReturn);  //Other Controls
		}
	}
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		if (UseJoySticks==true){
			// Used when config allows the usage of the Joysticks
			yLeft = LeftJoystick.getY();
		    yRight = -RightJoystick.getY();	
		   FlapperGo = RightJoystick.getRawButton(1);
		   FlapperReturn = RightJoystick.getRawButton(2);
		   Winch = LeftJoystick.getRawButton(1);
		   WinchReturn = LeftJoystick.getRawButton(2);
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
		
		if (UseXbox==true || UseJoySticks == true) 
		{
		      TankDrive(yLeft,yRight); //Tank Drive Code
		      OtherControls(FlapperGo,FlapperReturn,Winch,WinchReturn); //Other Controls
		}
	}
	
	public void TankDrive(double yLeft,double yRight) //Tank Drive Controls
	{
		if(Math.abs(yLeft) < DeadZone)
        {
        	FrontLeft.set(0);BackLeft.set(0);
        }
        else
        {
        	FrontLeft.set(yLeft);BackLeft.set(yLeft);
        }
      if(Math.abs(yRight) < DeadZone)
        {
        	FrontRight.set(0);BackRight.set(0);
        }
        else
        {
        	FrontRight.set(yRight);BackRight.set(yRight);
        }
		
	}
	
	public void OtherControls(boolean FlapperGo,boolean FlapperReturn,boolean Winch,boolean WinchReturn) //Non Tank Drive Controls
	{
	      if(FlapperGo==true){FlapperMotor.set(.5);}
	      if(FlapperReturn==true){FlapperMotor.set(-0.5);}
	      if(Winch==true){WinchMotor.set(.5);}
	      if(WinchReturn==true){WinchMotor.set(-.5);}
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

