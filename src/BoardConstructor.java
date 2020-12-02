import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to construct a board based on an XML file.
 * Currently, the board will always be based on DEFAULT_MAP.xml in the source files.
 */
public class BoardConstructor {

    /**
     * Adds the territories to each others neighbour list
     * @param t1 the first territory
     * @param t2 the second territory
     */
    public static void joinTerritories(Territory t1, Territory t2){
        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
    }

    /**
     * Creates a Color object from a hexadecimal string
     * @param colorString String representing a hexadecimal color code, formatted like "FFFFFF"
     * @return A Color object representing the input string
     */
    public static Color hex2RGB(String colorString) {
        return new Color(
                Integer.valueOf( colorString.substring( 0, 2 ), 16 ),
                Integer.valueOf( colorString.substring( 2, 4 ), 16 ),
                Integer.valueOf( colorString.substring( 4, 6 ), 16 ) );
    }

    public boolean loadBoardFromFile(String filename, Board board){
        //TODO: implement error handling if map isn't valid
        if(validateXMLSchema("src/MAP.xsd","src/" + filename)) {
            try {
                //an instance of factory that gives a document builder
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                //an instance of builder to parse the specified xml file
                DocumentBuilder db = dbf.newDocumentBuilder();

                InputStream in = getClass().getResourceAsStream(filename);

                Document doc = db.parse(in);

                doc.getDocumentElement().normalize();

                NodeList continentList = doc.getElementsByTagName("continent");
                NodeList borderList = doc.getElementsByTagName("border");
                NodeList lineList = doc.getElementsByTagName("line");

                //iterates through continents
                for (int itr = 0; itr < continentList.getLength(); itr++) {
                    Node continentNode = continentList.item(itr);
                    if (continentNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element continentElement = (Element) continentNode;

                        Continent c = new Continent(continentElement.getElementsByTagName("name").item(0).getTextContent(), Integer.parseInt(continentElement.getElementsByTagName("bonus").item(0).getTextContent()), hex2RGB(continentElement.getElementsByTagName("color").item(0).getTextContent()));
                        board.addContinent(c);

                        NodeList territoryList = continentElement.getElementsByTagName("territory");

                        //iterates through territories in each continent
                        for (int j = 0; j < territoryList.getLength(); j++) {
                            Node territoryNode = territoryList.item(j);
                            if (territoryNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element territoryElement = (Element) territoryNode;

                                NodeList pointList = territoryElement.getElementsByTagName("point");

                                //if(pointList.getLength() == 0) return null; //return no board to caller, indicating that something went wrong

                                int[] xPoints = new int[pointList.getLength()];
                                int[] yPoints = new int[pointList.getLength()];

                                for (int k = 0; k < pointList.getLength(); k++) {
                                    String[] point = pointList.item(k).getTextContent().split(",");
                                    xPoints[k] = Integer.parseInt(point[0]);
                                    yPoints[k] = Integer.parseInt(point[1]);
                                }

                                Territory t = new Territory(territoryElement.getElementsByTagName("name").item(0).getTextContent(), c, new Polygon(xPoints, yPoints, xPoints.length));
                            }
                        }
                    }
                }

                //iterates through borders
                for (int itr = 0; itr < borderList.getLength(); itr++) {
                    Node node = borderList.item(itr);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        NodeList nodeList = eElement.getElementsByTagName("territory");

                        Territory t1 = board.findTerritoryByName(nodeList.item(0).getTextContent());
                        Territory t2 = board.findTerritoryByName(nodeList.item(1).getTextContent());

                        if (t1 != null && t2 != null && !t1.getNeighbours().contains(t2)) joinTerritories(t1, t2);
                    }
                }

                for (int itr = 0; itr < lineList.getLength(); itr++) {
                    Node node = lineList.item(itr);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        NodeList pointList = eElement.getElementsByTagName("point");

                        String[] point1 = pointList.item(0).getTextContent().split(",");
                        String[] point2 = pointList.item(1).getTextContent().split(",");

                        List<Integer> line = Arrays.asList(Integer.parseInt(point1[0]), Integer.parseInt(point1[1]), Integer.parseInt(point2[0]), Integer.parseInt(point2[1]));

                        board.addLine(line);
                    }
                }
            return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Validates the desired xml file against the schema
     * @param xsdPath path to the xsd file
     * @param xmlPath path to the xml file
     * @return true if the xml is valid
     */
    public boolean validateXMLSchema(String xsdPath, String xmlPath){

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
}