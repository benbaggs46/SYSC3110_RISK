import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BoardMouseListener extends MouseAdapter {

    private BoardView boardView;
    private BoardController boardController;

    public BoardMouseListener(BoardView boardView, BoardController boardController){
        this.boardView = boardView;
        this.boardController = boardController;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);

        Board board = boardController.getBoard();

        if(board == null) return;

        for(Territory t: board.getTerritoryList()) {
            if (t.getPolygon().contains(me.getPoint())) {
                System.out.println("Clicked "+ t.getName());
                board.toggleTerritorySelection(t);
            }
        }
        boardView.paintAll(boardView.getGraphics());

    }
}
