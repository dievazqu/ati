package dnv.ati.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Histogram {

	public Histogram(Map<Integer, Integer> values) {
		JFrame frame = new JFrame("Histograma");
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(new Graph(values)));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	protected class Graph extends JPanel {

		protected static final int MIN_BAR_WIDTH = 4;
		private Map<Integer, Integer> mapHistory;

		public Graph(Map<Integer, Integer> mapHistory) {
			this.mapHistory = mapHistory;
			int width = (mapHistory.size() * MIN_BAR_WIDTH) + 11;
			Dimension minSize = new Dimension(width, 128);
			Dimension prefSize = new Dimension(width, 256);
			setMinimumSize(minSize);
			setPreferredSize(prefSize);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (mapHistory != null) {
				int xOffset = 5;
				int yOffset = 5;
				int offsetForLabels = 20;
				int width = getWidth() - 1 - (xOffset * 2);
				int height = getHeight() - 1 - (yOffset * 2) - offsetForLabels;
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(Color.DARK_GRAY);
				g2d.drawRect(xOffset, yOffset, width, height);
				int barWidth = Math.max(MIN_BAR_WIDTH, (int) Math.floor((float) width / (float) mapHistory.size()));

				int maxValue = 0;
				for (Integer key : mapHistory.keySet()) {
					int value = mapHistory.get(key);
					maxValue = Math.max(maxValue, value);
				}
				int xPos = xOffset;
				for (Integer key : mapHistory.keySet()) {
					int value = mapHistory.get(key);
					int barHeight = Math.round(((float) value / (float) maxValue) * height);
					g2d.setColor(new Color(key, key, key));
					int yPos = height + yOffset - barHeight;
					// Rectangle bar = new Rectangle(xPos, yPos, barWidth,
					// barHeight);
					Rectangle2D bar = new Rectangle2D.Float(xPos, yPos, barWidth, barHeight);
					g2d.fill(bar);
					g2d.setColor(Color.DARK_GRAY);
					g2d.draw(bar);
					if(key%8==0){
						g2d.drawLine(xPos+(barWidth/2), yPos + barHeight + offsetForLabels - 14,
								 xPos+(barWidth/2), yPos + barHeight + 2);
						g2d.drawString(""+key, xPos - ((int)Math.log10(key)*4), yPos + barHeight + offsetForLabels);
					}
					xPos += barWidth;
				}
				g2d.dispose();
			}
		}
	}

}
