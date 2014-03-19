<%@page import="cls.Flickr"%>
<%@page import="cls.FlickrPhoto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="cls.PMI"%>
<%@page import="cls.WebPageProcessor"%>
<%@page import="java.net.URL"%>
<%@page import="cls.RssProcessor.Item"%>
<%@page import="cls.RssProcessor"%>
<% request.setAttribute("menuActive", "tryout");%>
<%@ include file="header.jsp" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">

            <h5>RSS Feeds</h5>
            <ul class="nav nav-sidebar">
                <li><a href="?type=rss&name=CNN World news&src=http://rss.cnn.com/rss/edition_world.rss">CNN WORLD</a></li>
            </ul>
            <h5>Web pages</h5>
            <ul class="nav nav-sidebar">
                <li><a href="?type=wp&src=http://www.caranddriver.com/audi/r8">Audi R8</a></li>
            </ul>

        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header"><%=request.getAttribute("data.name")%></h1>

            <%
                RssProcessor rss = (RssProcessor) request.getAttribute("data.rss");
                PMI pmi = (PMI) request.getAttribute("data.pmi");
                Flickr flickr = (Flickr) request.getAttribute("data.flickr");
                if (rss != null) {
                    for (int i = 0; i < rss.size(); i++) {
                        Item item = rss.item(i);

            %>

            <h2><%=item.title%> <small><%=item.pubDate%></small></h2>
            <p><%=item.description%></p>

            <div class="well"><%=item.wp.getContent().toString()%></div>
            
            <%
                %><h4><%
                List<String> ents = pmi.getEntities(i);
                if (null == ents) {%><span class="label label-danger">No entities</span><%} else {
                    for (String s: ents) {
            %>          <span class="label label-primary"><%=s %></span> <%
                    }
                }
            
                List<String> kws = pmi.getKeywords(i, 12);
                if (kws == null) {%><span class="label label-warning">No keywords</span><%} else {
                    for (String s : kws) {
            %>          <span class="label label-default"><%=s %></span> <%
                    }
                }
                %></h4><%
                
            %>
            <%  if (null != ents) 
                    for (String s: ents) {
                    %><div class="panel panel-primary">
                        <div class="panel-heading">
                          <h3 class="panel-title"><%=s %></h3>
                        </div>
                        <div class="panel-body">
                        <% 
                        List<FlickrPhoto> fps = flickr.queryFlickr(s, 3);
                                  if (null != fps)
                                      for (FlickrPhoto f: fps){
                                          %><img style="display: inline-block; height: 15em; width: auto;" src="<%=f.getUrl()%>" alt="<%=f.getTitle() + "[" + s + "]"%>" title="<%=f.getTitle() + "[" + s + "]"%>"/><%                                
                                      }
                        %>
                       </div>
                     </div>
            <%      }  %>        
                
            
            <div>

                <% if (null != kws) 
                        for (String s : kws) {
                            List<FlickrPhoto> fp = flickr.queryFlickr(s, 1);
                            if (null != fp && fp.size() > 0) {
                                FlickrPhoto f = fp.get(0);
                %>
                <img style="display: inline-block; height: 15em; width: auto;" src="<%=f.getUrl()%>" alt="<%=f.getTitle() + "[" + s + "]"%>" title="<%=f.getTitle() + "[" + s + "]"%>"/>

                <%
                            }                    
               
                        }
         %></div><%
                
                }
            }
                
            %>

            
            
            

            <%
                WebPageProcessor wp = (WebPageProcessor) request.getAttribute("data.wp");
                pmi = (PMI) request.getAttribute("data.pmi");
                if (wp != null) {
                    
                    List<String> kws = pmi.getKeywords(0, 24);
                    List<String> ents = pmi.getEntities(0);
                
                    %><div class="well"><%=wp.getContent().toString()%></div><%
                
                    %><h4><%
                    if (ents != null) for (String s : ents) {
            %>          <span class="label label-primary"><%=s%></span> <%
                    }
                    if (null != kws)for (String s : kws) {
            %>          <span class="label label-default"><%=s%></span> <%            
                    }
                    %></h4><%
            %>
                    
            
                    <%  if (null != ents) 
                            for (String s: ents) {
                    %><div class="panel panel-primary">
                        <div class="panel-heading">
                          <h3 class="panel-title"><%=s %></h3>
                        </div>
                        <div class="panel-body">
                          <% 
                          List<FlickrPhoto> fps = flickr.queryFlickr(s, 3);
                                    if (null != fps)
                                        for (FlickrPhoto f: fps){
                                            %><img style="display: inline-block; height: 15em; width: auto;" src="<%=f.getUrl()%>" alt="<%=f.getTitle() + "[" + s + "]"%>" title="<%=f.getTitle() + "[" + s + "]"%>"/><%                                
                                        }
                          %>
                        </div>
                    </div>
                    <%      }  %>  
            
            <%  } %>

        </div>
    </div>
</div>




<%@ include file="footer.jsp" %>
