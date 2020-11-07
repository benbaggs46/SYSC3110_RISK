import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                board.toggleTerritorySelection(t);
            }
        }
        boardView.getMapPanel().drawTerritorySelection(boardView.getMapPanel().getGraphics());
    }
}
