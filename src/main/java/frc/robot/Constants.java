/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public final class Constants {

    public static final int TITAN_ID = 42;
    /**
     * Идентификатор CAN шины для контроллра TitanQuard
     */
    public static final int M0 = 3;
    /**
     * Идентификатор мотора правого колеса
     */
    public static final int M1 = 2;
    /**
     * Идентификатор мотора левого колеса
     */
    public static final int M3 = 1;
    /**
     * Идентификатор мотора заднего колеса
     */
    public static final double wheelRadius = 55;
    /**
     * Радиус колеса
     */
    public static final double pulsePerRevolution = 1440;
    /**
     * Количество пульсаций эндкодера на один полный оборот
     */
    public static final double gearRatio = 1 / 1;
    /**
     * Передаточное отношение между эндкодером и колесом
     */
    public static final double wheelPulseRatio = pulsePerRevolution * gearRatio;
    /**
     * Общее количество пульсаций на один оборот колеса
     */
    public static final double WHEEL_DIST_PER_TICK = (Math.PI * 2 * wheelRadius) / wheelPulseRatio;
    /**
     * Расстояние на один тик в миллиметрах
     */
    public static final int M2 = 0;
    /**
     * Идентификатор мотора лифта
     */
    public static final int DIF_SERVO = 0;
    /**
     * Идентификатор сервопривода
     */
    public static final double pulleyRadius = 7.85;
    /**
     * Радиус шкива в миллеметрах
     */
    public static final double pulsePerRevElevator = 1440;
    /**
     * Количество пульсаций эндкодера на один полный оборот
     */
    public static final double elevatorGearRatio = 2 / 1;
    /**
     * Передаточное отношение лифта
     */
    public static final double pulleyPulseRatio = pulsePerRevElevator * elevatorGearRatio;
    /**
     * Общее количество пульсаций на один оборот колеса
     */
    public static final double ELEVATOR_DIST_TICK = (Math.PI * 2 * pulleyRadius) / pulleyPulseRatio;
    /**
     * Расстояние на один тик в миллиметрах
     */
    public static final int SHARP1_ANALOG_CHANEL = 3;
    /**
     * Идентификатор порта для Sharp
     */
    public static final int SHARP2_ANALOG_CHANEL = 2;
    public static final int PING_CHANEL = 10;
    /**
     * Идентификатор порта ping для UltraSonic
     */
    public static final int ECHO_CHANEL = 11; /** Идентификатор порта echo для UltraSonic*/
}