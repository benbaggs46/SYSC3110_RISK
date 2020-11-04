import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardMouseListener extends MouseAdapter {

    private JPanel boardPanel;
    private Board board;

    public BoardMouseListener(JPanel boardPanel, Board board){
        this.boardPanel = boardPanel;
        this.board = board;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);

        boardPanel.paintAll(boardPanel.getGraphics());
        for(Territory t: board.getTerritoryList()) {
            if (t.getPolygon().contains(me.getPoint())) {
                System.out.println("Clicked "+ t.getName());
            }
        }

    }
}
