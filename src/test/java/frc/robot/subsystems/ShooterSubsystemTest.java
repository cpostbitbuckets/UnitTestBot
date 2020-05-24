package frc.robot.subsystems;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.first.hal.sim.PWMSim;
import frc.robot.Constants;

public class ShooterSubsystemTest {

    PWMSim shooterMotorSim = new PWMSim(Constants.kShooterMotorId);
    ShooterSubsystem shooterSubsystem;

    @Before
    public void before() {
        shooterSubsystem = new ShooterSubsystem();
        shooterMotorSim.resetData();
    }

    @After
    public void after() {
        shooterSubsystem.close();
    }

    @Test
    public void testFire() {
        shooterSubsystem.fire();
        assertEquals(.5, shooterMotorSim.getSpeed(), 0);
    }

    @Test
    public void testHoldFire() {
        shooterSubsystem.fire();
        assertEquals(.5, shooterMotorSim.getSpeed(), 0);

        shooterSubsystem.holdFire();
        assertEquals(0, shooterMotorSim.getSpeed(), 0);

    }

}