import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class BoardView extends JFrame{

    private BoardPanel mapPanel;

    public BoardView(){
        super("RISK");
        mapPanel = new BoardPanel();
        add(mapPanel, BorderLayout.CENTER);
        BoardController bc = new BoardController(this);
        MouseAdapter ma = new BoardMouseListener(mapPanel, bc);
        mapPanel.addMouseListener(ma);
        setSize(1000,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        Parser.begin(bc);
    }

    public BoardPanel getMapPanel(){
        return mapPanel;
    }

    public static void main(String[] args){
        new BoardView();
    }

}
