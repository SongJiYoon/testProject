<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    th{background-color:#f0f0f0;}
</style>

<script type='text/javascript'>

/* **************************
 * 그룹 상세 조회
/* ************************** */
function fn_detail(grpId){
    location.href = "<%=request.getContextPath()%>/system/groupAccount/detailList.html?userDivCd="+grpId;
}
</script>

<div class="container-fluid">
	<div class="animated fadeIn">
        <!-- 목록 START -->
        <div class="row">
            <div class="col-lg-12">
                <div class="card">
		            <div class="card-header">
		                <i class="fas fa-table"></i>
                        <strong>그룹관리</strong>
                    </div>
                    <div id="infoCollapse" class="card-body">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="dataTables_length" id="dataTable_length">
                                    <label class="font-weight-bold">총 ${fn:length(list) }개</label>
                                </div>
                            </div>
                        </div>
                        <table id="dataTable" class="table table-sm table-bordered table-condensed table-hover " >
                            <thead>
                                <tr role="row" class="success">
                                    <th class="text-center" >그룹 코드 </th>
                                    <th class="text-center" >그룸명    </th>
                                    <th class="text-center" >소속계정수</th>
                                    <th class="text-center" >생성일    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${list}">
                                    <tr style="cursor:pointer;" onclick="fn_detail('${item.grpId}');">
                                        <td align="center" >${item.grpId}</td>
                                        <td align="center" >${item.grpNm}</td>
                                        <td align="center" >${item.accountCnt}</td>
                                        <td align="center" ><fmt:formatDate value="${item.regDttm}" pattern="yyyy.MM.dd"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

