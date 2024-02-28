package IoT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Un relevé est effectué par un capteur
 * Un relevé est composé de :
 * la température, le taux d'humité, l'instant du relevé sous la forme "yyyy-MM-dd HH:mm:ss"
 */
public record Releve(float temperature, float tauxHumidite, String Horodatage) {}
