import java.util.List;

public interface RiskView {
    public void updateMap(MapEvent me);
    public void updateUI(UIEvent uie);
    public void showMessage(String message);
    public String getStringInput(String prompt, String defaultValue);
    public int getIntInput(String prompt, int min, int max);
    public int getOption(String prompt, Object[] options);
}
