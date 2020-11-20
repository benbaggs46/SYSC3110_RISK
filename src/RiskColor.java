import java.awt.*;

public enum RiskColor {
    RED(Color.RED, "Red"),
    BLUE(Color.BLUE, "Blue"),
    YELLOW(Color.YELLOW, "Yellow"),
    GREEN(Color.GREEN, "Green"),
    MAGENTA(Color.MAGENTA, "Magenta"),
    GRAY(Color.LIGHT_GRAY, "Gray");

    private Color color;
    private String colorName;

    RiskColor(final Color color, final String colorName) {
        this.color = color;
        this.colorName = colorName;
    }

    public String getName(){
        return colorName;
    }

    public Color getColor(){
        return color;
    }
}
