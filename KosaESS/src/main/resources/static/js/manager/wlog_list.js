var chkAll = $("#chkAll");//"전체" 체크박스
var chkList = $("input[class=chkWokCd]");//출석 상태 체크박스 리스트
var isChecked = chkAll.prop("checked");//"전체" 체크박스의 체크 상태(true 또는 false)
var allChecked = chkList.filter(":checked").length === chkList.length;
var wlogListTable = document.getElementById('wlog_list_tbody');//tbody 선택하기
var date = new Date();
var year = date.getFullYear();

//오늘 기준 이번 달의 첫 날 반환
function getFirstDay() { var month = ("0" + (1 + date.getMonth())).slice(-2); var day = "01"; return year + "-" + month + "-" + day; }
//오늘 기준 이번 달의 마지막 날 반환
function getLastDay() { var month = ("0" + (1 + date.getMonth())).slice(-2); var lastDay = new Date(year, month, 0).getDate(); return year + "-" + month + "-" + lastDay; }
//tbody의 모든 내용을 삭제
function clearTable() {
	if (wlogListTable.firstChild != null) {
		while (wlogListTable.firstChild) {
			wlogListTable.removeChild(wlogListTable.firstChild);
		}
	}
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

//---------------------------------------------------------------------------
// 1. 체크박스 컨트롤
$(document).ready(
	//document.getElementById('classId').value = document.getElementById('classId').options[document.getElementById('classId').selectedIndex];
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
function showModal() {
	var outputString = "Show Modal";
	console.log(outputString);
	outputString = outputString + "!";
}

function search() {
	//1) tbody의 내용물을 비운다
	clearTable();

	// 교육과정이 선택되지 않았다면 알림을 띄우고 탈출한다
	if(targetClassId == null){
		$(".look_at_me").css("display","flex");
		$(".look_at_me").fadeIn(200);
		$(".look_at_me .read_this").text("교육과정을 선택하세요");
		$(".look_at_me").delay(800);
		$(".look_at_me").fadeOut(200);
		return;
	}
	
	//2) tbody에 붙일 내용물을 만든다
	var targetClassId = document.getElementById('class_selector').options[document.getElementById('class_selector').selectedIndex].value.split("(")[1].split(",")[0].split("=")[1];
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;
	var stdtNm = document.getElementById("search_by_stdt_name").value;
	var wlogCd = getFilterCd();
	if (startDate == '') { startDate = getFirstDay(); }
	if (endDate == '') { endDate = getLastDay(); }

	console.log(targetClassId);
	console.log(startDate);
	console.log(endDate);
	console.log(wlogCd);
	console.log(stdtNm);

	$.ajax({
		type: 'get',
		url: '/manager/wlog/search', // 서버의 엔드포인트 URL
		// 			/*data: { classId<<컨트롤러에서 받는 위치: classId<<클라이언트가 보내는거})*/
		data: {
			classId: targetClassId
			, startDate: startDate
			, endDate: endDate
		},
		async: false,
		success: function(stdtListResponse) {
			clearTable();

			//교육과정명 변경 
			$(".title_a").text(stdtListResponse.thisClassVO.clssNm);
			$(".title_a").attr("id", "className");
			$(".title_a").val(stdtListResponse.thisClassVO.clssId);
			$(".title_a").attr("href", "/manager/class/" + stdtListResponse.thisClassVO.clssId);

			//인원 수 입력
			var stdtList = stdtListResponse.stdtList;
			document.getElementById("stdtCnt").innerHTML = stdtList.length;
			//End

			//입력 시작
			for (let i = 0; i < stdtList.length; i++) {
				var newRow = document.createElement("tr");
				newRow.className = "stdt_list_table_row";

				//체크박스 생성
				var inputChk = document.createElement('input');
				inputChk.value = stdtList[i].stdtId;
				inputChk.setAttribute('name', 'chkStdt');
				inputChk.type = 'checkBox';

				var rowChk = document.createElement('td');
				rowChk.appendChild(inputChk);//체크박스
				var rowNum = document.createElement('td');
				rowNum.innerHTML = i + 1;//행 번호
				var rowName = document.createElement('td');
				rowName.innerHTML = stdtList[i].stdtNm;//이름
				var rowEmail = document.createElement('td');
				rowEmail.innerHTML = stdtList[i].userEmail;//이메일
				var rowTel = document.createElement('td');
				rowTel.innerHTML = stdtList[i].stdtTel;//전화번호
				var rowGender = document.createElement('td');
				rowGender.innerHTML = stdtList[i].genderCd;//성
				var rowBirth = document.createElement('td');
				rowBirth.innerHTML = stdtList[i].birthDd;//생일

				newRow.appendChild(rowChk);
				newRow.appendChild(rowNum);
				newRow.appendChild(rowName);
				newRow.appendChild(rowEmail);
				newRow.appendChild(rowTel);
				newRow.appendChild(rowGender);
				newRow.appendChild(rowBirth);
				$.ajax({
					type: 'get',
					url: '/manager/student/search/codename',
					async: false,
					success: function(wlogResponse) {
						wlogCodeNameList = wlogResponse.wlogCodeList;
						for (let j = 0; j < wlogCodeNameList.length; j++) {
							var rowWlog = document.createElement('td');
							rowWlog.innerHTML = stdtList[i].wlogCnt.split(',')[j].substr(10);
							newRow.appendChild(rowWlog);//출결상태
						}
					}, error: function(error) { console.log("error: ", error); }
				})

				//▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽수정 필요
				var cmptRate = document.createElement('td');
				cmptRate.innerHTML = (stdtList[i].cmptRate).toFixed(1) + " %";
				//△△△△△△△△△△△△△△△△△△△△△△△△△△△수정 필요

				var rowJob = document.createElement('td');
				rowJob.innerHTML = stdtList[i].jobCd;

				newRow.appendChild(cmptRate);
				newRow.appendChild(rowJob);
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