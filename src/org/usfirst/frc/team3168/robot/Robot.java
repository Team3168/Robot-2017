package org.usfirst.frc.team3168.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.SD540;
import edu.wpi.first.wpilibj.Talon;
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
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	Joystick leftJoystick,rightJoystick;
	XboxController Xbox;
	Talon frontLeft, frontRight, backLeft, backRight,WinchMotor,FlapperMotor;
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
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData("Auto choices", chooser);
		
		if (UseJoySticks==true)
		{
			System.out.println("Using Joysticks");
			/**  Joy Sticks initalized here  */
			leftJoystick = new Joystick(0);
			rightJoystick = new Joystick(1);
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
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			frontLeft.set(.5);
			frontRight.set(.5);
			backLeft.set(.5);
			backRight.set(.5);
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
	}
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		if (UseJoySticks==true){
			// Used when config allows the usage of the Joysticks
			yLeft = leftJoystick.getY();
		    yRight = -rightJoystick.getY();	
		   FlapperGo = rightJoystick.getRawButton(1);
		   FlapperReturn = rightJoystick.getRawButton(2);
		   Winch = leftJoystick.getRawButton(1);
		   WinchReturn = leftJoystick.getRawButton(2);
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
		
		if (UseXbox==true || UseJoySticks == true) //Tank Drive Code
		{
			  if(Math.abs(yLeft) < DeadZone)
		        {
		        	frontLeft.set(0);backLeft.set(0);
		        }
		        else
		        {
		        	frontLeft.set(yLeft);backLeft.set(yLeft);
		        }
		      if(Math.abs(yRight) < DeadZone)
		        {
		        	frontRight.set(0);backRight.set(0);
		        }
		        else
		        {
		        	frontRight.set(yRight);backRight.set(yRight);
		        }
		      
		      //Other Controls
		      if(FlapperGo==true){FlapperMotor.set(.5);}
		      if(FlapperReturn==true){FlapperMotor.set(-0.5);}
		      if(Winch==true){WinchMotor.set(.5);}
		      if(WinchReturn==true){WinchMotor.set(-.5);}
		}
	}
	
	public static class SmartDashboard
	{
		public static void PutNumber(String Message,int Number){
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(Message,Number);}
		public static void PutString(String Caption,String Message){
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString(Caption,Message);
		}
		
	}
}

