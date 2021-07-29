<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!doctype html>
<html class="no-js">
<head>
<meta charset="UTF-8">
<title><tiles:insertAttribute name="title" /></title>
<!--IE Compatibility modes-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--Mobile first-->
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-------------------------------- css -------------------------------->
<!-- Font Awesome -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.1/css/all.css">
<!-- stylesheet -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.min.css">

<!-------------------------------- javascript -------------------------------->
<!--jQuery -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="http://malsup.github.io/min/jquery.form.min.js"></script>
<!--Bootstrap -->
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/js/bootstrap.min.js"></script>
<!-- scripts -->
<script src="<%= request.getContextPath() %>/common/js/common.js"></script>


<body class="<tiles:insertAttribute name="bodyClass" />">
<tiles:insertAttribute name="mainArea" />
<script type="text/javascript">
$(document).ready(function(){
	var href = $("#menu ul a:eq(0)").attr("href") || $("#menu a:eq(0)").attr("href");
	if(href != '#')
		$("#mainArea").attr("src", href);
})
</script>
</body>
</html>