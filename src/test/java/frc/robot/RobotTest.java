package frc.robot;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.sim.DriverStationSim;
import edu.wpi.first.hal.sim.PWMSim;
import edu.wpi.first.hal.sim.mockdata.DriverStationDataJNI;
import edu.wpi.first.wpilibj.DriverStation;

public class RobotTest {

    DriverStationSim driverStationSim = new DriverStationSim();
    PWMSim motor = new PWMSim(Constants.kShooterMotorId);
    

    @Before
    public void setup() throws Exception {
        // must call HAL.initialize to initialize the sim backend to work with joysticks
        HAL.initialize(500, 0);
        
        // Initialize joystick 1 with no buttons being pressed
        DriverStationDataJNI.setJoystickButtons((byte)Constants.kJoystickId, 0b000000000, 10);
        
        // Call this to initialize the DriverStation instance, which starts
        // a thread for querying joysticks
        DriverStation.getInstance();
        driverStationSim.notifyNewData();
        Thread.sleep(20);
    }

    @Test
    public void testTeleopPeriodic() throws Exception {
        // Configure the Sim DriverStation with one joystick, no buttons pressed
        DriverStationDataJNI.setJoystickButtons((byte)Constants.kJoystickId, 0b000000000, 10);
        // notify the DriverStation there is new data, and sleep a bit so the DriverStation thread
        // can pick up the notification and process it
        driverStationSim.notifyNewData();
        Thread.sleep(20);

        // auto close the robot when done
        try (Robot robot = new Robot()) {
            // enable the robot and call the init methods
            driverStationSim.setEnabled(true);
            robot.robotInit();
            robot.teleopInit();

            // call teleopPeriodic with no buttons pressed
            robot.robotPeriodic();
            robot.teleopPeriodic();

            // assert that our motor is not turned on
            assertEquals(0, motor.getSpeed(), 0);

            // turn on joystick button 1
            DriverStationDataJNI.setJoystickButtons((byte)Constants.kJoystickId, 0b000000001, 10);
            driverStationSim.notifyNewData();
            Thread.sleep(20);

            // go through a regular robot loop so the CommandScheduler is run
            robot.robotPeriodic();
            robot.teleopPeriodic();

            // With the button pressed, our command should fire and the 
            // motor should be set to 50%
            assertEquals(.5, motor.getSpeed(), 0);

            // turn off button 1 and verify our motor turns off
            DriverStationDataJNI.setJoystickButtons((byte)Constants.kJoystickId, 0b000000000, 10);
            driverStationSim.notifyNewData();
            Thread.sleep(20);

            // run through the robot loop once more and assert that our motor
            // was turned off
            robot.robotPeriodic();
            robot.teleopPeriodic();
            assertEquals(0, motor.getSpeed(), 0);
        }
    }

}