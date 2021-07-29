<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!doctype html>
<html class="no-js">
<head>
<meta charset="UTF-8">
<title>구몬ICT 통합관리자</title>
<!--IE Compatibility modes-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--Mobile first-->
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-------------------------------- css -------------------------------->
<!-- Font Awesome -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.1/css/all.css">
<!-- stylesheet -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.min.css">
<!-- datapicker -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.min.css" />
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker3.min.css" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap-timepicker.min.css" />
<!-- bootstrap-table -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/lib/bootstrap-table/bootstrap-table.css">

<!-- validate -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/validate.css">

<!-------------------------------- javascript -------------------------------->
<!--jQuery -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="http://malsup.github.io/min/jquery.form.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/js/bootstrap.min.js"></script>

<!-- scripts -->
<script src="<%= request.getContextPath() %>/common/js/common.js"></script>
<!-- datapicker -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/bootstrap-timepicker.min.js"></script>

<script src="<%= request.getContextPath() %>/assets/js/bootstrap-datepicker.kr.js"></script>
<!-- validate -->
<script src="<%= request.getContextPath() %>/assets/js/jquery.validate.js" type="text/javascript"></script>
<!-- bootstrap-table -->
<script src="<%= request.getContextPath() %>/assets/lib/bootstrap-table/bootstrap-table.js"></script>
<script src="<%= request.getContextPath() %>/assets/lib/bootstrap-table/bootstrap-table-ko-KR.js"></script>

<!-- spinner -->
<script src="<%= request.getContextPath() %>/assets/js/spin.min.js'/>"></script>

</head>
<body id="__CONTENT_BODY__">
		<tiles:insertAttribute name="mainArea" />
<script type="text/javascript">
$(function(){

});
</script>
</body>
</html>
