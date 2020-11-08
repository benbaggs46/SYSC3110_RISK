import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private BoardView boardView;

    public static final Color TERRITORY_SELECTION_COLOR = Color.RED;

    public static final Color TERRITORY_BORDER_COLOR = Color.BLACK;

    public static final Color ARMY_NUM_FONT_COLOR = Color.BLACK;

    public static final int ARMY_NUM_FONT_SIZE = 14;

    public static final Font ARMY_NUM_FONT = new Font(Font.SANS_SERIF, Font.BOLD, ARMY_NUM_FONT_SIZE);

    public BoardPanel(BoardView boardView){
        super();
        this.boardView = boardView;
    }

    public void drawTerritory(Territory t,Graphics g){

        Board board = boardView.getBoardController().getBoard();

        if(board == null) return;

        g.setColor(board.getSelectedTerritories().contains(t)? TERRITORY_SELECTION_COLOR: t.getContinent().getColor());
        g.fillPolygon(t.getPolygon());

        g.setColor(TERRITORY_BORDER_COLOR);
        g.drawPolygon(t.getPolygon());

        int centerX = (int) t.getPolygon().getBounds().getCenterX();
        int centerY = (int) t.getPolygon().getBounds().getCenterY();

        g.setFont(ARMY_NUM_FONT);
        FontMetrics metrics = g.getFontMetrics(ARMY_NUM_FONT);

        String text = t.getNumArmies() + (t.getTempArmies() > 0? " + "+ t.getTempArmies(): "");

        int textWidth = Math.max(metrics.stringWidth(text), metrics.getHeight());

        g.setColor(TERRITORY_BORDER_COLOR);
        g.fillRect(centerX - textWidth/2 - 1, centerY - textWidth/2 - 1,textWidth + 2,textWidth + 2);
        g.setColor(t.getOwner().getColor());
        g.fillRect(centerX - textWidth/2, centerY - textWidth/2, textWidth, textWidth);

        g.setColor(ARMY_NUM_FONT_COLOR);
        g.drawString(text, centerX - textWidth / 2, centerY + metrics.getHeight() / 3);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Board board = boardView.getBoardController().getBoard();

        if(board == null) return;

        for(Territory t: board.getTerritoryList()) {

            drawTerritory(t,g);
        }

    }
}