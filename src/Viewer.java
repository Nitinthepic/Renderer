import javax.swing.*;
import java.awt.*;

public class Viewer {
	public static void main(String[] args){
		JFrame frame = new JFrame("Window");
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());

		JPanel renderPanel = new JPanel(){
			public void paintComponent(Graphics g){
				Graphics2D g2 = (Graphics2D) g;
				g2.setBackground(Color.WHITE);
				g2.fillRect(0,0,getWidth(),getHeight());


			}
		};
		pane.add(renderPanel,BorderLayout.CENTER);
		frame.setSize(400,400);

		frame.setVisible(true);
		Point dog = new Point(50,50,5);

		renderPanel.paintComponents(renderPanel.getGraphics());

	}
}
