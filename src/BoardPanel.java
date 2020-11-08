import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A panel to display the RISK map
 */
public class BoardPanel extends JPanel {

    /**
     * The BoardView this BoardPanel is placed on
     */
    private BoardView boardView;

    /**
     * The color used to indicate a selected territory
     */
    public static final Color TERRITORY_SELECTION_COLOR = Color.RED;

    /**
     * The color used to draw borders between territories and extra lines
     */
    public static final Color TERRITORY_BORDER_COLOR = Color.BLACK;

    /**
     * The color used for the numbers indicating the amount of armies in a territory
     */
    public static final Color ARMY_NUM_FONT_COLOR = Color.BLACK;

    /**
     * The font size used for the numbers indicating the amount of armies in a territory
     */
    public static final int ARMY_NUM_FONT_SIZE = 14;

    /**
     * The font used for the numbers indicating the amount of armies in a territory
     */
    public static final Font ARMY_NUM_FONT = new Font(Font.SANS_SERIF, Font.BOLD, ARMY_NUM_FONT_SIZE);

    /**
     * Constructor for BoardPanel
     * @param boardView The BoardView this BoardPanel is placed on
     */
    public BoardPanel(BoardView boardView){
        super();
        this.boardView = boardView;
    }

    /**
     * Draws extra lines onto the map
     * @param g The graphics component of this BoardPanel
     */
    public void drawExtraLines(Graphics g){
        Board board = boardView.getBoardController().getBoard();

        if(board == null) return;

        g.setColor(TERRITORY_BORDER_COLOR);

        for(List<Integer> line: board.getLines()){
            g.drawLine(line.get(0),line.get(1),line.get(2),line.get(3));
        }
    }

    /**
     * Draws the specified territory onto the map
     * @param t The territory to be drawn
     * @param g The graphics component of this BoardPanel
     */
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

    /**
     * Draws the entire map
     * @param g The graphics component of this BoardPanel
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Board board = boardView.getBoardController().getBoard();

        if(board == null) return;

        drawExtraLines(g);

        for(Territory t: board.getTerritoryList()) {

            drawTerritory(t,g);
        }
    }
}