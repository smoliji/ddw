

package cls;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.catalina.util.URLEncoder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author jirka
 */
public class Flickr {
    
    protected static final String api_key = "084cf64920b630babf3220bc5890687e";
    protected URLEncoder encoder;
    
    private static Flickr instance = null;

    public static Flickr getInstance() {
        if (instance == null) {
            instance = new Flickr();
        }
        return instance;
    }
    
    private Flickr() {
        encoder = new URLEncoder();
    }
    
    private Document exec (URL url) {                
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());
            return doc;
        } catch (IOException ex) {
            Logger.getLogger(Flickr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Flickr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Flickr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    protected String encode(String s) {
        return encoder.encode(s);
    }
    
    public List<FlickrPhoto> queryFlickr(String search, int limit) {
        List<FlickrPhoto> output = new ArrayList<FlickrPhoto>();
        Document xml;
        try {
            xml = search(search, limit);
            NodeList photoNodes = xml.getElementsByTagName("photo");
            for (int i = 0; i < photoNodes.getLength(); i++) {
                Node photoNode = photoNodes.item(i);
                output.add(new FlickrPhoto(photoNode));
            }
            
        } catch (Exception ex) {
            return null;
        }
        return output;
    }
    
    /**
     * Flickr ref: https://www.flickr.com/services/api/flickr.photos.search.html
     * 
     * @param text
     * @return Document
     * @throws MalformedURLException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException 
     */
    public Document search(String text, int perPage) throws MalformedURLException, IOException, SAXException, ParserConfigurationException {
        URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="+api_key+"&text="+encode(text)+"&sort=relevance"+"&per_page="+perPage);
        return exec(url);
    }
    
}
