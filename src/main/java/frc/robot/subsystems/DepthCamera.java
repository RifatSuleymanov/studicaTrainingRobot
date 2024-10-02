package frc.robot.subsystems;

import com.orbbec.obsensor.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Класс DepthCamera, наследующий от SubsystemBase*/
public class DepthCamera extends SubsystemBase {
    private OBContext obContext; // Контекст для работы с камерой
    private final Object pipeLock = new Object(); // Объект для синхронизации потоков
    private Pipeline pipeline; // Конвейер для обработки данных с камеры

    // Конструктор класса DepthCamera
    public DepthCamera() {
        initCamera(); // Инициализация камеры
    }

    /** Метод для инициализации камеры*/
    private void initCamera() {
        // Создание контекста с коллбэком на изменение устройства
        obContext = new OBContext(new DeviceChangedCallback() {
            @Override
            public void onDeviceAttach(DeviceList deviceList) {
                initPipeline(deviceList); // Инициализация конвейера для подключенных устройств
                startStreams(); // Запуск потоков данных
                deviceList.close(); // Закрытие списка устройств
            }

            @Override
            public void onDeviceDetach(DeviceList deviceList) {
                stopStreams(); // Остановка потоков данных
                deInitPipeline(); // Освобождение конвейера
                deviceList.close(); // Закрытие списка устройств
            }
        });

        /** Запрос списка устройств*/
        DeviceList deviceList = obContext.queryDevices();
        if (null != deviceList) {
            // Если устройства подключены
            if (deviceList.getDeviceCount() > 0) {
                initPipeline(deviceList); // Инициализация конвейера
                startStreams(); // Запуск потоков данных
            }
            deviceList.close(); // Закрытие списка устройств
        }
    }

    /** Метод для освобождения камеры*/
    private void destroyCamera() {
        try {
            // Закрытие контекста, если он существует
            if (null != obContext) {
                obContext.close(); // Закрытие контекста
                obContext = null; // Удаление ссылки на контекст
            }
        } catch (Exception e) {
            e.printStackTrace(); // Вывод информации об ошибке
        }
    }

    /** Метод для инициализации конвейера */
    private void initPipeline(DeviceList deviceList) {
        synchronized (pipeLock) // Синхронизация доступа к конвейеру
        {
            if (null != pipeline) {
                pipeline.close(); // Закрытие предыдущего конвейера
            }

            try (Device device = deviceList.getDevice(0)) // Получение первого подключенного устройства
            {
                pipeline = new Pipeline(device); // Создание нового конвейера
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }
        }
    }

    /** Метод для освобождения конвейера*/
    private void deInitPipeline() {
        synchronized (pipeLock) // Синхронизация доступа к конвейеру
        {
            try {
                if (null != pipeline) {
                    pipeline.close(); // Закрытие конвейера
                    pipeline = null; // Удаление ссылки на конвейер
                }
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }
        }
    }

    /** Метод для запуска потоков */
    public void startStreams() {
        synchronized (pipeLock) // Синхронизация доступа к конвейеру
        {
            if (null == pipeline) // Проверка, инициализирован ли конвейер
            {
                System.out.println("Ошибка камеры! Устройство не подключено!"); // Вывод сообщения об ошибке
                return; // Выход из метода
            }
            Config config = new Config(); // Создание конфигурации

            // Настройка потоков для цветного изображения
            try (StreamProfileList colorProfileList = pipeline.getStreamProfileList(SensorType.COLOR)) {
                VideoStreamProfile colorProfile = colorProfileList.getStreamProfile(0).as(StreamType.VIDEO); // Получение профиля цветного потока
                if (null == colorProfile) {
                    throw new OBException("Не удалось получить профиль цветного потока, подходящий профиль не найден!"); // Исключение при ошибке
                }
                printVideoStreamProfile(colorProfile, SensorType.COLOR); // Вывод информации о профиле цветного потока
                config.enableStream(colorProfile); // Включение цветного потока
                colorProfile.close(); // Закрытие профиля цветного потока
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }

            // Настройка потоков для глубины
            try (StreamProfileList depthProfileList = pipeline.getStreamProfileList(SensorType.DEPTH)) {
                VideoStreamProfile depthProfile = depthProfileList.getStreamProfile(0).as(StreamType.VIDEO); // Получение профиля потока глубины
                if (null == depthProfile) {
                    throw new OBException("Не удалось получить профиль потока глубины, подходящий профиль не найден!"); // Исключение при ошибке
                }
                printVideoStreamProfile(depthProfile, SensorType.DEPTH); // Вывод информации о профиле потока глубины
                config.enableStream(depthProfile); // Включение потока глубины
                depthProfile.close(); // Закрытие профиля потока глубины
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }

            // Настройка потоков для ИК
            try (StreamProfileList irProfileList = pipeline.getStreamProfileList(SensorType.IR)) {
                VideoStreamProfile irProfile = irProfileList.getStreamProfile(0).as(StreamType.VIDEO); // Получение профиля ИК потока
                if (null == irProfile) {
                    throw new OBException("Не удалось получить профиль ИК потока, подходящий профиль не найден!"); // Исключение при ошибке
                }
                printVideoStreamProfile(irProfile, SensorType.IR); // Вывод информации о профиле ИК потока
                config.enableStream(irProfile); // Включение ИК потока
                irProfile.close(); // Закрытие профиля ИК потока
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }

            // Запуск потоков с передачей коллбэка для обработки наборов кадров
            try {
                pipeline.start(config, new FrameSetCallback() {
                    @Override
                    public void onFrameSet(FrameSet frameSet) {
                        processFrameSet(frameSet); // Обработка набора кадров
                        frameSet.close(); // Закрытие набора кадров
                    }
                });
                config.close(); // Закрытие конфигурации
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }
        }
    }

    /** Метод для остановки потоков*/
    public void stopStreams() {
        synchronized (pipeLock) // Синхронизация доступа к конвейеру
        {
            try {
                if (null != pipeline) // Проверка, инициализирован ли конвейер
                {
                    pipeline.stop(); // Остановка потоков
                }
            } catch (Exception e) {
                e.printStackTrace(); // Вывод информации об ошибке
            }
        }
    }

    /** Метод для вывода информации о профиле видео потока*/
    private void printVideoStreamProfile(VideoStreamProfile vsp, SensorType type) {
        StringBuilder sb = new StringBuilder()
                .append(" StreamType: " + vsp.getType() + "\n")
                .append(" Width: " + vsp.getWidth() + "\n")
                .append(" Height: " + vsp.getHeight() + "\n")
                .append(" FrameRate: " + vsp.getFrameRate() + "\n")
                .append(" Format: " + vsp.getFormat() + "\n");
        // Вывод информации о профиле в SmartDashboard в зависимости от типа сенсора
        if (type == SensorType.COLOR)
            SmartDashboard.putString("Профиль видео потока (цвет)", sb.toString());
        if (type == SensorType.DEPTH)
            SmartDashboard.putString("Профиль видео потока (глубина)", sb.toString());
        if (type == SensorType.IR)
            SmartDashboard.putString("Профиль видео потока (ИК)", sb.toString());
    }

    /** Метод для вывода информации о видео кадре */
    private void printVideoFrame(VideoFrame vf, SensorType type) {
        StringBuilder sb = new StringBuilder()
                .append("FrameType: " + vf.getStreamType() + "\n")
                .append(" Width: " + vf.getWidth() + "\n")
                .append(" Height: " + vf.getHeight() + "\n")
                .append(" Format: " + vf.getFormat() + "\n")
                .append(" Index: " + vf.getFrameIndex() + "\n")
                .append(" Timestamp: " + vf.getTimeStamp() + "\n")
                .append(" SysTimestamp: " + vf.getSystemTimeStamp() + "\n");
        // Вывод информации о кадре в SmartDashboard в зависимости от типа сенсора
        if (type == SensorType.COLOR)
            SmartDashboard.putString("Кадр видео (цвет)", sb.toString());
        if (type == SensorType.DEPTH)
            SmartDashboard.putString("Кадр видео (глубина)", sb.toString());
        if (type == SensorType.IR)
            SmartDashboard.putString("Кадр видео (ИК)", sb.toString());
    }

    /** Метод для обработки кадра глубины*/
    private void processDepthFrame(DepthFrame frame) {
        int height = frame.getWidth(); // Получение высоты кадра
        int width = frame.getHeight(); // Получение ширины кадра

        int x = width / 2; // Координата X в центре кадра
        int y = height / 2; // Координата Y в центре кадра

        double fov_w = 79.0; // Ширина поля зрения
        double fov_h = 62.0; // Высота поля зрения

        byte[] frameData = new byte[frame.getDataSize()]; // Массив для хранения данных кадра
        frame.getData(frameData); // Получение данных кадра

        int depthD1 = frameData[y * width + x]; // Данные глубины в центре кадра
        int depthD2 = frameData[y * width + x + 1]; // Данные глубины рядом с центром

        // Обработка данных глубины
        double pZ = ((depthD2 << 8) & 0xFF00) + (depthD1 & 0xFF);

        // Вычисление углов в зависимости от координат центра
        double theta_w = (fov_w / (double) width) * (x - (width / 2.0));
        double theta_h = (fov_h / (double) height) * (y - (height / 2.0));

        // Вычисление координат X и Y в миллиметрах
        double pX = pZ * Math.tan(Math.toRadians(theta_w));
        double pY = pZ * Math.tan(Math.toRadians(theta_h));

        // Вывод координат на SmartDashboard
        StringBuilder sb = new StringBuilder()
                .append("x: " + Math.round(pX) + "мм, y: " + Math.round(pY) + "мм, z: " + pZ + "мм");
        SmartDashboard.putString("Данные: ", sb.toString());
    }

    /** Метод для обработки набора кадров */
    private void processFrameSet(FrameSet frameSet) {
        try (ColorFrame colorFrame = frameSet.getFrame(FrameType.COLOR)) // Получение цветного кадра
        {
            if (null != colorFrame) {
                printVideoFrame(colorFrame, SensorType.COLOR); // Вывод информации о цветном кадре
            }
        } catch (Exception e) {
            e.printStackTrace(); // Вывод информации об ошибке
        }
        try (DepthFrame depthFrame = frameSet.getFrame(FrameType.DEPTH)) // Получение кадра глубины
        {
            if (null != depthFrame) {
                printVideoFrame(depthFrame, SensorType.DEPTH); // Вывод информации о кадре глубины
                processDepthFrame(depthFrame); // Обработка кадра глубины
            }
        } catch (Exception e) {
            e.printStackTrace(); // Вывод информации об ошибке
        }
        try (IRFrame irFrame = frameSet.getFrame(FrameType.IR)) // Получение ИК кадра
        {
            if (null != irFrame) {
                printVideoFrame(irFrame, SensorType.IR); // Вывод информации о ИК кадре
            }
        } catch (Exception e) {
            e.printStackTrace(); // Вывод информации об ошибке
        }
    }

    public void Exit(){
        System.out.println("Rifat");
    }
}