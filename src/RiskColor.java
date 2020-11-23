import java.awt.*;

/**
 * Represents a RISK color with an color value and name
 */
public enum RiskColor {
    RED(Color.RED, "Red"),
    BLUE(Color.BLUE, "Blue"),
    YELLOW(Color.YELLOW, "Yellow"),
    GREEN(Color.GREEN, "Green"),
    MAGENTA(Color.MAGENTA, "Magenta"),
    GRAY(Color.LIGHT_GRAY, "Gray");

    /**
     * The color of the RiskColor
     */
    private Color color;
    /**
     * The name of the RiskColor
     */
    private String colorName;

    RiskColor(final Color color, final String colorName) {
        this.color = color;
        this.colorName = colorName;
    }

    /**
     * returns the color name
     * @return the name of the RiskColor
     */
    public String getName(){
        return colorName;
    }

    /**
     * return the color
     * @return The color of the RiskColor
     */
    public Color getColor(){
        return color;
    }
}
