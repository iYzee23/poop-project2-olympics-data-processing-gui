package view;

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

// GraphChart prosiruje JPanel
public class Graph extends JPanel {

	private static final long serialVersionUID = 1L;
	private int padding = 25;
    private int labelPadding = 25;
    private Color pointColor = Color.red;
    private int pointWidth = 8;
    private int numberYDivisions = 7;
    private int numberXDivisions = 6;
    private int yWid = 35;
    private int xWid = 80;
    private int xOff = 30;
    private int yOff = 70;
    private ArrayList<DataTriplet> dataVals;
    private int xLeftBound = 1900;
    private int yStart = padding + 30;
    private int xStart = padding + labelPadding + 38;
    private double logicalYOffset = 50.0;
    private double logicalXOffset = 20.0;

    public void setPointColor(Color newColor) {
    	pointColor = newColor;
    }
    
    public Graph(ArrayList<DataTriplet> dataVals) {
        this.dataVals = dataVals;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //racunanje koordinata tacaka
        List<Point> graphPoints = new ArrayList<>();
        int startX = xStart;
        int startY = getHeight() - yStart;
        
        //skaliranje x vrednosti
        double distx1 = getWidth() + xWid - ((0 * (getWidth()-20 + xWid - padding * 2 - labelPadding)) / (numberXDivisions + 1) + padding + labelPadding);
    	double distx2 = getWidth() + xWid - ((1 * (getWidth()-20 + xWid - padding * 2 - labelPadding)) / (numberXDivisions + 1) + padding + labelPadding);
        double scaleDistX = (distx1 - distx2)/logicalXOffset;//20 je logicki offset izmedju godina 1900, 1920, 1940 itd..
    	
        //skaliranje y vrednosti
        double disty1 = getHeight() - yWid - ((0 * (getHeight() - yWid - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
        double disty2 = getHeight() - yWid - ((1 * (getHeight() - yWid - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
        double scaleDistY = (disty1 - disty2)/logicalYOffset;
        
    	for (int i = 0; i < dataVals.size(); i++) {
        	int year = dataVals.get(i).year;
        	double val = dataVals.get(i).val;
        	int x1 = (int)(startX + (year - xLeftBound)*scaleDistX);
        	int y1 = (int)(startY - val*scaleDistY);
            graphPoints.add(new Point(x1, y1));
        }

        // crtanje pozadine
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);
        
//        crtanje podeoka za y osu
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - yWid - ((i * (getHeight() - yWid - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            String yLabel = 50*i + "";
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 15, y0 + (metrics.getHeight() / 2) - 3 + xOff);
            g2.drawLine(x0 - pointWidth, y0 + xOff, x1 - pointWidth, y1 + xOff);
        }

        // crtanje podeoka za x osu
        for (int i = 0; i <= numberXDivisions; i++) {
        	 int x0 = getWidth() + xWid - ((i * (getWidth() -20 + xWid - padding * 2 - labelPadding)) / (numberXDivisions + 1) + padding + labelPadding);
             int x1 = x0;
             int y0 = getHeight() - padding - labelPadding;
             int y1 = y0 - pointWidth;
             String xLabel = 1900 + (numberXDivisions - i)*20 +"";
             FontMetrics metrics = g2.getFontMetrics();
             int labelWidth = metrics.stringWidth(xLabel);
             g2.drawString(xLabel, x0 - yOff - labelWidth / 2, y0 + metrics.getHeight() + 8);
             g2.drawLine(x0 - yOff, y0+pointWidth, x1 - yOff, y1+pointWidth);
        }

        // crtanje x i y ose
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        //Crtanje tacaka
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(oldStroke);
        for (int i = 0; i < graphPoints.size(); i++) {
        	if(dataVals.get(i).season.equals("Winter")){ //pazi na equals
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


    //ovo moze da koristi
//    public void setScores(List<Double> scores) {
//        this.scores = scores;
//        invalidate();
//        this.repaint();
//    }

    private static void createAndShowGui() {
        ArrayList<DataTriplet> dt = new ArrayList<DataTriplet>();
        dt.add(new DataTriplet(50.0, 1896, "Summer"));
        dt.add(new DataTriplet(0.0, 1900, "Winter"));
        dt.add(new DataTriplet(0.0, 1910, "Winter"));
        dt.add(new DataTriplet(0.0, 1920, "Winter"));
        Graph mainPanel = new Graph(dt);
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

