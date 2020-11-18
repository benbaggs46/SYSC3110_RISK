public interface RiskInput {
    public String getStringInput(String prompt, String defaultValue);
    public int getIntInput(String prompt, int min, int max);
    public int getOption(String prompt, Object[] options);
}
