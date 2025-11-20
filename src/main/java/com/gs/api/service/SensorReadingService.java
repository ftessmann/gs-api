package com.gs.api.service;

import com.gs.api.dto.request.SensorReadingCreateRequestDTO;
import com.gs.api.dto.response.SensorReadingResponseDTO;
import com.gs.api.enums.NotificationStatus;
import com.gs.api.model.*;
import com.gs.api.repository.SensorReadingRepository;
import com.gs.api.repository.SensorRepository;
import com.gs.api.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorReadingService {

    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final NotificationRepository notificationRepository;

    public SensorReadingResponseDTO createReading(SensorReadingCreateRequestDTO dto) {
        Sensor sensor = sensorRepository.findByCode(dto.getSensorCode())
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        SensorReading reading = SensorReading.builder()
                .sensor(sensor)
                .temperature(dto.getTemperature())
                .humidity(dto.getHumidity())
                .co(dto.getCo())
                .co2(dto.getCo2())
                .ch4(dto.getCh4())
                .h2s(dto.getH2s())
                .timestamp(Instant.now())
                .build();

        sensorReadingRepository.save(reading);

        checkThresholdsAndNotify(reading);

        return toResponseDTO(reading);
    }

    public List<SensorReadingResponseDTO> getReadingsForUser(Long userId) {
        List<SensorReading> readings =
                sensorReadingRepository.findBySensor_Sector_Supervisor_Id(userId);

        return readings.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<SensorReadingResponseDTO> getReadingsForUserSensor(Long userId, String sensorCode) {
        List<SensorReading> readings =
                sensorReadingRepository.findBySensor_CodeAndSensor_Sector_Supervisor_Id(sensorCode, userId);

        return readings.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private void checkThresholdsAndNotify(SensorReading reading) {
        Sector sector = reading.getSensor().getSector();
        User supervisor = sector.getSupervisor();

        if (reading.getTemperature() != null && reading.getTemperature() > 30.0) {
            createNotification(
                    supervisor,
                    sector,
                    "Temperatura acima do limite",
                    "A temperatura do setor " + sector.getName() +
                            " está em " + reading.getTemperature() + "°C (limite 30°C).",
                    "temperature",
                    reading.getTemperature(),
                    30.0
            );
        }

        if (reading.getCo2() != null && reading.getCo2() > 800.0) {
            createNotification(
                    supervisor,
                    sector,
                    "CO2 acima do limite",
                    "O nível de CO2 do setor " + sector.getName() +
                            " está em " + reading.getCo2() + " ppm (limite 800 ppm).",
                    "co2",
                    reading.getCo2(),
                    800.0
            );
        }
    }

    private void createNotification(
            User supervisor,
            Sector sector,
            String title,
            String description,
            String metric,
            Double measured,
            Double threshold
    ) {
        Notification notification = Notification.builder()
                .title(title)
                .description(description)
                .metric(metric)
                .measuredValue(measured)
                .thresholdValue(threshold)
                .status(NotificationStatus.PENDING)
                .recipient(supervisor)
                .sector(sector)
                .build();

        notificationRepository.save(notification);
    }

    private SensorReadingResponseDTO toResponseDTO(SensorReading reading) {
        return SensorReadingResponseDTO.builder()
                .id(reading.getId())
                .sensorId(reading.getSensor().getId())
                .sensorCode(reading.getSensor().getCode())
                .temperature(reading.getTemperature())
                .humidity(reading.getHumidity())
                .co(reading.getCo())
                .co2(reading.getCo2())
                .ch4(reading.getCh4())
                .h2s(reading.getH2s())
                .timestamp(reading.getTimestamp())
                .build();
    }
}
