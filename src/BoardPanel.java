import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private Board board;

    public BoardPanel(Board board){
        super();
        this.board = board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(Territory t: board.getTerritoryList()){
            g.setColor(t.getContinent().getColor());
            g.fillPolygon(t.getPolygon());
            g.setColor(Color.BLACK);
            g.drawPolygon(t.getPolygon());
        }
    }
}