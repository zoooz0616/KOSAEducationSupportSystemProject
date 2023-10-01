var chkAll = $("#chkAll");//"전체" 체크박스
var chkList = $("input[class=chkWokCd]");//출석 상태 체크박스 리스트
var isChecked = chkAll.prop("checked");//"전체" 체크박스의 체크 상태(true 또는 false)
var allChecked = chkList.filter(":checked").length === chkList.length;
var wlogListTable = $('#wlog_list_tbody');//tbody 선택하기
var date = new Date();
var year = date.getFullYear();
var stdtListTable = document.getElementById('stdt_list_table_tbody');//tbody 선택하기
var date = new Date();
var year = date.getFullYear();

var radioBtns = $("input[name=default_period]");

//오늘 기준 이번 달의 첫 날 반환
function getFirstDay() { var month = ("0" + (1 + date.getMonth())).slice(-2); var day = "01"; return year + "-" + month + "-" + day; }
//오늘 기준 이번 달의 마지막 날 반환
function getLastDay() { var month = ("0" + (1 + date.getMonth())).slice(-2); var lastDay = new Date(year, month, 0).getDate(); return year + "-" + month + "-" + lastDay; }
//tbody의 모든 내용을 삭제
function clearTable() {
	/*if (wlogListTable.firstChild != null) {
		while (wlogListTable.firstChild) {
			wlogListTable.removeChild(wlogListTable.firstChild);
		}
	}*/
	wlogListTable.empty();
}

//오늘 기준 이번 주의 월요일, 일요일을 각각 반환
function getThisWeek(){
	let thisWeek = [];
	for(let i=0; i<7; i++) {
		let resultDay = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() - (i + new Date().getDay()));
		let yyyy = resultDay.getFullYear();
		let mm = Number(resultDay.getMonth()) + 1;
		let dd = resultDay.getDate();
		mm = String(mm).length === 1 ? '0' + mm : mm;
		dd = String(dd).length === 1 ? '0' + dd : dd;
		thisWeek[i] = yyyy + '-' + mm + '-' + dd;
	}
	return thisWeek;
}

//체크된 출석 상태 반환
function getFilterCd() {
	const checkedWokCds = [];
	chkList.each(function() {
		if ($(this).prop("checked")) {
			checkedWokCds.push($(this).val());
		}
	});
	return checkedWokCds;
}
//End

//현재 체크된 라디오 버튼의 값에 따라 start/end Date를 변경
function setSearchPeriod(){
	if($("input[name='default_period']:checked").val()=="thisClassPeriod"){
		let thisClassId = $('#class_selector option:selected').val();
		thisClassId = thisClassId.split('(')[1];
		thisClassId = thisClassId.split(',')[0];
		thisClassId = thisClassId.split('=')[1];
		console.log(thisClassId);
		
		$.ajax({
			type: 'get',
			url: '/manager/worklog/getPeriod', // 서버의 엔드포인트 URL
			data: {
				clssId: thisClassId
			},
			async: false,
			success: function(response) {
				$("#startDate").val(response.classDetail.clssStartDd);
				$("#endDate").val(response.classDetail.clssEndDd);
			}
			, error: function(error) {
				console.log(error);
			}
		})
	}else if ($("input[name='default_period']:checked").val()=="thisMonth") {
		$('#startDate').val(getFirstDay());
		$('#endDate').val(getLastDay());
	}else{
		$('#startDate').val(getThisWeek()[6]);//월요일 넣기
		$('#endDate').val(getThisWeek()[0]);//일요일 넣기
	}
}
//End

//초기화 버튼에 필요한 함수 할당
function reset(){
	// 검색 결과는 그대로

	// 날짜 초기화(null), 라디오 버튼 초기화 
	$('#startDate').val(null);
	$('#endDate').val(null);
	$("input:radio[name=default_period]").prop('checked',false)

	//교육과정 선택상자 0번 선택
	document.getElementById('class_selector').selectedIndex=0;
	
	// 이수상태 체크박스 전부 체크
	$('#chkAll').prop("checked", true);
	$('.chkWokCd').prop("checked", true);
	$('#fileContainedOnly').prop("checked", false);
	$('#isDelete').prop("checked", false);
	
	// 검색어 null 
	$('#search_keyword').val(null)
}

//---------------------------------------------------------------------------
// 1. 체크박스 컨트롤
$(document).ready(
	//document.getElementById('classId').value = document.getElementById('classId').options[document.getElementById('classId').selectedIndex];
	
	radioBtns.change( function(){
		setSearchPeriod();
	}),
	
	//"전체" 체크박스의 상태에 따라 나머지 체크박스의 상태를 변경
	chkAll.on("change", function() {
		isChecked = chkAll.prop("checked");
		chkList.prop("checked", isChecked);
	})
	//End
	,
	//개별 체크박스가 변경될 때 "전체" 체크박스 상태 업데이트
	chkList.on("change", function() {
		allChecked = chkList.filter(":checked").length === chkList.length;
		chkAll.prop("checked", allChecked);
	})
	//End
	//화면이 로딩 되면 출석 상태 전부 체크하기
	,
	chkAll.prop('checked', true)
	,
	chkList.prop('checked', true)
	//End
);
//End : 체크박스 컨트롤

// 2. 검색 누르면 검색하세요
function showModal(resnIcon) {
	var thisResnId = resnIcon.getAttribute("value");
	console.log(thisResnId);
}

function search() {
	
	var targetClassId = $('#class_selector option:selected').val();
	
	/*
	// 교육과정이 선택되지 않았다면 알림을 띄우고 탈출한다
	if(targetClassId == ''){
		alertFade("교육과정을 선택하세요.","F9DCCB","FF333E")
		return;
	}
	// End
	/*/
	
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;
	
	// 검색 기간이 선택되지 않았다면 알림을 띄우고 탈출한다
	if(startDate == '' || endDate == ''){
		alertFade("검색 기간을 입력하세요.","F9DCCB","FF333E")
		return;
	}
	// End
	
	
	if(targetClassId != ''){
		targetClassId = targetClassId.split("(")[1];
		targetClassId = targetClassId.split(",")[0];
		targetClassId = targetClassId.split("=")[1];
	}else{
		targetClassId = null
	}
	
	var keyword = $("#search_keyword").val();
	var wlogCd = getFilterCd();
	var isDeleteVal = $('#isDelete').is(':checked');
	var resnOnlyVal = $('#fileContainedOnly').is(':checked');
	
	//ㅇㅋ
	
	$.ajax({
		type: 'get',
		url: '/manager/worklog/search', // 서버의 엔드포인트 URL
		// 			/*data: { classId<<컨트롤러에서 받는 위치: classId<<클라이언트가 보내는거})*/
		data: {
			clssId: targetClassId
			, startDate: startDate
			, endDate: endDate
			, wlogCd:wlogCd
			, isDelete:isDeleteVal
			, resnOnly:resnOnlyVal
			, keyword:keyword
		},
		async: false,
		success: function(wlogListResponse) {
			var wlogList = wlogListResponse.wlogList;
			
			alertFade(wlogList.length+"건이 검색되었습니다.","9FBCCD","0E5881");
			
			// 테이블 초기화
			clearTable();

			//인원 수 입력
			document.getElementById("wlogCnt").innerHTML = wlogList.length;
			//End

			//입력 시작
			for (let i = 0; i < wlogList.length; i++) {
				// 새 행 생성
				var newRow = $('<tr></tr>');
				newRow.className = "wlog_list_tbody_row";//행 클래스 입력

				var rowChk = $('<td></td>');
				var rowNum = $('<td></td>');
				var rowName = $('<td></td>');
				var rowEmail = $('<td></td>');
				var rowInTime = $('<td></td>');
				var rowOutTime = $('<td></td>');
				var rowTotalTime = $('<td></td>');
				var rowWlogCd = $('<td></td>');
				var rowIsDelete = $('<td></td>');
				var rowResn = $('<td></td>');
				var rowResnCd = $('<td></td>');

				rowChk.html('<input type="checkbox" class="chk_wlog">');
				rowChk.attr("value",wlogList[i].stdtId);
				rowNum.text(i+1);
				rowName.text(wlogList[i].stdtNm);
				rowEmail.text(wlogList[i].userEmail);
				rowInTime.html(wlogList[i].strInTmDd.split(" ")[0]+"<br>"+wlogList[i].strInTmDd.split(" ")[1].split(".")[0]);
				rowOutTime.html(wlogList[i].strOutTmDd.split(" ")[0]+"<br>"+wlogList[i].strOutTmDd.split(" ")[1].split(".")[0]);
				rowTotalTime.text(Number(wlogList[i].wlogTotalTm).toFixed(1));
				rowWlogCd.text(wlogList[i].wlogCd);
				rowIsDelete.text(wlogList[i].deleteYn);
				if(wlogList[i].resnId!=null){
					rowResn.html(
						$('<img>',{
						src:'/img/file.png'
						,value:wlogList[i].resnId
						,class:'resn_icon'
						})
						/*
						.css({
							cursor:'pointer'
							,width:'15px'
							,height: '15px'
						})
						*/
					);
				}
				
				$('.resn_icon').attr("onclick","showModal(this)");
				
				rowResnCd.text(wlogList[i].prcsCd);
				
				rowChk.attr("class","chk_wlog");
				rowNum.attr("class","row_number");
				rowName.attr("class","row_name");
				rowEmail.attr("class","row_email");
				rowInTime.attr("class","row_intime");
				rowOutTime.attr("class","row_outtime");
				rowTotalTime.attr("class","row_totaltile");
				rowWlogCd.attr("class","row_wlogcode");
				rowIsDelete.attr("class","row_isdelete");
				rowResn.attr("class","file_cell");
				rowResnCd.attr("class","row_resncode");
				
				newRow.append(rowChk);
				newRow.append(rowNum);
				newRow.append(rowName);
				newRow.append(rowEmail);
				newRow.append(rowInTime);
				newRow.append(rowOutTime);
				newRow.append(rowTotalTime);
				newRow.append(rowWlogCd);
				newRow.append(rowIsDelete);
				newRow.append(rowResn);
				newRow.append(rowResnCd);
				
				//3) tbody에 붙인다
				wlogListTable.append(newRow);
			}
			//End
		}, error: function(error) {
			console.log("error: ", error);
		}
	})
	//End
}
//End : 검색 버튼 누르면 검색