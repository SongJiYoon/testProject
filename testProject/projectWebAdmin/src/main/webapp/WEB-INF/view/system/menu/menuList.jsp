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
                        <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap no-footer">
                            <div class="row">
                                <div class="col-lg-6">
                                    <div class="dataTables_length" id="dataTable_length">
                                        <label class="font-weight-bold">총 ${fn:length(list) }개</label>
                                    </div>
                                </div>
                                <div class="col-lg-6" align="right"> <h:a href="#" class="btn btn-primary" onclick="f_ajax('save.json', $('#menuInfo').so(), function(r){ location.reload(); })">저장</h:a> </div>
                            </div>
                            <div class="row">
                                <h:form class="col-lg-12" id="menuInfo" name="menuInfo">
                                    <input type="hidden" name="grpId" value="${grpId}"/>
                                    <input type="checkbox" name="menuNos" value="" checked="checked" style="visibility: hidden;" />
                                    <input type="checkbox" name="menuNos" value="" checked="checked" style="visibility: hidden;" />
                                    <div>
                                        <table id="dataTable" class="table table-sm table-bordered table-condensed table-hover table-striped dataTable no-footer" role="grid" aria-describedby="dataTable_info">
                                            <thead>
                                                <tr role="row" class="success">
                                                    <th class="text-center" >번호  </th>
                                                    <th class="text-center" >메뉴1</th>
                                                    <th class="text-center" >메뉴2</th>
                                                    <th class="text-center" >메뉴3</th>
                                                    <th class="text-center" >메뉴4</th>
                                                    <th class="text-center" >선택  </th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${list}">
                                                <tr>
                                                    <td>${item.menuNo}</td>
                                                    <td>${fn:split(item.menuNm,'^##^')[0] }</td>
                                                    <td>${fn:split(item.menuNm,'^##^')[1] }</td>
                                                    <td>${fn:split(item.menuNm,'^##^')[2] }</td>
                                                    <td>${fn:split(item.menuNm,'^##^')[3] }</td>
                                                    <td><input type="checkbox" name="menuNos" value="${item.menuNo }" <c:if test="${item.selected == 1}">checked</c:if> /></td>
                                                </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                        <script type="text/javascript">
                                        var tb = $('#dataTable > tbody');
                                        var rl = tb.find('> tr').length;
                                        var cl = tb.find('> tr:eq(0) > td').length;

                                        for(var i=0; i<cl; i++){
                                            var ct = null;

                                            for(var j=0; j<rl; j++){
                                                if(!ct){
                                                    ct = tb.find('> tr:eq('+j+') > td:eq('+i+')');
                                                    if(ct.text() == '')
                                                        ct = null;
                                                    continue;
                                                }

                                                td = tb.find('> tr:eq('+j+') > td:eq('+i+')');

                                                if(ct.text() == td.text()){
                                                    var crp = ct.attr('rowspan') || '1';
                                                    crp = parseInt(crp);
                                                    td.hide();
                                                    ct.attr('rowspan', crp+1);
                                                }
                                                else if(!td.text()){
                                                    ct = null;
                                                }
                                                else{
                                                    ct = td;
                                                }
                                            }
                                        }
                                        </script>
                                    </div>
                                </h:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>