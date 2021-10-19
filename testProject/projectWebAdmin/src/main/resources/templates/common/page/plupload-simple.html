<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<a id="pickfiles" href="javascript:;" class="btn btn-primary btn-sm">파일업로드</a>

<div class="col-lg-8">
	<table class="table table-hover" id="plupload_list">
		<input type="hidden" name="plupload_name" value="${param.name }" />
		<input type="hidden" name="plupload_maxlength" value="${param.maxlength == null ? 30 : param.maxlength}" />
		<thead>
			<tr>
				<th class="preview">미리보기</th>
				<th>파일명</th>
				<th>삭제</th>
				<th class="plorder">순서</th>
			</tr>
		</thead>
		<tbody>
			<tr style="display: none">
				<td width="120px" class="preview"><img src="" width="100px" /></td>
				<td></td>
				<td>
					<span class="percent"></span>
					<a href="javascript:;" style="display: none" class="btn btn-primary btn-xs delete" onclick="pluploadDelete(this); return false; ">삭제</a>
				</td>
				<td class="plorder">
					<a href="javascript:;" class="btn btn-primary btn-xs" onclick="pl_up(this); return false; ">위</a>
					<a href="javascript:;" class="btn btn-primary btn-xs" onclick="pl_down(this); return false; ">아래</a>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<script type="text/javascript">

var plmaxlength = ${param.maxlength == null ? 30 : param.maxlength};
var plfiles = ${f:nvl(param.files, "null")};
var plpreview = ${f:nvl(param.preview, "true")};

$(function(){
	if(plmaxlength == 1){
		$("#plupload_list .plorder").hide();
	}

	if(!plpreview){
		$("#plupload_list .preview").hide();
	}

	var pl_fileOids = [];

	if($.isArray(plfiles)){
		$.each(plfiles, function(){
			pl_fileOids.push(this.fileOid);
		});
	}
	else if($.isPlainObject(plfiles)){
		pl_fileOids.push(plfiles.fileOid);
	}

	f_ajax("<%=request.getContextPath()%>/plfiles.json", pl_fileOids, function(resp){
		var tb = $("#plupload_list tbody");
		var tr = $("#plupload_list tbody tr:first");
		$.each(resp.resultData.data, function(){
			var ntr = tr.clone().show().attr("id", this.fileOid).addClass("cms");
			$("<a>").attr({ href : this.url, target : '_blank' }).text(this.diskFilename).appendTo(ntr.find("td:eq(1)"));
			$(ntr).find("img:eq(0)").attr({src : this.url});
			$(ntr).find("a.delete").show();

			ntr.data(this);
			ntr.appendTo(tb);
		});
	});

	if(pl_fileOids.length >= plmaxlength){
		$('#pickfiles').hide();
	}
});

function pl_files(){
	var files = [];
	$("#plupload_list > tbody > tr.cms").each(function(){
		files.push($(this).data());
	});

	return files;
}

function pl_up(o){
	var ctr = $(o).parents("tr:eq(0)");
	ctr.prev().before(ctr);
}

function pl_down(o){
	var ctr = $(o).parents("tr:eq(0)");
	ctr.next().after(ctr);
}

function pluploadDelete(s){
	var tr = $(s).parents("tr:eq(0)");
	tr.remove();

	if($("#plupload_list > tbody > tr.cms").length < plmaxlength){
		$('#pickfiles').show("hide");
	}
}

var uploader_conf = {
	runtimes : 'html5,flash,silverlight,html4',

    browse_button : 'pickfiles', // you can pass in id...
//    container: document.getElementById('container'), // ... or DOM Element itself
     
    url : "<%=request.getContextPath()%>/plupload.json",
     
    filters : {
        max_file_size : '1024mb',
        mime_types: [
            {title : "Image files", extensions : "jpg,gif,png,jpeg"},
            {title : "Video files", extensions : "mp4"},
            {title : "Zip files", extensions : "zip"}
        ]
    },

	chunk_size: '10mb',
 
    // Flash settings
    flash_swf_url : '/plupload/js/Moxie.swf',
 
    // Silverlight settings
    silverlight_xap_url : '/plupload/js/Moxie.xap',
     
 
    init: {
        PostInit: function() {
        },

        FilesAdded: function(up, files) {
			if(files.length >= plmaxlength){
				$('#pickfiles').hide("slow");
			}

			var tb = $("#plupload_list tbody");
			var tr = $("#plupload_list tbody tr:first");

			plupload.each(files, function(file) {
				var ntr = tr.clone().show().attr("id", file.id).addClass("cms");
				$(ntr).find("td:eq(1)").text(file.name);

				ntr.appendTo(tb);
			});

            uploader.start();
        },

		FileUploaded: function(up, file, info) {
			resp = JSON.parse(info.response);
			cmsfile = resp.resultData.data;

			var tr = $("#" + file.id);
			tr.data(cmsfile);
			tr.find("img:eq(0)").attr({src : cmsfile.url})

			var txt = tr.find("td:eq(1)").text();
			tr.find("td:eq(1)").text("");
			$("<a>").attr({ href : cmsfile.url, target : '_blank' }).text(txt).appendTo(tr.find("td:eq(1)"));
			tr.find("span.percent").hide();
			tr.find("a.delete").show();
		},
 
        UploadProgress: function(up, file) {
			var tr = $("#" + file.id);
			tr.find("span.percent").text(file.percent + "%");
        },

		UploadComplete: function(up, files){
		},
 
        Error: function(up, err) {
            //document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
        }
    }
};

if(plmaxlength == 1){
	uploader_conf['multi_selection'] = false;
}
 
var uploader = new plupload.Uploader(uploader_conf);
 
uploader.init();
 
</script>