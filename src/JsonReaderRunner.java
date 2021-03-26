import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class JsonReaderRunner {

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        //Получаем список билетов
        List<Ticket> ticketList = null;
        try {
            ticketList = objectMapper.readValue(new File(
                    "tickets.json"),
                            TicketList.class).getTickets();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Количество полетов
        int numberOfFlights = ticketList.size();

        //Список значений разницы полета между городами  Владивосток и Тель-Авив
        //Т.к дата вылета и прибытия одинакова, ее можно не учитывать
        List<Long> timeDifferenceInMilliSecondsListSorted = ticketList
                .stream()
                .filter(ticket -> ticket.getOrigin_name().equals("Владивосток")
                && ticket.getDestination_name().equals("Тель-Авив"))
                .mapToLong((ticket) -> parseStringToDate(ticket.getArrival_time()).getTime()
                        - parseStringToDate(ticket.getDeparture_time()).getTime())
                .boxed().sorted()
                .collect(Collectors.toList());


        // Среднее значение между разницей  в прибытии и вылета в часах
        Double averageTimeInHours
                = timeDifferenceInMilliSecondsListSorted.stream().mapToDouble(number -> number)
                .average().getAsDouble() / (double) (1000 * 60 * 60);

        // 90-й процентиль времени полета между городами  Владивосток и Тель-Авив
        Double percentile90ThInHours = timeDifferenceInMilliSecondsListSorted.get((int)(numberOfFlights * 0.9) - 1)
                / (double) (1000 * 60 * 60);


        try (FileWriter writer = new FileWriter("output.txt")) {
            writer.write(averageTimeInHours.toString());
            writer.write("\n");
            writer.write(percentile90ThInHours.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Date parseStringToDate(String stringToParse) {
        Date date = null;
        try {
            date = new SimpleDateFormat("HH:mm").parse(stringToParse);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        return date;
    }
}
