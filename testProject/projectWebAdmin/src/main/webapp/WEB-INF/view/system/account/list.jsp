<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>


<script type='text/javascript'>

/* **************************
 * 계정 상세 조회
/* ************************** */
function fn_detail(userNo){
    location.href = "<%=request.getContextPath()%>/system/account/add.html?userNo="+userNo;
}
</script>
<!-- 검색 START -->
<div class="container-fluid">
	<div class="animated fadeIn">
		<div class="row">
		    <div class="col-lg-12">
		        <div class="card">
		            <div class="card-header">
		                <i class="far fa-edit"></i>
		                <strong>계정관리</strong>
		            </div>
		            <div class="card-body" id="colorPickerBlock">
		                <h:form class="form-horizontal" object="accountInfo">
		                    <input type="hidden" id="page"   name="page"   value="${accountInfo.page}" />
		                    <input type="hidden" id="totCnt" name="totCnt" value="${list[0].totCnt  }" />
		                    <div class="form-group row">
		                        <label for="userId" class="col-lg-1 col-form-label text-right" >아이디/이름 : </label>
		                        <div class="col-lg-2">
		                            <input type="text" class="form-control" id="userId" name="userId" value="${accountInfo.userId}" autofocus="autofocus"/>
		                        </div>
		                        <div class="col-lg-1">
		                            <button class="btn btn-danger" id="searchBtn" type="submit" >조회</button>
		                        </div>
		                    </div>
		                </h:form>
		            </div>
		        </div>
		    </div>
		</div>
		<!-- 검색 END -->
		
		<!-- 목록 START -->
		<div class="row">
		    <div class="col-lg-12">
		        <div class="card">
		            <div class="card-header">
		                <i class="fas fa-table"></i>
		                <strong>목록</strong>
		            </div>
		            <div id="infoCollapse" class="card-body">
		                <div class="row">
		                    <label class="col-lg-2">조회리스트 : ${list[0].totCnt}</label>
		                </div>
		                <table id="dataTable" class="table table-sm table-bordered table-condensed table-hover">
		                    <thead>
		                        <tr class="success">
		                            <th class="text-center">번호</th>
		                            <th class="text-center">아이디</th>
		                            <th class="text-center">이름</th>
		                            <th class="text-center">등록일</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                        <c:forEach var="item" items="${list}">
		                            <tr style="cursor:pointer;" onclick="fn_detail('${item.userNo}');">
		                                <td align="center" >${item.userNo}</td>
		                                <td align="center" >${item.userId}</td>
		                                <td align="center" >${item.userNm}</td>
		                                <td align="center" ><fmt:formatDate value="${item.regDttm}" pattern="yyyy.MM.dd"/></td>
		                            </tr>
		                        </c:forEach>
		                    </tbody>
		                </table>
		                <div>
		                    <jsp:include page="/WEB-INF/view/common/page/page.jsp" flush="false">
		                        <jsp:param value="${accountInfo.page}" name="page"/>
		                        <jsp:param value="${list[0].totCnt}" name="totCnt"/>
		                        <jsp:param value="20" name="cntPerPage"/>
		                    </jsp:include>
		                </div>
		            </div>
				    <div class="card-footer text-right">
				        <a class="btn btn-primary btn-flat" href="<%=request.getContextPath()%>/system/account/add.html?userNo=">계정 등록</a>
				    </div>
		        </div>
		    </div>
		</div>
	</div>
</div>
