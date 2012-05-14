package genom.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Function {

	List<Point> plotData;

	public Function() {
		plotData = new ArrayList<Point>();
	}

	public void calculate(int[] xValues) {

		for (int x : xValues) {
			Point point = new Point(x, xSquare(x));
			plotData.add(point);
		}
	}

	public static int xSquare(int x) {
		return x * x;
	}

	public List<Point> getPlotData() {
		return plotData;
	}
}
