import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private Board board;

    public BoardPanel(){
        super();
    }

    public void setBoard(Board board){
        this.board = board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(board == null) return;

        for(Territory t: board.getTerritoryList()){
            g.setColor(t.getContinent().getColor());
            g.fillPolygon(t.getPolygon());
            g.setColor(Color.BLACK);
            g.drawPolygon(t.getPolygon());
        }
        for(Territory t: board.getTerritoryList()){
            if(board.getSelectedTerritories().contains(t)) {
                g.setColor(Color.RED);
                g.drawPolygon(t.getPolygon());
            }
        }
    }
}