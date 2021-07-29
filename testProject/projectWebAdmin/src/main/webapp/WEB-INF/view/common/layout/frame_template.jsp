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
<!-- Bootstrap -->
<!-- <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.min.css"> -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap-timepicker.min.css" />
<!-- Font Awesome -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.1/css/all.css">
<!-- stylesheet -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="<%= request.getContextPath() %>/assets/lib/html5shiv/html5shiv.js"></script>
<script src="<%= request.getContextPath() %>/assets/lib/respond/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
var CTXPATH = "<%= request.getContextPath() %>";
</script>
<!--Modernizr-->
<script src="//cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.3/modernizr.min.js"></script>
<!--jQuery -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="http://malsup.github.io/min/jquery.form.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/menu.js"></script>
</head>
<body class="app header-fixed sidebar-fixed aside-menu-fixed sidebar-lg-show">
<div id="wrap">
	<div id="top">
		<nav class="app-header navbar">
			<button class="navbar-toggler sidebar-toggler d-lg-none mr-auto" type="button" data-toggle="sidebar-show">
				<span class="navbar-toggler-icon"></span>
			</button>
      		<a class="navbar-brand" href="<%= request.getContextPath() %>/index.html">
        		<img class="navbar-brand-full" src="<%= request.getContextPath() %>/assets/img/logo.png" width="100%" height="100%" alt="스마트구몬" style="padding:5px 0px 5px 10px;">
        		<!-- <img class="navbar-brand-minimized" src="img/brand/sygnet.svg" width="30" height="30" alt="구몬ICT 통합관리자"> -->
      		</a>
      		<button class="navbar-toggler sidebar-toggler d-md-down-none" type="button" data-toggle="sidebar-lg-show">
      			<span class="navbar-toggler-icon"></span>
      		</button>
      		<ul class="nav navbar-nav d-md-down-none">
	        	<h:li var="v" loop="${user.menuInfos}" test="empty(prtMenuNo)" class="nav-item px-3"> 
	        		<a class="nav-link" href="<%= request.getContextPath() %>/index.html?menuNo=${v.menuNo}">${v.menuNm }</a>
	        	</h:li>
	      	</ul>
	      <ul class="nav navbar-nav ml-auto">
	     	<li class="nav-item d-md-down-none">
	            <i class="far fa-user"></i>
	            <span>&nbsp;${user.tutorGrpNm }(${user.userNm })님</span>
	        </li>
	     	<!-- <li class="nav-item d-md-down-none">
	          <a class="nav-link" href="#" onclick="memoPopup();">
	            <i class="far fa-envelope"></i>
	            <span id="unMemo" class="badge badge-pill badge-danger"></span>
	          </a>
	        </li> -->
	        <li class="nav-item">
	          <a class="nav-link" href="<%= request.getContextPath() %>/login/logout.html">
	            <i class="fas fa-power-off"></i>
	          </a>
	        </li>
	      </ul>
	    </nav>
		<!-- /.navbar -->
		<!-- /.head -->
	</div>
	<!-- /#top -->
	<div id="left">
		<!-- #menu -->
		<div class="app-body">
      		<div class="sidebar">
        		<nav class="sidebar-nav">
          			<ul id="menu" class="nav">
          				<tiles:insertAttribute name="leftArea" />
          			</ul>
        		</nav>
      		</div>
    	</div>
		<!-- /#menu -->
	</div>
	<!-- /#left -->
	<div class="main" id="content">
		<ol class="breadcrumb d-lg-none mr-auto" style="margin-bottom:0;">
          <%-- <li class="breadcrumb-item">${subTitle}</li> --%>
          <h:li var="v" loop="${user.menuInfos}" test="empty(prtMenuNo)" class="breadcrumb-item"> 
	        	<a href="<%= request.getContextPath() %>/index.html?menuNo=${v.menuNo}">${v.menuNm }</a>
	      </h:li>
        </ol>
          	<iframe id="mainArea" name="mainArea" src="" width="100%" frameborder="0" style="margin-top:1.5rem;min-height: 844px;"></iframe>
	</div>
	<!-- /#right -->
</div>
<!-- /#wrap -->
<footer class="app-footer">
	<div>
		<span>2016 &copy; 구몬ICT 통합 관리자</span>
	</div>
</footer>
<!-- /.modal -->
<!-- /#helpModal -->
<!--Bootstrap -->
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/bootstrap-timepicker.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/bootstrap-datepicker.kr.js"></script>
<!-- spinner -->
<script type="text/javascript" src="<c:url value='/assets/lib/spin/spin.min.js'/>"></script>
<!-- scripts -->
<script src="<%= request.getContextPath() %>/common/js/common.js"></script>
<script src="<%= request.getContextPath() %>/common/js/popup.js"></script>
<script>
/* function memoPopup(){
    var x = 770;
    var y = 870;
    var left = (screen.availWidth - x) / 2;
    var top = (screen.availHeight - y) / 2;
    var options = "menubar=no, toolbar=no, location=no, status=no, resizble=no, fullscreen=no, scrollbars=yes, top=" + top + ", left=" + left + ", width=" + x + ", height=" + y;
    var url = "/admin/board/memo/memoSend.html";
    var popup = window.open(url, "memo", options);
}
$(document).ready(function() {
	// 쪽지 정보 조회
	fn_memoConfirm(); 
	
});

function fn_memoConfirm(){
    var data = {};
    data['userNo'] = '${user.userNo}';
    f_ajax('getUnConfirmMemoCnt.json', data, function(data){
             $("#unMemo").text(data.cnt); 
    });
} */



</script>

</body>
</html>
