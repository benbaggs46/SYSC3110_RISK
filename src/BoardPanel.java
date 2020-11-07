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

    public void drawMap(Graphics g){

        if(board == null) return;

        for(Territory t: board.getTerritoryList()) {
            g.setColor(t.getContinent().getColor());
            g.fillPolygon(t.getPolygon());
            g.setColor(Color.BLACK);
            g.drawPolygon(t.getPolygon());
        }
    }

    public void drawTerritorySelection(Graphics g){

        if(board == null) return;

        for(Territory t: board.getTerritoryList()) {
            g.setColor(Color.BLACK);
            g.drawPolygon(t.getPolygon());
        }

        for(Territory t: board.getTerritoryList()) {
            if(board.getSelectedTerritories().contains(t)) {
                g.setColor(Color.RED);
                g.drawPolygon(t.getPolygon());
            }
        }

    }

    public void drawTerritoryInfo(Territory t,Graphics g){

        g.setColor(t.getContinent().getColor());
        g.fillPolygon(t.getPolygon());

        int centerX = (int) t.getPolygon().getBounds().getCenterX();
        int centerY = (int) t.getPolygon().getBounds().getCenterY();

        g.setColor(t.getOwner().getColor());
        g.fillOval(centerX - 15, centerY - 15, 30,30);

        g.setColor(Color.WHITE);
        g.fillOval(centerX - 10,centerY - 10, 20,20);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.drawString(t.getNumArmies() + (t.getTempArmies() > 0? " + "+ t.getTempArmies(): ""), centerX - g.getFont().getSize() / 2, centerY + g.getFont().getSize() / 2);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if(board == null) return;

        drawMap(g);

        for(Territory t: board.getTerritoryList()) {

            drawTerritoryInfo(t,g);
        }

        drawTerritorySelection(g);

    }
}