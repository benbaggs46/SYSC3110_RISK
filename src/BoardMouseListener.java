import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardMouseListener extends MouseAdapter {

    private BoardView boardView;

    public BoardMouseListener(BoardView boardView){
        this.boardView = boardView;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);

        Board board = boardView.getBoardController().getBoard();

        if(board == null) return;

        for(Territory t: board.getTerritoryList()) {
            if (t.getPolygon().contains(me.getPoint())) {
                board.toggleTerritorySelection(t);
            }
        }
        boardView.getMapPanel().drawTerritorySelection(boardView.getMapPanel().getGraphics());
    }
}
