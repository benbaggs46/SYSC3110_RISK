/**
 * A view of the RISK board model
 */
public interface RiskView {
    /**
     * Updates the specified territories on the RISK map
     * @param me A MapEvent describing the board state
     */
    public void updateMap(MapEvent me);

    /**
     * Updates other UI elements
     * @param uie A UIEvent describing the board state
     */
    public void updateUI(UIEvent uie);

    /**
     * Displays a message to the user
     * @param message The message to be displayed
     */
    public void showMessage(String message);
}
