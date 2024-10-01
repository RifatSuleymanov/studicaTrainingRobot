package frc.robot.gamepad;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
    Joystick drivePad;

    public OI() {
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
    }

    public double getRightDriveY() {
        double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
        if (Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    public double getRightDriveX() {
        double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_X);
        if (Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    public double getLeftDriveY() {
        double joy = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        if (Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    public double getLeftDriveX() {
        double joy = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_X);
        if (Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    public boolean getDriveRightTrigger() {
        return drivePad.getRawButton(GamepadConstants.RIGHT_TRIGGER);
    }
    public boolean getDriveRightBumper() {
        return drivePad.getRawButton(GamepadConstants.RIGHT_BUMPER);
    }
    public boolean getDriveLeftTrigger() {
        return drivePad.getRawButton(GamepadConstants.LEFT_TRIGGER);
    }
    public boolean getDriveLeftBumper() {
        return drivePad.getRawButton(GamepadConstants.LEFT_BUMPER);
    }
    public boolean getDriveDPadX() {
        return drivePad.getRawButton(GamepadConstants.DPAD_X);
    }
    public boolean getDriveDPadY() {
        return drivePad.getRawButton(GamepadConstants.DPAD_Y);
    }
    public boolean getDriveXButton() {
        return drivePad.getRawButton(GamepadConstants.SHARE_BUTTON);
    }
    public boolean getDriveYButton() {
        return drivePad.getRawButton(GamepadConstants.TRIANGLE_BUTTON);
    }
    public boolean getDriveBButton() {
        return drivePad.getRawButton(GamepadConstants.CIRCLE_BUTTON);
    }
    public boolean getDriveAButton() {
        return drivePad.getRawButton(GamepadConstants.X_BUTTON);
    }
    public boolean getDriveBackButton() {
        return drivePad.getRawButton(GamepadConstants.SHARE_BUTTON);
    }
    public boolean getDriveStartButton() {
        return drivePad.getRawButton(GamepadConstants.OPTIONS_BUTTON);
    }
    public boolean getDriveRightAnalogButton() {
        return drivePad.getRawButton(GamepadConstants.RIGHT_ANALOG_BUTTON);
    }

    public boolean getDriveLeftAnalogButton() {
        return drivePad.getRawButton(GamepadConstants.LEFT_ANALOG_BUTTON);
    }
}