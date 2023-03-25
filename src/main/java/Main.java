import entity.Data;
import entity.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final int ARRAY_INDEX_CALL_TYPE = 0;
    private static final int ARRAY_INDEX_PHONE = 1;
    private static final int ARRAY_INDEX_START_TIME = 2;
    private static final int ARRAY_INDEX_END_TIME = 3;
    private static final int ARRAY_INDEX_TARIFF = 4;

    private static final String UNLIMIT_TARIFF_ID = "06";
    private static final String RESOURCE_DIR_PATH = "./reports";

    private static void parseInfo(String line, Map<User, TreeSet<Data>> map) {
        String[] lineArray = line.split(", ");
        User user = new User(lineArray[ARRAY_INDEX_PHONE], lineArray[ARRAY_INDEX_TARIFF]);
        if (!map.containsKey(user)) {
            map.put(user, new TreeSet<>());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date start = sdf.parse(lineArray[ARRAY_INDEX_START_TIME]);
            Date end = sdf.parse(lineArray[ARRAY_INDEX_END_TIME]);
            Data data = new Data(lineArray[ARRAY_INDEX_CALL_TYPE], start, end, user);
            map.get(user).add(data);
        } catch (ParseException e) {
            throw new RuntimeException("Didn't parse date!");
        }
    }

    private static void createReport(User user, TreeSet<Data> dataSet) {
        try(FileWriter writer = new FileWriter("./reports/" + user.getPhoneNumber() + ".txt", false)) {
            writer.write("Tariff index: " + user.getTariff().getId() + "\n" +
                    "----------------------------------------------------------------------------\n" +
                    "Report for phone number " + user.getPhoneNumber() + ":\n" +
                    "----------------------------------------------------------------------------\n" +
                    "| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n" +
                    "----------------------------------------------------------------------------\n");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            double totalCost = 0.;
            for (Data data: dataSet) {
                final int MULTIPLIER_TO_HOURS = 1000 * 60 * 60;
                final int MULTIPLIER_TO_MINUTES = 1000 * 60;
                final int MULTIPLIER_TO_SECONDS = 1000;
                final int COUNT_TIME = 60;

                long duration = data.getDuration();
                int hours = (int) duration / MULTIPLIER_TO_HOURS;
                int minutes = (int) (duration / MULTIPLIER_TO_MINUTES) % COUNT_TIME;
                int seconds = (int) (duration / MULTIPLIER_TO_SECONDS) % COUNT_TIME;

                String startString = format.format(data.getStartTime());
                String endString = format.format(data.getEndTime());
                totalCost += data.getCost();
                writer.write("|     " + data.getCallType() + "    | " +
                        startString + " | " + endString + " | " +
                        String.format("%02d:%02d:%02d | ", hours, minutes, seconds) +
                        String.format("%5.2f", data.getCost()) + " |\n");
            }
            totalCost += user.getTariff().getId().equals(UNLIMIT_TARIFF_ID) ? 100 : 0;
            writer.write("----------------------------------------------------------------------------\n" +
                    "|                                           Total Cost: | " +
                    String.format("%9.2f", totalCost) + " rubles |\n" +
                    "----------------------------------------------------------------------------\n");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Map<User, TreeSet<Data>> map = new TreeMap<>();
        try(Scanner scanner = new Scanner(new File("./src/main/resources/cdr.txt"))){
            while (scanner.hasNextLine()) {
                parseInfo(scanner.nextLine(), map);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        File resourceDirectory = new File(RESOURCE_DIR_PATH);
        if (resourceDirectory.mkdir()) {
            for (User key: map.keySet()) {
                createReport(key, map.get(key));
            }
        } else {
            System.out.println("Can't make directory 'reports'");
        }
    }
}
