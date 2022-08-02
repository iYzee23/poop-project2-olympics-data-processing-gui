package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ds.DataTriplet;

public class DynamicGraph extends JPanel {

	private static final long serialVersionUID = 1L;
    private int padding = 25;
    private int labelPadding = 25;
    private int pointWidth = 8;
    private int numberYDivisions = 10;
    private List<DataTriplet> scores;
    private double maxScore = Double.MIN_VALUE;
    private double minScore = Double.MAX_VALUE;
    private int xBegin;
    private int xEnd;
    private int yearRange;

    public DynamicGraph(List<DataTriplet> scores, int xBegin, int xEnd) {
        this.scores = scores;
        this.xBegin = xBegin;
        this.xEnd = xEnd;
        yearRange = xEnd - xBegin;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (yearRange - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
        	//todo
//            int x1 = (int) (i * xScale + padding + labelPadding);
            int x1 = (int) ((scores.get(i).year - xBegin) * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores.get(i).val) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.size() > 0) {
//                g2.setColor(gridColor);
//                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // and for x axis
        for (int i = 0; i < yearRange; i++) {
            if (yearRange > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (yearRange - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((yearRange / 20.0)) + 1)) == 0) {
//                    g2.setColor(gridColor);
//                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + xBegin + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                    g2.drawLine(x0, y0, x1, y1);
                }
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
//        g2.setColor(lineColor);
//        g2.setStroke(GRAPH_STROKE);
//        for (int i = 0; i < graphPoints.size() - 1; i++) {
//            int x1 = graphPoints.get(i).x;
//            int y1 = graphPoints.get(i).y;
//            int x2 = graphPoints.get(i + 1).x;
//            int y2 = graphPoints.get(i + 1).y;
//            g2.drawLine(x1, y1, x2, y2);
//        }

        g2.setStroke(oldStroke);
        for (int i = 0; i < graphPoints.size(); i++) {
        	if(scores.get(i).season.equals("Winter")) {
        		g2.setColor(Color.blue);
        	}
        	else g2.setColor(Color.red);
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, heigth);
//    }
    private double getMinScore() {
        for (DataTriplet score : scores) {
            minScore = Math.min(minScore, score.val);
        }
        return minScore;
    }

    private double getMaxScore() {
        for (DataTriplet score : scores) {
            maxScore = Math.max(maxScore, score.val);
        }
        return maxScore;
    }

    public void setScores(List<DataTriplet> scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }

    public List<DataTriplet> getScores() {
        return scores;
    }

    private static void createAndShowGui() {
        ArrayList<DataTriplet> scores = new ArrayList<DataTriplet>();
        scores.add(new DataTriplet(0.0, 1900, "Summer"));
        scores.add(new DataTriplet(1.0, 1900, "Summer"));
        scores.add(new DataTriplet(4.0, 1900, "Summer"));
        scores.add(new DataTriplet(0.0, 1928, "Summer"));
        scores.add(new DataTriplet(10.0, 1935, "Summer"));
        DynamicGraph mainPanel = new DynamicGraph(scores, 1900, 2020);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
		public void run() {
            createAndShowGui();
         }
      });
   }
}
