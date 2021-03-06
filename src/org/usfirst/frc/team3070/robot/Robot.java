
package org.usfirst.frc.team3070.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	CANTalon lift1, lift2;
	Joystick xbox;
	DigitalInput upperLimit, lowerLimit, toteLimit;
//	TestLift lift;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	lift1 = new CANTalon(8);
    	lift2 = new CANTalon(9);
    	
    	xbox = new Joystick(1);
    	
    	upperLimit = new DigitalInput(1);
    	lowerLimit = new DigitalInput(2);
    	toteLimit = new DigitalInput(3);
    	
//    	lift = new TestLift(lift1, lift2, upperLimit, lowerLimit, toteLimit, xbox);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	lift1.set(xbox.getRawAxis(1));
    	lift2.set(-xbox.getRawAxis(1));
  
    	// for testing if limit switches are working
    	if (upperLimit.get()) {
    		//System.out.println("At Top");
    	}
    	
    	if (lowerLimit.get()) {
    		//System.out.println("At Bottom");
    	}
    	
    	if (!toteLimit.get()) {
    		System.out.println("Tote");
    	}
    	
        // lift.periodic();
    }
    
    public void disabledInit() {
//    	lift.stopPeriodic();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
