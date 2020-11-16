import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * Creates a frame for the user to play RISK
 */
public class BoardView extends JFrame implements RiskView{

    /**
     * The panel to draw the risk board on
     */
    private BoardPanel mapPanel;

    /**
     * The panel to display player specific information
     * (player name, turn phase, number of territories, etc.)
     */
    private InfoPanel infoPanel;

    /**
     * The button the user presses to place/retract armies, attack, or fortify
     */
    private JButton actionButton;

    /**
     * The BoardController which updates the Board model according to user input and updates the BoardView to reflect those changes
     */
    private BoardController bc;

    /**
     * Constructor for BoardView
     */
    public BoardView(){

        super("RISK");
        bc = new BoardController(this);
        mapPanel = new BoardPanel(this);
        add(mapPanel, BorderLayout.CENTER);
        MouseAdapter ma = new BoardMouseListener(this);
        mapPanel.addMouseListener(ma);

        infoPanel = new InfoPanel();
        add(infoPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JButton button = new JButton("New Game");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        button = new JButton("Help");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        button = new JButton("Quit");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        button = new JButton("Proceed");
        button.setActionCommand("Proceed");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        actionButton = new JButton("Action");
        actionButton.setActionCommand("Action");
        actionButton.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(actionButton);

        setSize(1000,725);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Returns the associated BoardController
     * @return The associated BoardController
     */
    public BoardController getBoardController(){
        return bc;
    }

    /**
     * Returns the map panel of this BoardView
     * @return The map panel of this BoardView
     */
    public BoardPanel getMapPanel(){
        return mapPanel;
    }

    /**
     * Returns the info panel of this BoardView
     * @return The info panel of this BoardView
     */
    public InfoPanel getInfoPanel(){
        return infoPanel;
    }

    /**
     * Sets the text of the ACTION button to the specified text
     * @param text The text to be displayed by the ACTION button
     */
    public void setActionButtonText(String text){
        actionButton.setText(text);
    }
    
    public static void main(String[] args){
        new BoardView();
    }

    @Override
    public void updateMap(MapEvent me) {
        for(Territory t: me.getTerritoryList()){
            mapPanel.drawTerritory(t, mapPanel.getGraphics());
        }
    }

    @Override
    public void updateUI(UIEvent uie) {

    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public String getStringInput() {
        return null;
    }

    @Override
    public int getIntInput() {
        return 0;
    }
}
