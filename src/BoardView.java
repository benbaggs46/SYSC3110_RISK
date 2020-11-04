import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class BoardView {

    public static void main(String[] args){

        BoardPanel jPanel = new BoardPanel();
        BoardController bc = new BoardController(jPanel);

        JFrame jFrame = new JFrame("Polygon Test");
        jFrame.setSize(1000,700);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.add(jPanel);
        MouseAdapter ma = new BoardMouseListener(jPanel, bc);
        jPanel.addMouseListener(ma);
        jFrame.add(jPanel);
        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.paint(jFrame.getGraphics());

        Parser.begin(bc);
    }

}
