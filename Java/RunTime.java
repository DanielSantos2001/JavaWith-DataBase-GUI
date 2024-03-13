import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RunTime {


    /**Método para calcular diferença de tempo na execução (tempo final - tempo inicial).
     */

    public void calcularTempoExecucao(LocalDateTime tempo) {

        DateTimeFormatter x = DateTimeFormatter.ofPattern("EEEE; yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Duration duration = Duration.between(tempo.atZone(ZoneId.systemDefault()).toInstant(), Instant.now());

        System.out.println("Inicio: " + tempo.format(x) + "\n" + "Fim: " + LocalDateTime.now().format(x) + "\n" +
                "Tempo de execucao: " + duration.toMillis() + "milisegundos ( Segundos = " + duration.toSecondsPart() +
                "; minutos = " + duration.toMinutesPart() + "; horas = " + duration.toHoursPart() + " )");

    }

    public int minutos(LocalDateTime tempo) {
        DateTimeFormatter x = DateTimeFormatter.ofPattern("EEEE; yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Duration duration = Duration.between(tempo.atZone(ZoneId.systemDefault()).toInstant(), Instant.now());
        return duration.toMinutesPart()+1;
    }

}
