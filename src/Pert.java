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

        System.out.println("Средняя трудоемкость кодирования UI: " + uiAverageComplexity);
        System.out.println("Средняя трудоемкость кодирования обработчиков событий: " + actAverageComplexity);
        System.out.println("Средняя трудоемкость кодирования бизнес-объектов: " + boAverageComplexity);
        System.out.println("Средняя трудоемкость кодирования бизнес-методов: " + bmAverageComplexity);
        System.out.println();

        double uiStandardDeviation = uiParams.calcStandardDeviation();
        double actStandardDeviation = actParams.calcStandardDeviation();
        double boStandardDeviation = boParams.calcStandardDeviation();
        double bmStandardDeviation = bmParams.calcStandardDeviation();

        System.out.println("Среднеквадратичное отклонение UI: " + uiStandardDeviation);
        System.out.println("Среднеквадратичное отклонение обработчиков событий: " + actStandardDeviation);
        System.out.println("Среднеквадратичное отклонение бизнес-объектов: " + boStandardDeviation);
        System.out.println("Среднеквадратичное отклонение бизнес-методов: " + bmStandardDeviation);
        System.out.println();

        double totalComplexity = uiAverageComplexity * uiParams.count + actAverageComplexity * actParams.count
                + boAverageComplexity * boParams.count + bmAverageComplexity * bmParams.count;
        System.out.println("Суммарная трудоемкость кодирования: " + totalComplexity);

        double totalStandardDeviation = calcTotalStandardDeviation(uiStandardDeviation, actStandardDeviation,
                boStandardDeviation, bmStandardDeviation);
        System.out.println("Среднеквадратичное отклонение для оценки суммарной трудоемкости кодирования: "
                + totalStandardDeviation);

        double totalComplexity95 = totalComplexity + 2 * totalStandardDeviation;
        System.out.println("Суммарная трудоемкость кодирования, которую не превысим с вероятностью 95%: "
                + totalComplexity95);

        double relativeError = (totalStandardDeviation / totalComplexity) * 100;
        System.out.println("Относительная погрешность в оценке суммарной трудоемкости кодирования: "
                + relativeError + "%");

        double totalProjectComplexity = totalComplexity95 * 4;
        System.out.println("Общая трудоемкость проекта: " + totalProjectComplexity);

        double totalProjectComplexityMonth = totalProjectComplexity / 132;
        System.out.println("Общая трудоемкость проекта в человек-месяцах: " + totalProjectComplexityMonth);

        double optimalProjectDuration = 2.5 * (Math.pow(totalProjectComplexityMonth, 1.0/3));
        System.out.println("Оптимальная продолжительность проекта: " + optimalProjectDuration);

        double optimalTeamSize = totalProjectComplexityMonth / optimalProjectDuration;
        System.out.println("Оптимальная численность команды: " + optimalTeamSize);
    }

    private static double calcTotalStandardDeviation(double uiStandardDeviation, double actStandardDeviation,
                                                     double boStandardDeviation, double bmStandardDeviation) {
        return Math.sqrt(uiStandardDeviation * uiStandardDeviation + actStandardDeviation * actStandardDeviation
                + boStandardDeviation * boStandardDeviation + bmStandardDeviation * bmStandardDeviation);
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