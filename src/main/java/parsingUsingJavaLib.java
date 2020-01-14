import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class parsingUsingJavaLib {
    public static void main(String[] args) {
        double costSum = 0; //сумма расходов
        double incomeSum = 0; //сумма всех приходов
        Map<String, Double> costBreakdown = new HashMap<>(); //Мапа, которая будет хранить разбивку расходов
        String pathToFile = "src/main/resources/movementList.csv";
        try {
            List<String> linesAtFile = Files.readAllLines(Paths.get(pathToFile));
            linesAtFile.remove(0); //удаляем строку с заголовками
            for (String line : linesAtFile) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    //вот тут парсим числовые значения (доходы и расходы):
                    if (values[i].charAt(0) == '"') { //если первый символ ", значит засплитили неверно и нужно склеить
                        values[i] += "," + values[i + 1];  // этот элемент со следующим, а затем удалить оттуда все кавычки.
                        values[i] = values[i].replaceAll("\"", "");
                        values[i] = values[i].replaceAll(",", "."); //заменим все запятые точками, чтобы парсить Double
                    }
                }
                double cost = Double.parseDouble(values[7]);
                double income = Double.parseDouble(values[6]);
                costSum += cost;
                incomeSum += income;
                String mccCode = values[5].substring(values[5].length() - 7, values[5].length()); //Вот тут парсим MCC-код операции
                costBreakdown.put(mccCode, costBreakdown.getOrDefault(mccCode, 0.0) + cost); //Добавляем сумму расходов в мапу
            }
            System.out.printf("Sum of all costs is %.2f\n", costSum );
            System.out.printf("Income sum is %.2f\n", incomeSum );

            System.out.println("Sum of all costs by category: ");
            for (String mcc: costBreakdown.keySet()) {
                System.out.printf("%s : %.2f\n", getCategoryByMcc(mcc), costBreakdown.get(mcc));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static String getCategoryByMcc(String mccCode) {
        switch (mccCode) {
            case "MCC6011":
                return "Cash withdrawals";
            case "MCC5691":
                return "Wear shops";
            case "MCC4121":
                return "Taxi";
            case "MCC6536":
                return "MasterCard MoneySend in country";
            case "MCC7399":
                return "Hosting";
            case "MCC5995":
                return "Pet shops";
            case "MCC5411":
                return "Supermarkets";
            case "MCC5814":
                return "Fast food";
            case "MCC5968":
                return "Subscriptions";
            case "MCC6538":
                return "MasterCard MoneySend Funding";
            case "MCC5812":
                return "Restaurants";
            case "MCC7372":
                return "Programming";
            case "MCC5977":
                return "Cosmetics shops";
            case "MCC5818":
                return "Digital products";
            default:
                return "Other";
        }
    } //Метод получения категории по MCC
}
