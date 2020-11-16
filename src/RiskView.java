public interface RiskView {
    public void updateMap(MapEvent me);
    public void updateUI(UIEvent uie);
    public void showMessage(String message);
    public String getStringInput();
    public int getIntInput();
}
