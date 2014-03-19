package cls;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RssProcessor {
    
    public class Item {
        public String title;
        public String link;
        public String description;     
        public String pubDate;
        
        public WebPageProcessor wp;
        
        public Item (String t, String l, String d, String pd) {
            title = t;
            link = l;
            description = d;
            pubDate = pd;
            wp = new WebPageProcessor(link);
            
        }
    }
    
    ArrayList<Item> items = new ArrayList<Item>();
    
    public RssProcessor(String _url, int limit) {
        try {
            
            URL url = new URL(_url);                    
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());
            
            NodeList xmlItems = doc.getElementsByTagName("item");
            for (int i = 0; i < xmlItems.getLength() && i < limit; i++) {
                Element xmlItem = (Element) xmlItems.item(i);
                Item newItem = new Item(xmlItem.getElementsByTagName("title").item(0).getTextContent(),
                        xmlItem.getElementsByTagName("link").item(0).getTextContent(),
                        xmlItem.getElementsByTagName("description").item(0).getTextContent(),
                        xmlItem.getElementsByTagName("pubDate").item(0).getTextContent());
                items.add(newItem);
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(RssProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RssProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RssProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RssProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public RssProcessor(String url) {
        this(url, 1<<24);
    }
    
    private void parseDoc(Document doc, int limit) {
        NodeList xmlItems = doc.getElementsByTagName("item");
        for (int i = 0; i < xmlItems.getLength() && i < limit; i++) {
            Element xmlItem = (Element) xmlItems.item(i);
            items.add(new Item(xmlItem.getElementsByTagName("title").item(0).getTextContent(),
                               xmlItem.getElementsByTagName("link").item(0).getTextContent(),
                               xmlItem.getElementsByTagName("description").item(0).getTextContent(),
                               xmlItem.getElementsByTagName("pubDate").item(0).getTextContent()));                    
        }
    }
    
    public Item item(int i) {
        return items.get(i);
    }
    
    public int size() {
        return items.size();
    }
    
}
