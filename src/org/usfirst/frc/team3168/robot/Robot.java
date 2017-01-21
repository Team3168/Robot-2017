package org.usfirst.frc.team3168.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SD540;
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
	SD540 frontLeft, frontRight, backLeft, backRight;
	double yRight, yLeft;
	
	boolean UseJoySticks =true;
	boolean UseXbox = false;
	boolean DebugMode = false;
	double DeadZone = 0.5;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		if (UseJoySticks==true)
		{
			System.out.println("Using Joysticks");
			/**  Joy Stick code here  */
			leftJoystick = new Joystick(0);
			rightJoystick = new Joystick(1);
		}else if (UseXbox==true) {
			System.out.println("Set up XBOX Controllers. @NATHAN @ABHISHEK");
			}
			
		}
		
		
		
	
	
	public static void robotMotors() {
		
		
	}
	
	public void robotJoysticks() {

	}
	
	public static void robotXBOX() {
		
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
			/** Used when config allows the usage of the Joysticks*/
			yLeft = leftJoystick.getY();
		    yRight = -rightJoystick.getY();
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
			
		}else if (UseXbox==true){
			
		}
	}
}

