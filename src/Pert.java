import java.util.Scanner;

public class Pert {
    public static void main(String[] args) {
        ObjectParameters uiParams = new ObjectParameters();
        ObjectParameters actParams = new ObjectParameters();
        ObjectParameters boParams = new ObjectParameters();
        ObjectParameters bmParams = new ObjectParameters();
        try (Scanner in = new Scanner(System.in)) {
            uiParams.readParameters(in, "Введите параметры для UI");
            actParams.readParameters(in, "Введите параметры для обработчиков событий");
            boParams.readParameters(in, "Введите параметры для бизнес-объектов");
            bmParams.readParameters(in, "Введите параметры для бизнес-методов");
        }

        double uiAverageComplexity = uiParams.calcAverageComplexity();
        double actAverageComplexity = actParams.calcAverageComplexity();
        double boAverageComplexity = boParams.calcAverageComplexity();
        double bmAverageComplexity = bmParams.calcAverageComplexity();

        printFormatParameter("Средняя трудоемкость кодирования UI", uiAverageComplexity);
        printFormatParameter("Средняя трудоемкость кодирования обработчиков событий", actAverageComplexity);
        printFormatParameter("Средняя трудоемкость кодирования бизнес-объектов", boAverageComplexity);
        printFormatParameter("Средняя трудоемкость кодирования бизнес-методов", bmAverageComplexity);
        System.out.println();

        double uiStandardDeviation = uiParams.calcStandardDeviation();
        double actStandardDeviation = actParams.calcStandardDeviation();
        double boStandardDeviation = boParams.calcStandardDeviation();
        double bmStandardDeviation = bmParams.calcStandardDeviation();

        printFormatParameter("Среднеквадратичное отклонение UI", uiStandardDeviation);
        printFormatParameter("Среднеквадратичное отклонение обработчиков событий", actStandardDeviation);
        printFormatParameter("Среднеквадратичное отклонение бизнес-объектов", boStandardDeviation);
        printFormatParameter("Среднеквадратичное отклонение бизнес-методов", bmStandardDeviation);
        System.out.println();

        double totalComplexity = uiAverageComplexity * uiParams.count + actAverageComplexity * actParams.count
                + boAverageComplexity * boParams.count + bmAverageComplexity * bmParams.count;
        printFormatParameter("Суммарная трудоемкость кодирования", totalComplexity);

        double totalStandardDeviation = calcTotalStandardDeviation(uiStandardDeviation, actStandardDeviation,
                boStandardDeviation, bmStandardDeviation);
        printFormatParameter("Среднеквадратичное отклонение для оценки суммарной трудоемкости кодирования",
                totalStandardDeviation);

        double totalComplexity95 = totalComplexity + 2 * totalStandardDeviation;
        printFormatParameter("Суммарная трудоемкость кодирования, которую не превысим с вероятностью 95%"
                , totalComplexity95);

        double relativeError = (totalStandardDeviation / totalComplexity) * 100;
        printFormatParameter("Относительная погрешность в оценке суммарной трудоемкости кодирования: ",
                "%", relativeError);

        double totalProjectComplexity = totalComplexity95 * 4;
        printFormatParameter("Общая трудоемкость проекта", totalProjectComplexity);

        double totalProjectComplexityMonth = totalProjectComplexity / 132;
        printFormatParameter("Общая трудоемкость проекта в человеко-месяцах", totalProjectComplexityMonth);

        double optimalProjectDuration = 2.5 * (Math.pow(totalProjectComplexityMonth, 1.0 / 3));
        printFormatParameter("Оптимальная продолжительность проекта", optimalProjectDuration);

        double optimalTeamSize = totalProjectComplexityMonth / optimalProjectDuration;
        printFormatParameter("Оптимальная численность команды", optimalTeamSize);
    }

    private static double calcTotalStandardDeviation(double uiStandardDeviation, double actStandardDeviation,
                                                     double boStandardDeviation, double bmStandardDeviation) {
        return Math.sqrt(uiStandardDeviation * uiStandardDeviation + actStandardDeviation * actStandardDeviation
                + boStandardDeviation * boStandardDeviation + bmStandardDeviation * bmStandardDeviation);
    }

    private static void printFormatParameter(String prefix, String suffix, double parameter) {
        System.out.printf("%s: %.4f%s%n", prefix, parameter, suffix);
    }

    private static void printFormatParameter(String prefix, double parameter) {
        printFormatParameter(prefix, "", parameter);
    }

    private static class ObjectParameters {
        private int optimistic;
        private int pessimistic;
        private int possible;
        private int count;

        void readParameters(Scanner scanner, String title) {
            System.out.println(title);
            System.out.print("Оптимистичная оценка: ");
            optimistic = scanner.nextInt();
            System.out.print("Пессимистичная оценка: ");
            pessimistic = scanner.nextInt();
            System.out.print("Наиболее вероятная оценка: ");
            possible = scanner.nextInt();
            System.out.print("Общее количество объектов: ");
            count = scanner.nextInt();
            System.out.println();
        }

        double calcAverageComplexity() {
            return (pessimistic + 4 * possible + optimistic) / 6.0;
        }

        double calcStandardDeviation() {
            return (pessimistic - optimistic) / 6.0;
        }
    }
}