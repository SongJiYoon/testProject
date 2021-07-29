<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="container-fluid">
	<div class="animated fadeIn">
        <div class="row ui-sortable">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-table"></i>
                        <strong>목록</strong>
                    </div>
                    <div id="collapse4" class="card-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <table id="dataTable" class="table table-sm table-bordered table-condensed table-hover table-striped dataTable no-footer" role="grid" aria-describedby="dataTable_info">
                                        <thead>
                                            <tr role="row" class="success">
                                                <th class="text-center" >회원번호</th>
                                                <th class="text-center" >아이디</th>
                                                <th class="text-center" >이름</th>
                                                <th class="text-center" >소속</th>
                                                <th class="text-center" >등록일</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty list}">
                                                    <tr>
                                                        <td colspan="5" style="text-align:center;">데이터가 존재하지 않습니다.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${list}">
                                                        <tr>
                                                            <td align="center" >${item.userNo}</td>
                                                            <td align="center" >${item.userId}</td>
                                                            <td align="center" >${item.userNm}</td>
                                                            <td align="center" >${item.userDivCd}</td>
                                                            <td align="center" ><fmt:formatDate value="${item.regDttm}" pattern="yyyy.MM.dd"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        <div>
                        <h:form class="form-horizontal" object="accountInfo">
                        	<input type="hidden" id="page"   name="page"   value="${accountInfo.page}" />
                        	<input type="hidden" id="userDivCd"   name="userDivCd"   value="${accountInfo.userDivCd}" />
		                    <input type="hidden" id="totCnt" name="totCnt" value="${list[0].totCnt  }" />
                        </h:form>
                            <jsp:include page="/WEB-INF/view/common/page/page.jsp" flush="false">
                                <jsp:param value="${accountInfo.page}" name="page"/>
                                <jsp:param value="${list[0].totCnt}" name="totCnt"/>
                                <jsp:param value="20" name="cntPerPage"/>
                            </jsp:include>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

