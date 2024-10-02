package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
    private Main() {
    }

    /**
     * Запуск робота создавая новый экземпляр класса Robot
     */
    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }
}