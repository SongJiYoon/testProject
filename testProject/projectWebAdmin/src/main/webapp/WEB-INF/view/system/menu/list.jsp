<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type='text/javascript'>
/* **************************
 * 메뉴 상세 조회
/* ************************** */
function fn_detail(grpId){
    location.href = "<%=request.getContextPath()%>/system/menu/menuList.html?grpId="+grpId;
}
</script>

<div class="container-fluid">
	<div class="animated fadeIn">
        <div class="row ui-sortable">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-header">
                       <i class="fas fa-table"></i>
                        <strong>그룹별 메뉴관리</strong>
                    </div>
                    <div id="collapse4" class="card-body">
                        <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap no-footer">
                            <div class="row">
                                <div class="col-lg-6">
                                    <div class="dataTables_length" id="dataTable_length">
                                        <label class="font-weight-bold">총 ${fn:length(list) }개</label>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <table id="dataTable" class="table table-sm table-bordered table-condensed table-hover table-striped dataTable no-footer" role="grid" aria-describedby="dataTable_info">
                                        <thead>
                                            <tr role="row" class="success">
                                                <th class="text-center" tabindex="0" aria-controls="dataTable" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">그룹 코드</th>
                                                <th class="text-center" tabindex="0" aria-controls="dataTable" aria-label="Browser: activate to sort column ascending">그룸명</th>
                                                <th class="text-center" tabindex="0" aria-controls="dataTable" aria-label="Browser: activate to sort column ascending">소속메뉴수</th>
                                                <th class="text-center" tabindex="0" aria-controls="dataTable" aria-label="Browser: activate to sort column ascending">생성일</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${list}">
                                                <tr style="cursor:pointer;" onclick="fn_detail('${item.grpId}');">
                                                    <td align="center" >${item.grpId}</td>
                                                    <td align="center" >${item.grpNm}</td>
                                                    <td align="center" >${item.menuCnt}</td>
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
        </div>
    </div>
</div>

