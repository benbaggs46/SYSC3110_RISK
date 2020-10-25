import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class BoardConstructor {

    public static void joinTerritories(Territory t1, Territory t2){
        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
    }

    public static Board createMapFromFile(String filename){
        Board board = new Board();
        try
        {
            //creating a constructor of file class and parsing an XML file
            File file = new File("src\\" + filename);
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList continentList = doc.getElementsByTagName("continent");
            NodeList borderList = doc.getElementsByTagName("border");
            // nodeList is not iterable, so we are using for loop
            //continent loop
            for (int itr = 0; itr < continentList.getLength(); itr++)
            {
                Node node = continentList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    //System.out.println("Continent: "+ eElement.getElementsByTagName("name").item(0).getTextContent());
                    //System.out.println("Bonus: " + eElement.getElementsByTagName("bonus").item(0).getTextContent());

                    Continent c = new Continent(eElement.getElementsByTagName("name").item(0).getTextContent(),Integer.parseInt(eElement.getElementsByTagName("bonus").item(0).getTextContent()));
                    board.addContinent(c);

                    for(int i = eElement.getElementsByTagName("territory").getLength(); i > 0; i--) {
                        //System.out.println("Territory: " + eElement.getElementsByTagName("territory").item(i-1).getTextContent());

                        Territory t = new Territory(eElement.getElementsByTagName("territory").item(i-1).getTextContent());
                        c.addTerritory(t);
                    }
                }
            }
            //border loop
            for (int itr = 0; itr < borderList.getLength(); itr++)
            {
                Node node = borderList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    NodeList nodeList = eElement.getElementsByTagName("territory");
                    //System.out.println("Border: "+ nodeList.item(0).getTextContent() + " <-> " + nodeList.item(1).getTextContent());

                    Territory t1 = board.findTerritoryByName(nodeList.item(0).getTextContent());
                    Territory t2 = board.findTerritoryByName(nodeList.item(1).getTextContent());

                    if(t1 != null && t2 != null && !t1.getNeighbours().contains(t2)) joinTerritories(t1, t2);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return board;
    }
}