#DDW Homework 1

Java web application for extracting keywords and entities, providing those with images from Flickr.

##Info

*What are these files?* It is a Netbeans directory structure. Source files are located in [src](https://github.com/smoliji/ddw/tree/master/src/java) and [web](https://github.com/smoliji/ddw/tree/master/web) directories. 

Application have 2 functionalities:
 * process **set of web pages** provided by rss
 * process **single web page**
 
###Set of web pages processing

####Phase 1 - Find articles in RSS resource

Application parses RSS in XML format provided by user and then process all the pages in file (CNN news provides RSS containing list of links to articles).

####Phase 2 - Information extraction

All articles are scanned by GATE Embedded, entities (persons, organizations, locations) are found. Key-words for each article are determined by [pointwise mutual information](http://en.wikipedia.org/wiki/Pointwise_mutual_information), thus more frequent words contained in each article are less important etc.

####Phase 3 - Flickr

When all articles passed above phases, Flickr is queried for couple of images of entities found using [Flickr API](https://www.flickr.com/services/api/), mostly [search function](https://www.flickr.com/services/api/flickr.photos.search.html) ordered by relevance.

###Single web page processing

Web page is scanned for entities by GATE Embedded. Photos of entities are found (see Phase 3 in Web pages set processing)

##Example outputs

Example outputs are located in [docs](https://github.com/smoliji/ddw/tree/master/docs).

Image files shows output of application to certain text. 
Legend: Blue labels are entities, grey keywords followed by pictures from Flickr.

HTML files are copy-pasted page from the application response (does not include stylesheets, but is enough to get picture whats the app doing)


