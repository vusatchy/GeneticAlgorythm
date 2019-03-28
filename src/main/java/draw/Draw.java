package draw;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;


public class Draw {

    private Draw() {

    }

    static {
        //StdDraw.setXscale(-10, 10);
        // StdDraw.setYscale(-2.0, +100.0);
    }

    private static int MAX_X = 0;
    private static int MIN_X = 0;

    private static int MAX_Y = 0;
    private static int MIN_Y = 0;

    private static void resize(List<? extends Number> x, List<? extends Number> y) {
        IntSummaryStatistics xStat = x.stream().mapToInt(Number::intValue).summaryStatistics();
        IntSummaryStatistics yStat = y.stream().mapToInt(Number::intValue).summaryStatistics();

        int max_x = xStat.getMax();
        int max_y = yStat.getMax();

        int min_x = xStat.getMin();
        int min_y = yStat.getMin();

        MAX_X = Math.max(MAX_X, max_x);
        MAX_Y = Math.max(MAX_Y, max_y);

        MIN_X = Math.min(MIN_X, min_x);
        MIN_Y = Math.min(MIN_Y, min_y);

        StdDraw.setXscale(MIN_X, MAX_X);
        StdDraw.setYscale(MIN_Y, MAX_Y);
    }

    public static void drawCompare(List<? extends Number> x1, List<? extends Number> y1, Color color1,
                                   List<? extends Number> x2, List<? extends Number> y2, Color color2) {
        resize(x1, y1);
        resize(x2, y2);

        drawLine(x1, y1, color1);
        drawLine(x2, y2, color2);
    }

    public static void drawLine(Map<Integer, Double> cords, Color color) {
        List<Integer> x1 = new ArrayList<>();
        List<Double> y1 = new ArrayList<>();
        cords.forEach((x, y) -> {
            x1.add(x);
            y1.add(y);
        });
        resize(x1, y1);
        drawLine(x1, y1, color);
    }

    public static void drawLine(List<? extends Number> x, List<? extends Number> y, Color color) {
        StdDraw.setPenColor(color);
        for (int i = 0; i < x.size() - 1; i++) {
            StdDraw.line(x.get(i).doubleValue(), y.get(i).doubleValue(),
                    x.get(i + 1).doubleValue(), y.get(i + 1).doubleValue());
        }
    }

}
