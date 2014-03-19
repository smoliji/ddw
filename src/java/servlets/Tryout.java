package servlets;

import cls.Flickr;
import cls.GateClient;
import cls.RssProcessor;
import cls.WebPageProcessor;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "Tryout", urlPatterns = {"/tryout"})
public class Tryout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        String src = request.getParameter("src");
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        
        if (src == null || type == null) {
            request.setAttribute("data.name", "&laquo; Choose");            
            RequestDispatcher view = request.getRequestDispatcher("tryout.jsp");
            view.forward(request, response);
            return;
        }
        
        if (name == null) name = src;
        
        GateClient gc = GateClient.getInstance();
        gc.clear();
        
        if (type.equals("rss")) {
            RssProcessor rss = new RssProcessor(src, 5);
            request.setAttribute("data.name", name);    
            request.setAttribute("data.rss", rss);
            for(int i = 0; i < rss.size(); i++)
                gc.addDocument(rss.item(i).wp.getContent().toString());
        }        
        if (type.equals("wp")) {
            WebPageProcessor wp = new WebPageProcessor(src);
            request.setAttribute("data.name", wp.getTitle());
            request.setAttribute("data.wp", wp);
            gc.addDocument(wp.getContent().toString());
            
        }
        
        gc.run();
        request.setAttribute("data.pmi", gc.getPMI());
        request.setAttribute("data.flickr", Flickr.getInstance());
        
        RequestDispatcher view = request.getRequestDispatcher("tryout.jsp");
        view.forward(request, response);
    }
    
    
}
