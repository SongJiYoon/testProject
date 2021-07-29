<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!-- 점검시간 -->
<%-- 
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.commons.lang.time.DateUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%
String pass = request.getParameter("pass");
if(StringUtils.isEmpty(pass) || !StringUtils.equals(pass, "kumon3rd")) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	String todayTime = sdf.format(new Date());

	if(todayTime.compareTo("20181115000000") > 0 && todayTime.compareTo("20181115180000") < 0) {
%>
    <div style="background: white;margin-top: 100px;">
	    <div class="content">
	        <div class="header">
	            <div style="font-size:30px;text-align:center;"><b>채점센터 이용제한 안내</b></div>	            
	        </div>
	        <div class="body">
	            <div style="font-size:20px;text-align:center;"><br/>인프라 증설 작업으로 인하여 서비스가 임시 제한됩니다.<br/>고객 여러분께 불편을 드려 죄송합니다.<br/><br/></div>
                <div style="font-size:21px;text-align:center;"><b>제한 일자 : 2017년 10월 28일(토)<br/>제한 시간 : 00:00 ~ 09:00<br/>제한 내용 : 작업시간 동안 서비스 이용 불가</b></div>
                <div style="font-size:20px;text-align:center;"><br/>빠른 시간 내에 작업을 완료하여 보다 나은 서비스로 보답하겠습니다.<br/>궁금하신 사항은 고객센터(1588-5566)로 문의해 주시기 바랍니다.</div>
	        </div>
	    </div>
	</div>
<%
	    return;
	}
}
%> --%>

<script type="text/javascript">
if(parent != self){
    top.location.href = '<%=request.getContextPath()%>/';
}
</script>
<div class="app flex-row align-items-center">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-5">
          <div class="card-group">
            <div class="card p-4">
              <div class="card-body">
                <h1>로그인</h1>
                <p class="text-muted">사용자 아이디와 패스워드를 입력하세요</p>
                <form class="form-submit" action="login.json" method="post">
                <div class="input-group mb-3">
                  <div class="input-group-prepend">
                    <span class="input-group-text">
                      <i class="fas fa-user"></i>
                    </span>
                  </div>
                  <input class="form-control" type="text" name="userId" placeholder="아이디">
                </div>
                <div class="input-group mb-4">
                  <div class="input-group-prepend">
                    <span class="input-group-text">
                      <i class="fas fa-lock"></i>
                    </span>
                  </div>
                  <input class="form-control" type="password" name="pwd" placeholder="패스워드">
                </div>
                <div class="row">
                  <div class="col-12">
                    <button class="btn btn-primary col-12" type="submit" onclick="
	                    f_submit(this.form, function(r){
	                        location.replace(r.go);
	                    });
	                ">로그인</button>
                  </div>
                </div>
	                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>