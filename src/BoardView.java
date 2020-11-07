import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class BoardView extends JFrame{

    private BoardPanel mapPanel;
    private InfoPanel infoPanel;
    private JButton actionButton;

    public BoardView(){

        super("RISK");
        mapPanel = new BoardPanel();
        add(mapPanel, BorderLayout.CENTER);
        BoardController bc = new BoardController(this);
        MouseAdapter ma = new BoardMouseListener(this, bc);
        mapPanel.addMouseListener(ma);

        infoPanel = new InfoPanel();
        add(infoPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JButton button = new JButton("New Game");
        button.addActionListener(new BoardButtonListener(bc));
        buttonPanel.add(button);

        button = new JButton("Help");
        button.addActionListener(new BoardButtonListener(bc));
        buttonPanel.add(button);

        button = new JButton("Quit");
        button.addActionListener(new BoardButtonListener(bc));
        buttonPanel.add(button);

        button = new JButton("Proceed");
        button.setActionCommand("Proceed");
        button.addActionListener(new BoardButtonListener(bc));
        buttonPanel.add(button);

        actionButton = new JButton("Action");
        actionButton.setActionCommand("Action");
        actionButton.addActionListener(new BoardButtonListener(bc));
        buttonPanel.add(actionButton);

        setSize(1200,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        //Parser.begin(bc);
    }

    public BoardPanel getMapPanel(){
        return mapPanel;
    }

    public InfoPanel getInfoPanel(){
        return infoPanel;
    }

    public void setActionButtonText(String text){
        actionButton.setText(text);
    }

    public static void main(String[] args){
        new BoardView();
    }

    public void updatePlayerInfo(Board board){
        Player currentPlayer = board.getCurrentPlayer();
        infoPanel.setPlayerName(currentPlayer.getName());
        infoPanel.setNumArmies(currentPlayer.getNumArmies());
        infoPanel.setNumTerritories(currentPlayer.getNumTerritories());
        infoPanel.setCurrentBonus(board.getArmyBonusForPlayer(currentPlayer));
        infoPanel.setArmiesToPlace(board.getArmiesToPlace());
    }

}
