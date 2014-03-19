<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String menuActive = (String) request.getAttribute("menuActive");
    if (null == menuActive) menuActive = "home";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="bootstrap/css/dashboard.css" />
        <script type="text/javascript" src="bootstrap/js/jquery-1.11.0.min.js"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
        
        <title>DDW Homework 1</title>
    </head>
    <body role="document" style="padding-top: 70px">
        
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="home">MI-DDW</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li <% if (menuActive.equals("home")) out.write("class='active'"); %>><a href="home">Home</a></li>
            <li <% if (menuActive.equals("tryout")) out.write("class='active'"); %>><a href="tryout">Tryout</a></li>
            <li <% if (menuActive.equals("contact")) out.write("class='active'"); %>><a href="contact">Contact</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    
    <div class="container">
