<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>

<style>

.table-hover tbody tr:hover > td {
  cursor: pointer
}
</style>

<div class="outer">
    <div class="inner bg-light lter">

        <div class="row">
            <div class="col-lg-12">
                <div class="box box-solid">
                    <header>
                        <div class="icons"> <i class="fa fa-table"></i> </div>
                        <h5>초등 APP버전 목록</h5>
                    </header>
                    <div id="infoCollapse" class="body">
                        <table id="dataTable" class="table table-bordered table-condensed table-hover">
                            <thead>
                                <tr class="success">
                                    <th class="text-center">no</th>
                                    <th class="text-center">버전 코드</th>
                                    <th class="text-center">버전 정보</th>
                                    <th class="text-center">등록일시</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty list}">
                                    <tr>
                                        <td align="center" colspan="4">데이터가 존재하지 않습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${list}" varStatus="status">
                                        <tr style="cursor:pointer;" onclick="fn_detail('${item.apkMngNo}');">
                                            <td align="center" >${item.no}</td>
                                            <td align="center" >${item.verCd}</td>
                                            <td align="center" >${item.verNm}</td>
                                            <td align="center" ><fmt:formatDate value="${item.regDttm}" pattern="yyyy.MM.dd" /></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        <div>
                            <jsp:include page="/WEB-INF/view/common/page/page.jsp" flush="false">
                                <jsp:param value="${appInfo.page}" name="page"/>
                                <jsp:param value="${list[0].totCnt}" name="totCnt"/>
                                <jsp:param value="20" name="cntPerPage"/>
                            </jsp:include>
                        </div>
                    </div>
                </div>
            <!--12-->
            </div>
            <div class="col-lg-12 text-right btn-toolbar sidebar-right-opened">
                <a class="btn btn-primary" href="<%=request.getContextPath()%>/data/app/add.html?appDivCd=PRI">등록</a>
            </div>
        </div>
        <br />
    </div>
</div>

<script type='text/javascript'>
/* APK 상세 조회 */
function fn_detail(apkMngNo){

    location.href = "<%=request.getContextPath()%>/data/app/add.html?apkMngNo="+apkMngNo+"&appDivCd=PRI";
}
</script>