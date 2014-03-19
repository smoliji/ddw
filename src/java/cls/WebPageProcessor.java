package cls;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPageProcessor {
    
    private Document doc;
    private StringBuffer content;
    
    private String title;
    
    public WebPageProcessor(String url) {
        
        content = new StringBuffer();
        
        try {
            
            doc = (Document) Jsoup.connect(url).get();
            title = doc.title();
            
            processDocument();
            
        } catch (IOException ex) {
            Logger.getLogger(WebPageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void processDocument() {
        Elements paragraphs = doc.getElementsByTag("p");
        for (Element p: paragraphs) {
            content.append(p.text());
        }
    }
    
    public StringBuffer getContent() {
        return content;
    }
    
    public String getTitle() {
        return title;
    }
    
}
