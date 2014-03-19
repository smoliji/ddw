
package cls;

import org.w3c.dom.Node;

public class FlickrPhoto {
    
    protected String url;
    protected String title;
    
    public FlickrPhoto (Node node) {
        String farmId = node.getAttributes().getNamedItem("farm").getNodeValue();
        String serverId = node.getAttributes().getNamedItem("server").getNodeValue();
        String id = node.getAttributes().getNamedItem("id").getNodeValue();
        String secret = node.getAttributes().getNamedItem("secret").getNodeValue();
        url = "http://farm"+farmId+".staticflickr.com/"+serverId +"/"+id+"_"+secret+".jpg";
        title = node.getAttributes().getNamedItem("title").getNodeValue();
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getTitle() {
        return title;
    }
}
