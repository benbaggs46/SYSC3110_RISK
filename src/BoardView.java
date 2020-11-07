import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class BoardView extends JFrame{

    private BoardPanel mapPanel;
    private InfoPanel infoPanel;
    private JButton actionButton;
    private BoardController bc;

    public BoardView(){

        super("RISK");
        bc = new BoardController(this);
        mapPanel = new BoardPanel(this);
        add(mapPanel, BorderLayout.CENTER);
        MouseAdapter ma = new BoardMouseListener(this);
        mapPanel.addMouseListener(ma);

        infoPanel = new InfoPanel();
        add(infoPanel, BorderLayout.WEST);

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

        setSize(1200,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public BoardController getBoardController(){
        return bc;
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

}
