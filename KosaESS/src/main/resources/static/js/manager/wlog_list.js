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
var isResnChanged;

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

function selectClassFill() {
	//교육과정 select 내부의 option을 제거
	$('#class_selector').empty();
	//목록을 채우기
	$('#class_selector').append('<option value="" selected>교육과정을 선택하세요</option>');
	$.ajax({
		type: 'get',
		url: '/manager/get_class_list', // 서버의 엔드포인트 URL
		data: {
			//here
			year: $('#select_year').val()
		},
		success: function(response) {
			let classList = response.classList;
			let newOption;
			for (let i = 0; i < classList.length; i++) {
				newOption = $('<option/>');
				newOption.html(classList[i].clssNm);
				newOption.attr("value", "clssId(clssId="+classList[i].clssId+",");
				newOption.attr("clssId", classList[i].clssId);
				newOption.attr("startDd", classList[i].clssStartDd);
				newOption.attr("endDd", classList[i].clssEndDd);
				newOption.attr("value", classList[i].clssId);
				newOption.attr("name", classList[i].clssSubsidy);
				$('#class_selector').append(newOption);
				//console.log("classList[" + i + "] >>> " + classList[i]);
				//console.log("classList[" + i + "].clssStartDd >>> " + classList[i].clssStartDd);
			}
			//총 몇 명인지 체크
		}
		, error: function(error) {
			console.log(error);
		}
	})
}

//오늘 기준 이번 주의 월요일, 일요일을 각각 반환
function getThisWeek() {
	var currentDay = new Date();
	var theYear = currentDay.getFullYear();
	var theMonth = currentDay.getMonth();
	var theDate = currentDay.getDate();
	var theDayOfWeek = currentDay.getDay();
	var thisWeek = [];
	for (let i = 0; i < 2; i++) {
		var resultDay = new Date(theYear, theMonth, theDate - (theDayOfWeek) + i * 6 + 1);
		var yyyy = resultDay.getFullYear();
		var mm = Number(resultDay.getMonth()) + 1;
		var dd = resultDay.getDate();
		mm = String(mm).length === 1 ? '0' + mm : mm;
		dd = String(dd).length === 1 ? '0' + dd : dd;
		thisWeek[i] = yyyy + '-' + mm + '-' + dd;
	}
	return thisWeek;
}

/*
//체크된 출석 상태 반환
function getFilterCd(isSaved) {
	const checkedWokCds = [];
	checkedItems.push('RESN');//이거 없으면 "전체" 해제가 작동하지 않음
	if(isSaved){
		$('.chk_wok_save').each(function() {
			if ($(this).prop("checked")) {
				checkedWokCds.push($(this).val());
			}
		});
	}else{
		chkList.each(function() {
			if ($(this).prop("checked")) {
				checkedWokCds.push($(this).val());
			}
		});
	}
	return checkedWokCds;
}
//End
*/

//현재 체크된 라디오 버튼의 값에 따라 start/end Date를 변경
function setSearchPeriod() {
	if ($("input[name='default_period']:checked").val() == "thisClassPeriod") {
		let thisClassId = $('#class_selector option:selected').val();
		if (thisClassId === "") {
			alertFade("교육과정을 선택하세요.", "F9DCCB", "FF333E");
			$("input:radio[name=default_period]").prop('checked', false)
			return;
		}
		thisClassId = thisClassId.split('(')[1];
		thisClassId = thisClassId.split(',')[0];
		thisClassId = thisClassId.split('=')[1];

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
	} else if ($("input[name='default_period']:checked").val() == "thisMonth") {
		$('#startDate').val(getFirstDay());
		$('#endDate').val(getLastDay());
	} else {
		$('#startDate').val(getThisWeek()[0]);//월요일 넣기
		$('#endDate').val(getThisWeek()[1]);//일요일 넣기
	}
}
//End

//초기화 버튼에 필요한 함수 할당
function reset() {
	// 검색 결과는 그대로

	// 날짜 초기화(null), 라디오 버튼 초기화 
	$('#startDate').val(null);
	$('#endDate').val(null);
	$("input:radio[name=default_period]").prop('checked', false)

	//교육과정 선택상자 0번 선택
	document.getElementById('class_selector').selectedIndex = 0;

	// 이수상태 체크박스 전부 체크
	$('#chkAll').prop("checked", true);
	$('.chkWokCd').prop("checked", true);
	$('#fileContainedOnly').prop("checked", false);
	$('#isDelete').prop("checked", false);

	// 검색어 null 
	$('#search_keyword').val(null)
}

//현재 체크된 출석상태 목록을 반환
function getCheckedItems(isSaved) {
	const checkedItems = [];
	checkedItems.push('RESN');//이거 없으면 "전체" 해제가 작동하지 않음
	if (isSaved) {
		$("input[class=chk_wok_save]").each(function() {
			if ($(this).prop("checked")) {
				checkedItems.push($(this).val());
			}
		});
	} else {
		$("input[class=chkWokCd]").each(function() {
			if ($(this).prop("checked")) {
				checkedItems.push($(this).val());
			}
		});
	}
	return checkedItems;
}

//현재 체크된 wlog 반환
function getCheckedWlog() {
	const checkedItems = [];
	checkedItems.push('WLOG');//이거 없으면 "전체" 해제가 작동하지 않음
	$("input[class=chk_wlog]").each(function() {
		if ($(this).prop("checked")) {
			checkedItems.push($(this).val());
		}
	});
	return checkedItems;
}

function deleteWlogCode() {
	let targetWlogList = getCheckedWlog();

	// 출석 상태 업데이트를 한다
	$.ajax({
		type: 'post',
		url: '/manager/worklog/delete_wlog', // 서버의 엔드포인트 URL
		data: {
			wlogList: targetWlogList
		},
		async: false,
		success: function(response) {
			reload();
		}, error: function(error) {
			console.log("error: ", error);
		}
	})
	// 목록을 다시 출력한다
}

// 출퇴근 기록 리스트 받고, 버튼 값 받아서 상태 업데이트 후 목록 재 출력
function updateWlogCode(button) {
	let targetWlogList = getCheckedWlog();
	let targetWlogCode = button.value;

	// 출석 상태 업데이트를 한다
	$.ajax({
		type: 'post',
		url: '/manager/worklog/update_wlog_code', // 서버의 엔드포인트 URL
		data: {
			wlogList: targetWlogList
			, wlogCd: targetWlogCode
		},
		async: false,
		success: function(response) {
			//console.log(response.result);
			reload();
			alertFade("상태를 변경하였습니다.","CFDEE6","0E5881");
		}, error: function(error) {
			console.log("error: ", error);
			alertFade("상태 변경에 실패하였습니다.","F9DCCB","FF333E");
		}
	})
	// 목록을 다시 출력한다
}

// resn 상태 업데이트(인자가 버튼이면 버튼값으로, 인자가 버튼이 아니라면 그 값으로 업데이트)
function updateResnCode(button) {
	targetResnCode = button;
	isResnChanged = true;
	
	if (typeof targetResnCode === 'object') {
		$.ajax({
			type: 'post',
			url: '/manager/worklog/update_resn_code', // 서버의 엔드포인트 URL
			data: {
				resnId: $('.resn_modal').val()
				, resnCd: targetResnCode.value
			},
			async: false,
			success: function(response) {
				if (response != null) {
					$('#modal_prcs_name').text(button.innerText);
				}
				alertFade("상태를 변경하였습니다.","CFDEE6","0E5881");
				reload();
			}, error: function(error) {
				console.log("error: ", error);
				alertFade("상태 변경에 실패하였습니다.","F9DCCB","FF333E")
			}
		})
	} else {
		$.ajax({
			type: 'post',
			url: '/manager/worklog/update_resn_code', // 서버의 엔드포인트 URL
			data: {
				resnId: $('.resn_modal').val()
				, resnCd: targetResnCode
			},
			async: false,
			success: function(response) {
				if (response != null) {
					$('#modal_prcs_name').text(response.responseName);
					$('#modal_prcs_name').val(response.responseValue);
				}
				alertFade("상태를 변경하였습니다.","CFDEE6","0E5881");
				reload();
			}, error: function(error) {
				console.log("error: ", error);
				alertFade("상태 변경에 실패하였습니다.","F9DCCB","FF333E")
			}
		})
	}
}

//---------------------------------------------------------------------------
// 1. 체크박스 컨트롤
$(document).ready(
	//document.getElementById('classId').value = document.getElementById('classId').options[document.getElementById('classId').selectedIndex];

	//교육과정의 연도가 바뀌면 교육과정 목록을 다시 채우기
	$('#select_year').on("change",function(){
		selectClassFill()
	}),

	//modal 외부를 클릭 시 닫히는 이벤트
	$(document).mouseup(function(e) {
		let container = $('.resn_modal_wrap');
		if (container.has(e.target).length === 0) {
			closeModal();
		}
	}),

	// startDate가 바뀌면 endDate의 min을 변경
	$('#startDate').on("change", function() {
		$('#endDate').prop("min", $('#startDate').val());
	}),

	//라디오뻐튼을 이용해 검색 기간을 자동으로 입력 가능
	radioBtns.change(function() {
		setSearchPeriod();
	}),

	//classId 선택을 변경할 경우, 숨겨진 시작/종료 일자를 저장하고 라디오버튼의 체크를 해제
	$('#class_selector').change(function() {
		let targetClassStartDate = ((String)($('#class_selector').val().split(",")[10])).split("=")[1]
		let targetClassEndDate = ((String)($('#class_selector').val().split(",")[11])).split("=")[1]
		$("#start_date_save").val(targetClassStartDate);
		$("#end_date_save").val(targetClassEndDate);
		$("input:radio[name=default_period]").prop('checked', false)
	}),

	//검색 기간을 직접 변경하면 라디오버튼 해제
	$('#startDate').change(function() {
		$("input:radio[name=default_period]").prop('checked', false)
	}),
	$('#endDate').change(function() {
		$("input:radio[name=default_period]").prop('checked', false)
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
	/*
	,
	chkAll.prop('checked', true)
	,
	chkList.prop('checked', true)
	*/
	//End
);
//End : 체크박스 컨트롤

// 모달창 외부를 클릭 시 모달창을 제거
function closeModal() {
	if (isResnChanged) {
		reload();
	}else{
		let prcsCd = $('#modal_prcs_name').val();
		//미확인이라면 보류로 변경
		if(prcsCd == 'RES0000001'){
			//$('#modal_prcs_name').removeAttr("value")
			updateResnCode('RES0000004');
			reload();
		}
	}
	isResnChanged = false;
	$('.resn_modal_wrap').css("display", 'none');
	$('body').css("overflow", 'auto');
}

// 아이콘을 누르면 모달을 표시
function showModal(resnIcon) {
	let thisResnId = resnIcon.getAttribute("value");
	isResnChanged = false;
	
	$.ajax({
		type: 'get',
		url: '/manager/worklog/resnContent',
		data: {
			resnId: thisResnId
		},
		async: false,
		success: function(response) {
			//console.log(response)
			//console.log(response.resnContent)

			$('.resn_modal_wrap').css("display", 'flex');
			$('body').css("overflow", 'hidden');

			$('.resn_modal').val(response.resnContent.resnId);

			$('#modal_name').text(response.resnContent.stdtNm);
			$('#modal_email').text(response.resnContent.userEmail);
			$('#modal_prcs_name').text(response.resnContent.prcsNm);
			$('#modal_prcs_name').val(response.resnContent.resnCd);

			if (response.resnContent.strInTmDd != null) {
				$('#modal_in_time').html(response.resnContent.strInTmDd);
			} else {
				$('#modal_in_time').html('-')
			}

			if (response.resnContent.strOutTmDd != null) {
				$('#modal_out_time').text(response.resnContent.strOutTmDd);
			} else {
				$('#modal_out_time').text('-')
			}

			$('#modal_wlog_cd').text(response.resnContent.wlogNm);

			$('.resn_text').text(response.resnContent.resnContent);

			$.ajax({
				type: 'get',
				url: '/manager/worklog/resn_file_list',
				async: false,
				data: {
					resnId: thisResnId
				}, success: function(resnFileResponse) {
					resnFileList = resnFileResponse.resnFileList;
					$('.resn_file_list').empty();
					///*
					for (let i = 0; i < resnFileList.length; i++) {
						let resnFileName = $("<li>");
						let resnFileLink = $("<a>");
						let resnFileIcon = $("<img>");
						resnFileIcon.attr('src', "/img/file.png")
						resnFileName.append(resnFileIcon);
						resnFileName.append(resnFileLink);
						resnFileLink.text(resnFileList[i].fileNm);
						resnFileLink.attr('href', "/manager/resn/" + resnFileList[i].fileId + "/" + resnFileList[i].fileSubId);
						$('.resn_file_list').append(resnFileName);
					}
					//*/
				}, error: function(error) {
					console.log("error: ", error);
				}
			});
			$('.resn_controller').empty();
			for (let i = 0; i < response.resnCdList.length; i++) {
				///*
				let newButton = $('<button onclick="updateResnCode(this)" value = "' + response.resnCdList[i].cmcdId + '" >' + response.resnCdList[i].cmcdNm + '</button>')
				$('.resn_controller').append(newButton);
				// */
				//console.log(response.wlogCdList[i].cmcdNm);
			}

		}, error: function(error) {
			console.log("error: ", error);
		}
	})
}

function search() {

	var targetClassId = $('#class_selector option:selected').val();
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;

	/*
	// 교육과정이 선택되지 않았다면 알림을 띄운다
	if(targetClassId == ''){
		alertFade("담당 중인 모든 교육과정의 결과입니다.","F9DCCB","FF333E")
	}
	// End
	// 검색 기간이 선택되지 않았다면 알림을 띄운다
	if(startDate == '' && endDate == ''){
		alertFade("모든 기간에 걸친 검색 결과입니다.","F9DCCB","FF333E")
	}
	// End
	*/

	if (targetClassId != '') {
		targetClassId = targetClassId.split("(")[1];
		targetClassId = targetClassId.split(",")[0];
		targetClassId = targetClassId.split("=")[1];
	} else {
		targetClassId = null
	}

	var keyword = $("#search_keyword").val();
	//var wlogCd = getFilterCd();
	var isDeleteVal = $('#isDelete').is(':checked');
	var resnOnlyVal = $('#fileContainedOnly').is(':checked');
	var filterString = getCheckedItems();

	$.ajax({
		type: 'get',
		url: '/manager/worklog/search', // 서버의 엔드포인트 URL
		data: {
			clssId: targetClassId
			, startDate: startDate
			, endDate: endDate
			//, wlogCd:wlogCd
			, isDelete: isDeleteVal
			, resnOnly: resnOnlyVal
			, keyword: keyword
			, filterString: filterString
		},
		async: false,
		success: function(wlogListResponse) {
			var wlogList = wlogListResponse.wlogList;

			alertFade(wlogList.length + "건이 검색되었습니다.", "9FBCCD", "0E5881");

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
				var rowClssNm = $('<td></td>');
				var rowInTime = $('<td></td>');
				var rowOutTime = $('<td></td>');
				var rowTotalTime = $('<td></td>');
				var rowWlogCd = $('<td></td>');
				var rowResn = $('<td></td>');
				var rowResnCd = $('<td></td>');

				rowChk.html('<input type="checkbox" value=' + wlogList[i].wlogId + '>');
				rowNum.text(i + 1);
				//rowNum.text(wlogList[i].wlogId);//개발자용 옵션
				rowName.text(wlogList[i].stdtNm);
				rowEmail.text(wlogList[i].userEmail);
				rowClssNm.text(wlogList[i].clssNm);

				if (wlogList[i].inTm != null) {
					rowInTime.html(wlogList[i].strInTmDd);
				} else {
					rowInTime.text("-")
				}
				if (wlogList[i].outTm != null) {
					rowOutTime.html(wlogList[i].strOutTmDd);
				} else {
					rowOutTime.text("-")
				}
				rowTotalTime.text(Number(wlogList[i].wlogTotalTm).toFixed(1));
				rowWlogCd.text(wlogList[i].wlogCd);
				if (wlogList[i].resnId != null) {
					rowResn.html(
						$('<img>', {
							src: '/img/file.png'
							, value: wlogList[i].resnId
							, class: 'resn_icon'
							, onclick: "showModal(this)"
						})
					);
				}

				//$('.resn_icon').attr("onclick", "showModal(this)");

				rowResnCd.text(wlogList[i].prcsCd);

				rowChk.attr("class", "fixed_length_cell");
				rowNum.attr("class", "fixed_length_cell");
				rowName.attr("class", "fixed_length_cell");
				rowEmail.attr("class", "variable_length_cell");
				rowClssNm.attr("class", "variable_length_cell");
				rowInTime.attr("class", "fixed_length_cell");
				rowOutTime.attr("class", "fixed_length_cell");
				rowTotalTime.attr("class", "number_cell");
				rowWlogCd.attr("class", "fixed_length_cell");
				rowResn.attr("class", "fixed_length_cell");
				rowResnCd.attr("class", "fixed_length_cell");

				newRow.append(rowChk);
				newRow.append(rowNum);
				newRow.append(rowName);
				newRow.append(rowEmail);
				newRow.append(rowClssNm);
				newRow.append(rowInTime);
				newRow.append(rowOutTime);
				newRow.append(rowTotalTime);
				newRow.append(rowWlogCd);
				newRow.append(rowResn);
				newRow.append(rowResnCd);


				//3) tbody에 붙인다
				wlogListTable.append(newRow);
			}
			//End

			//이수상태 변경이 영향을 받지 않도록 검색 조건 저장
			//히든 셀렉트 박스 값에 classId 값을 hidden에 저장
			let classId = null;
			if ($('#class_selector').val() != null) {
				classId = $('#class_selector').val().match(/clssId=([A-Z0-9]+)/)[1];
			}
			$('#class_id_save').val(classId);
			//히든 start/endDate 상자에 날짜를 저장
			$('#start_date_save').val($('#startDate').val());
			$('#end_date_save').val($('#endDate').val());
			//히든 검색어에 검색어 저장
			$('#keyword_save').val($('#search_keyword').val());
			//히든 이수상태 체크박스에 이수상태 저장
			$('.resn_only_save').prop('checked', $('#fileContainedOnly').prop('checked'))
			$('.contain_delete_save').prop('checked', $('#isDelete').prop('checked'))
			///*
			$("input[class=chkWokCd]").each(
				function(i, o) {
					if (o.checked) {
						$("input:checkbox[name=" + o.value + "]").prop('checked', true)
					} else {
						$("input:checkbox[name=" + o.value + "]").prop('checked', false)
					}
				}
			)
			//*/

		}, error: function(error) {
			console.log("error: ", error);
		}
	})
	//End
}
//End : 검색 버튼 누르면 검색

//save 조건에 맞춰서 재출력
function reload() {
	let savedClassId = $('#class_id_save').val();
	let savedStartDate = $('#start_date_save').val();
	let savedEndDate = $('#end_date_save').val();
	let savedKeyword = $("#keyword_save").val();
	let savedIsDeleteVal = $('.contain_delete_save').is(':checked');
	let savedResnOnlyVal = $('.resn_only_save').is(':checked');

	let savedFilterString = getCheckedItems(true);
	//let savedWlogCd = getFilterCd(true);

	$.ajax({
		type: 'get',
		url: '/manager/worklog/search', // 서버의 엔드포인트 URL
		data: {
			clssId: savedClassId
			, startDate: savedStartDate
			, endDate: savedEndDate
			, isDelete: savedIsDeleteVal
			, resnOnly: savedResnOnlyVal
			, keyword: savedKeyword
			, filterString: savedFilterString
		},
		async: false,
		success: function(wlogListResponse) {
			let wlogList = wlogListResponse.wlogList;

			// 테이블 초기화
			clearTable();

			//입력 시작
			for (let i = 0; i < wlogList.length; i++) {
				// 새 행 생성
				var newRow = $('<tr></tr>');
				newRow.className = "wlog_list_tbody_row";//행 클래스 입력

				var rowChk = $('<td></td>');
				var rowNum = $('<td></td>');
				var rowName = $('<td></td>');
				var rowEmail = $('<td></td>');
				var rowClssNm = $('<td></td>');
				var rowInTime = $('<td></td>');
				var rowOutTime = $('<td></td>');
				var rowTotalTime = $('<td></td>');
				var rowWlogCd = $('<td></td>');
				var rowResn = $('<td></td>');
				var rowResnCd = $('<td></td>');

				rowChk.html('<input type="checkbox" class= "chk_wlog" value=' + wlogList[i].wlogId + '>');
				rowNum.text(i + 1);
				rowName.text(wlogList[i].stdtNm);
				rowEmail.text(wlogList[i].userEmail);

				if (wlogList[i].inTm != null) {
					rowInTime.html(wlogList[i].strInTmDd);
				} else {
					rowInTime.text("-")
				}
				if (wlogList[i].outTm != null) {
					rowOutTime.html(wlogList[i].strOutTmDd);
				} else {
					rowOutTime.text("-")
				}
				rowTotalTime.text(Number(wlogList[i].wlogTotalTm).toFixed(1));
				rowWlogCd.text(wlogList[i].wlogCd);
				rowClssNm.text(wlogList[i].clssNm);
				if (wlogList[i].resnId != null) {
					rowResn.html(
						$('<img>', {
							src: '/img/file.png'
							, value: wlogList[i].resnId
							, class: 'resn_icon'
							, onclick : "showModal(this)"
						})
					);
				}

				//$('.resn_icon').attr("onclick", "showModal(this)");

				rowResnCd.text(wlogList[i].prcsCd);

				rowChk.attr("class", "fixed_length_cell");
				rowNum.attr("class", "fixed_length_cell");
				rowName.attr("class", "fixed_length_cell");
				rowEmail.attr("class", "variable_length_cell");
				rowInTime.attr("class", "fixed_length_cell");
				rowOutTime.attr("class", "fixed_length_cell");
				rowTotalTime.attr("class", "number_cell");
				rowWlogCd.attr("class", "fixed_length_cell");
				rowClssNm.attr("class", "variable_length_cell");
				rowResn.attr("class", "fixed_length_cell");
				rowResnCd.attr("class", "fixed_length_cell");
				//rowResnCd.attr("value", wlogList[i].resnCd);
				rowResnCd.val(wlogList[i].resnCd);

				newRow.append(rowChk);
				newRow.append(rowNum);
				newRow.append(rowName);
				newRow.append(rowEmail);
				newRow.append(rowClssNm);
				newRow.append(rowInTime);
				newRow.append(rowOutTime);
				newRow.append(rowTotalTime);
				newRow.append(rowWlogCd);
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