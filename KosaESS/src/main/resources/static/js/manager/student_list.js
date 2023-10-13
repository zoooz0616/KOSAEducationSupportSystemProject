var paramList = new URLSearchParams(location.search);
var thisClassId = paramList.get('classId');

var chkAll = $("#chkAllStdt");//"전체" 학생 체크박스
var chkList = $("input[name=checkedStdt]");//존재하는 stdt체크박스 리스트
var isChecked = chkAll.prop("checked");//"전체(학생)" 체크박스의 상태값(true 아니면 false)
var allChecked = chkList.filter(":checked").length === chkList.length;//학생 체크박스가 전체 체크됐는지 여부

var chkAllcmpt = $("#chk_all_cmpt");//"전체" 이수상태 체크박스
var chkcmptList = $("input[class=cmpt_checkbox]");//존재하는 cmpt리스트
var chkcmptsaveList = $("input[class=cmpt_checkbox_save]");//존재하는 cmpt리스트
var chkAllcmptChecked = chkAllcmpt.prop("checked");//"전체(학생)" 체크박스의 상태값(true 아니면 false)
var chkcmptAllChecked = chkcmptList.filter(":checked").length === chkcmptList.length;//학생 체크박스에서 체크 된 리스트

var stdtListTable = document.getElementById('stdt_list_table_tbody');//tbody 선택하기
var date = new Date();
var year = date.getFullYear();

var radioBtns = $("input[name=default_period]");

var selectClassId = $("#classId");

//오늘 기준 이번 주의 월요일, 일요일을 각각 반환
function getThisWeek(){
	var currentDay = new Date();  
	var theYear = currentDay.getFullYear();
	var theMonth = currentDay.getMonth();
	var theDate  = currentDay.getDate();
	var theDayOfWeek = currentDay.getDay();
	var thisWeek = [];
	for(let i=0; i<2; i++) {
		var resultDay = new Date(theYear, theMonth, theDate - (theDayOfWeek) + i*6 + 1);
		var yyyy = resultDay.getFullYear();
		var mm = Number(resultDay.getMonth()) + 1;
		var dd = resultDay.getDate();
		mm = String(mm).length === 1 ? '0' + mm : mm;
		dd = String(dd).length === 1 ? '0' + dd : dd;
		thisWeek[i] = yyyy + '-' + mm + '-' + dd;
	}
	return thisWeek;
}

//classId != null ? this_class_period.checked = true : false
function checkClassPeriod(){
	if(thisClassId!=null){
		$("input:radio[id=this_class_period]").prop('checked',true)
	}
}

//오늘 기준 이번 달의 첫 날 반환
function getFirstDayMonth(){var month = ("0" + (1 + date.getMonth())).slice(-2);var day = "01";return year + "-" + month + "-" + day;}
//오늘 기준 이번 달의 마지막 날 반환
function getLastDayMonth(){var month = ("0" + (1 + date.getMonth())).slice(-2);var lastDay = new Date(year,month, 0).getDate();return year + "-" + month + "-" + lastDay;}
//tbody의 모든 내용을 삭제
function clearTable(){if(stdtListTable.firstChild != null){while (stdtListTable.firstChild) {stdtListTable.removeChild(stdtListTable.firstChild);}}}

//현재 체크된 라디오 버튼의 값에 따라 start/end Date를 변경
function setSearchPeriod(){
	if($("input[name='default_period']:checked").val()=="thisClassPeriod"){
		if($('#classId').prop('selectedIndex')==0){
			alertFade("교육과정을 선택하세요.","F9DCCB","FF333E");
			$("input:radio[name=default_period]").prop('checked',false)
			return;
		}
		$('#startDate').val($('#start_date_save').val());
		$('#endDate').val($('#end_date_save').val());
	} else if ($("input[name='default_period']:checked").val()=="thisMonth") {
		$('#startDate').val(getFirstDayMonth());
		$('#endDate').val(getLastDayMonth());
	}else{
		/*
		$('#startDate').val(getThisWeek()[0]);//월요일 넣기
		$('#endDate').val(getThisWeek()[6]);//일요일 넣기
		*/
		$('#startDate').val(getThisWeek()[0]);//월요일 넣기
		$('#endDate').val(getThisWeek()[1]);//일요일 넣기
	}
}

//현재 체크된 교육생 목록을 반환
function getCheckedStdt() {
	const checkedStdts = [];
	chkList.each(function () {
		if ($(this).prop("checked")) {
			checkedStdts.push($(this).val());
		}
	});
	return checkedStdts;
}
//End

//현재 체크된 이수 상태 목록을 반환
function getCheckedCmpts() {
	const checkedCmpts = [];
	checkedCmpts.push('CMPT');
	chkcmptList.each(function () {
		if ($(this).prop("checked")) {
			checkedCmpts.push($(this).val());
		}
	});
	return checkedCmpts;
}
//End

//현재 체크된 이수 상태 목록을 반환
function getCheckedSavedCmpts() {
	const checkedCmpts = [];
	checkedCmpts.push('CMPT');
	chkcmptsaveList.each(function () {
		if ($(this).prop("checked")) {
			checkedCmpts.push($(this).val());
		}
	});
	return checkedCmpts;
}
//End

//주소의 classId param이 null이 아니라면 hidden targetClassId를 변경
function changeTargetClassId(){
	if(thisClassId != null){
		$('#class_id_save').val(thisClassId);
	}
}
//End

//주소의 classId param이 null이 아니라면 select 박스의 값을 param의 id와 같은 것으로 변경
function changeSelectBox(){
	if(thisClassId != null){
		for(let i = 0;i < document.getElementById('classId').options.length;i++){
			//console.log((String)((String)(document.getElementById('classId').options[i].value.split("(")[1]).split(",")[0]).split("=")[1]);
			if(thisClassId == (String)((String)(document.getElementById('classId').options[i].value.split("(")[1]).split(",")[0]).split("=")[1]){
				document.getElementById('classId').selectedIndex=i;
				//$("#classId option").prop("selected",true);
				return;
			}
		}
	}
}
//End

//classId!=null ? date = start & end date : null 
function initStartDate(){
	if(thisClassId != null){
		$('#startDate').val($('#start_date_save').val());
		$('#endDate').val($('#end_date_save').val());
	}
}

//초기화 버튼에 필요한 함수 할당
function reset(){
	// 검색 결과는 그대로
	
	// 날짜 초기화(null), 라디오 버튼 초기화 
	$('#startDate').val(null);
	$('#endDate').val(null);
	$("input:radio[name=default_period]").prop('checked',false)

	//교육과정 선택상자 0번 선택
	document.getElementById('classId').selectedIndex=0;
	
	// 이수상태 체크박스 전부 체크
	chkAllcmpt.prop("checked", true);
	$('.cmpt_checkbox').prop("checked", true);
	
	// 검색어 null 
	$('.input_keyword').val(null)
}

//---------------------------------------------------------------------------
// 1. 체크박스 컨트롤
$(document).ready(
	//document.getElementById('classId').value = document.getElementById('classId').options[document.getElementById('classId').selectedIndex];
	
	// startDate가 바뀌면 endDate의 min을 변경
	$('#startDate').on("change", function() {
		$('#endDate').prop("min",$('#startDate').val());
	}),
	
	//라디오버튼으로 검색 기간을 자동 입력
	radioBtns.change( function(){
		setSearchPeriod();
	}),
	
	//classId 선택을 변경할 경우, 숨겨진 시작/종료 일자를 저장하고 라디오버튼의 체크를 해제
	selectClassId.change(function(){
		let targetClassStartDate = ((String)($("#classId").val().split(",")[10])).split("=")[1]
		let targetClassEndDate = ((String)($("#classId").val().split(",")[11])).split("=")[1]
		$("#start_date_save").val(targetClassStartDate);
		$("#end_date_save").val(targetClassEndDate);
		$("input:radio[name=default_period]").prop('checked',false)
	}),
	
	//검색 기간을 직접 변경하면 라디오버튼 해제
	$('#startDate').change(function(){
		$("input:radio[name=default_period]").prop('checked',false)
	}),
	$('#endDate').change(function(){
		$("input:radio[name=default_period]").prop('checked',false)
	}),
	
	//"전체"(교육생) 체크박스의 상태에 따라 나머지 교육생 체크박스의 상태를 변경
	chkAll.on("change", function () {
		isChecked = chkAll.prop("checked");
		chkList.prop("checked", isChecked);
	}),
	//End
	
	//교육생 체크박스가 변경될 때 "전체"(교육생) 체크박스 상태 업데이트
	chkList.on("change", function () {
		allChecked = chkList.filter(":checked").length === chkList.length;
		chkAll.prop("checked", allChecked);
	}),
	//End

	//"전체"(이수 상태) 체크박스의 상태에 따라 나머지 이수 상태 체크박스의 상태를 변경
	chkAllcmpt.on("change", function () {
		chkAllcmptChecked = chkAllcmpt.prop("checked");
		chkcmptList.prop("checked", chkAllcmptChecked);
	}),
	//End
	
	//이수 상태 체크박스가 변경될 때 "전체"(이수 상태) 체크박스 상태 업데이트
	chkcmptList.on("change", function () {
		chkcmptallChecked = chkcmptList.filter(":checked").length === chkcmptList.length;
		chkAllcmpt.prop("checked", chkcmptallChecked);
	}),
	//End
	
	//주소의 classId Param이 null 아니라면, 해당 id에 해당하는 이름으로 select 박스 값으로 선택하기
	changeTargetClassId(),
	//End
	
	//주소의 classId param이 null이 아니라면 hidden select 박스의 값을 param의 id와 같은 것으로 변경
	changeSelectBox(),
	//End
	
	//classId!=null ? date = start & end date : null 
	initStartDate(),
	//End
	
	//classId != null ? this_class_period.checked = true : false
	checkClassPeriod(),
	//End
	
	//교육과정을 선택했을 경우, date가 null이라면 시작~종료를 default로 지정해주기
	$('#classId').on("change", function () {
		if(($('#startDate').val()=="")&&($('#endDate').val()=="")){
			$('#startDate').val($('#start_date_save').val());
			$('#endDate').val($('#end_date_save').val());
		}
	}),
	//End
	
);
//End : 체크박스 컨트롤

// 2. 검색 누르면 검색하세요
	function search(){
		if((document.getElementById('classId').selectedIndex == 0)){
			alertFade("교육과정을 선택하세요.","F9DCCB","FF333E");
			return;
		}
		if(($("#startDate").val()=="")||($("#endDate").val()=="")){
			alertFade("검색 기간을 입력하세요.","F9DCCB","FF333E");
			return;
		}
		var targetClassId = document.getElementById('classId').options[document.getElementById('classId').selectedIndex].value.split("(")[1].split(",")[0].split("=")[1];
		//var targetClassId = thisClassId;
		
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
		//if(startDate==''){startDate=getFirstDay();}
		//if(endDate==''){endDate=getLastDay();}
		
		var inputKeyword = $('.input_keyword').val();
		var cmptList = getCheckedCmpts();
		
		$.ajax({
			type:'get',
			url:'/manager/student/search', // 서버의 엔드포인트 URL
// 			/*data: { classId<<컨트롤러에서 받는 위치: classId<<클라이언트가 보내는거})*/
			data: {
				classId:targetClassId
 				, startDate:startDate
 				, endDate:endDate
				, keyword:inputKeyword
				, cmptList:cmptList
			},
			async:false,
			success: function(stdtListResponse) {
				//인원 수 입력
				var stdtList = stdtListResponse.stdtList;
				
				alertFade(stdtList.length+"명이 검색되었습니다.","CFDEE6","0E5881");
				
				clearTable();
				$("#chkAllStdt").prop("checked",false);
				
				
				//교육과정명 변경 
				$(".title_a").text(stdtListResponse.thisClassVO.clssNm);
				$(".title_a").attr("id", "className");
				$(".title_a").val(stdtListResponse.thisClassVO.clssId);
				$(".title_a").attr("href", "/manager/class/"+stdtListResponse.thisClassVO.clssId);
				
				if(stdtList.length != 0){
					document.getElementById("stdtCnt").innerHTML=stdtList.length;
					$('.stdt_count_unit').css("visibility","visible")
				}else{
					document.getElementById("stdtCnt").innerHTML="검색 결과가 없습니다.";
					$('.stdt_count_unit').css("visibility","hidden")
				}
				//End

				//입력 시작
				for(let i = 0; i < stdtList.length; i++){
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
					rowNum.innerHTML=i+1;//행 번호
					var rowName = document.createElement('td');
					rowName.innerHTML=stdtList[i].stdtNm;//이름
					var rowEmail = document.createElement('td');
					rowEmail.innerHTML=stdtList[i].userEmail;//이메일
					var rowTel = document.createElement('td');
					rowTel.innerHTML=stdtList[i].stdtTel;//전화번호
					var rowGender = document.createElement('td');
					rowGender.innerHTML=stdtList[i].genderCd;//성
					var rowBirth = document.createElement('td');
					rowBirth.innerHTML=stdtList[i].birthDd;//생일

					newRow.appendChild(rowChk);
					newRow.appendChild(rowNum);
					newRow.appendChild(rowName);
					newRow.appendChild(rowEmail);
					newRow.appendChild(rowTel);
					newRow.appendChild(rowGender);
					newRow.appendChild(rowBirth);
					$.ajax({
						type:'get',
						url:'/manager/student/search/codename',
						async:false,
						success: function(wlogResponse) {
						wlogCodeNameList = wlogResponse.wlogCodeList;
						for (let j = 0; j < wlogCodeNameList.length; j++) {
							var rowWlog = document.createElement('td');
							rowWlog.innerHTML=stdtList[i].wlogCnt.split(',')[j].substr(10);
							newRow.appendChild(rowWlog);//출결상태
							rowWlog.setAttribute("class","number_cell")
						}
						},error: function(error){console.log("error: ", error);}
					})
					
					//▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽수정 필요
					var cmptRate = document.createElement('td');
					cmptRate.innerHTML=(stdtList[i].cmptRate).toFixed(1);
					//△△△△△△△△△△△△△△△△△△△△△△△△△△△수정 필요
					
					var rowJob = document.createElement('td');
					rowJob.innerHTML=stdtList[i].jobCd;
					
					newRow.appendChild(cmptRate);
					newRow.appendChild(rowJob);
					//3) tbody에 붙인다
					stdtListTable.append(newRow);
					
					rowChk.setAttribute("class","fixed_length_cell");
					rowNum.setAttribute("class","fixed_length_cell");
					rowName.setAttribute("class","fixed_length_cell");
					rowTel.setAttribute("class","fixed_length_cell");
					rowGender.setAttribute("class","fixed_length_cell");
					rowBirth.setAttribute("class","fixed_length_cell");
					cmptRate.setAttribute("class","number_cell");
					rowJob.setAttribute("class","fixed_length_cell");
				}
				//End
			},error: function(error){
				console.log("error: ", error);
			}
		})
		//개별 체크박스가 변경될 때 "전체" 체크박스 상태 업데이트
		chkList = $("input[name=chkStdt]");//존재하는 stdt체크박스 리스트
		isChecked = chkAll.prop("checked");//체크된 stdt체크박스 리스트
		chkAll.prop("checked", false);
		
		chkList.on("change", function () {
			allChecked = chkList.filter(":checked").length === chkList.length;
			chkAll.prop("checked", allChecked);
		})
		//End
		
		//이수상태 변경이 영향을 받지 않도록...
			//히든 셀렉트 박스 값에 classId 값을 hidden에 저장
		$('#class_id_save').val(document.getElementById('classId').options[document.getElementById('classId').selectedIndex].value.split("(")[1].split(",")[0].split("=")[1]);
			//히든 start/endDate 상자에 날짜를 저장
		$('#start_date_save').val($('#startDate').val());
		$('#end_date_save').val($('#endDate').val());
			//히든 검색어에 검색어 저장
		$('#keyword_save').val($('.input_keyword').val());
			//히든 이수상태 체크박스에 이수상태 저장
		chkcmptList.each(
			function(i, o){
				if(o.checked){
					$("input:checkbox[name="+o.value+"]").prop('checked',true)
				}else{
					$("input:checkbox[name="+o.value+"]").prop('checked',false)
				}
			}
		)
	}
//End : 검색 버튼 누르면 검색

// 3. 이수 상태 변경
	function cmptUpdate(button){
// 		//1. 선택받은 아이들을 가져온다(var targetList)
		var targetList = getCheckedStdt();
// 		//2. 바꿀 값이 뭔지 가져온다(var cmptValue)+어떤 수업인지도 가져온다
		var targetCmptId = button.value;
		var targetClassId = $("#class_id_save").val();
		let targetKeyword = $('#keyword_save').val();
		let targetCmptList = getCheckedSavedCmpts();
		
		console.log(targetKeyword)
		console.log(targetCmptList)
		
// 		//3. target이랑 cmptValue를 넘긴다
		$.ajax({
			//4. update한다
			url : '/manager/student/updateCmpt',
			type :'post',
			data : {
				targetList:targetList
				,clssId:targetClassId
				,targetCmptId:targetCmptId
			},
			success: function(updatedStdtList){
				var startDate = document.getElementById("startDate").value;
				var endDate = document.getElementById("endDate").value;
				//var targetClassId = document.getElementById('classId').options[document.getElementById('classId').selectedIndex].value.split("(")[1].split(",")[0].split("=")[1];
				//========================================================================================
				$.ajax({
					type:'get',
					url:'/manager/student/search', // 서버의 엔드포인트 URL
		// 			/*data: { classId<<컨트롤러에서 받는 위치: classId<<클라이언트가 보내는거})*/
					data: {
						classId:targetClassId
		 				, startDate:startDate
		 				, endDate:endDate
						, keyword:targetKeyword
						, cmptList:targetCmptList
					},
					async:false,
					success: function(stdtListResponse) {
						
						console.log("targetClassId : "+targetClassId);
						
						clearTable();
						var stdtList = stdtListResponse.stdtList;
		
						//입력 시작
						for(let i = 0; i < stdtList.length; i++){
							var newRow = document.createElement("tr");
							
							//체크박스 생성
							var inputChk = document.createElement('input');
							inputChk.value = stdtList[i].stdtId;
							inputChk.setAttribute('name', 'chkStdt');
							inputChk.type = 'checkBox';
							
							var rowChk = document.createElement('td');
							rowChk.appendChild(inputChk);//체크박스
							var rowNum = document.createElement('td');
							rowNum.innerHTML=i+1;//행 번호
							var rowName = document.createElement('td');
							rowName.innerHTML=stdtList[i].stdtNm;//이름
							var rowEmail = document.createElement('td');
							rowEmail.innerHTML=stdtList[i].userEmail;//이메일
							var rowTel = document.createElement('td');
							rowTel.innerHTML=stdtList[i].stdtTel;//전화번호
							var rowGender = document.createElement('td');
							rowGender.innerHTML=stdtList[i].genderCd;//성
							var rowBirth = document.createElement('td');
							rowBirth.innerHTML=stdtList[i].birthDd;//생일
		
							rowChk.className = 'fixed_length_cell';
							rowNum.className ="fixed_length_cell";
							rowName.className ="fixed_length_cell";
							rowTel.className ="fixed_length_cell";
							rowGender.className ="fixed_length_cell";
							rowBirth.className ="fixed_length_cell";
		
							newRow.appendChild(rowChk);
							newRow.appendChild(rowNum);
							newRow.appendChild(rowName);
							newRow.appendChild(rowEmail);
							newRow.appendChild(rowTel);
							newRow.appendChild(rowGender);
							newRow.appendChild(rowBirth);
							$.ajax({
								type:'get',
								url:'/manager/student/search/codename',
								async:false,
								success: function(wlogResponse) {
								wlogCodeNameList = wlogResponse.wlogCodeList;
								for (let j = 0; j < wlogCodeNameList.length; j++) {
									var rowWlog = document.createElement('td');
									rowWlog.innerHTML=stdtList[i].wlogCnt.split(',')[j].substr(10);
									rowWlog.className ="number_cell";
									newRow.appendChild(rowWlog);//출결상태
								}
								},error: function(error){console.log("error: ", error);}
							})
							
							//▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽수정 필요
							var cmptRate = document.createElement('td');
							cmptRate.innerHTML=(stdtList[i].cmptRate).toFixed(1);
							//△△△△△△△△△△△△△△△△△△△△△△△△△△△수정 필요
							
							var rowJob = document.createElement('td');
							rowJob.innerHTML=stdtList[i].jobCd;
							
							cmptRate.className ="number_cell";
							rowJob.className ="fixed_length_cell";
							
							newRow.appendChild(cmptRate);
							newRow.appendChild(rowJob);
							//3) tbody에 붙인다
							stdtListTable.append(newRow);
						}
						//End
					},error: function(error){
						console.log("error: ", error);
					}
				})
				//개별 체크박스가 변경될 때 "전체" 체크박스 상태 업데이트
				chkList = $("input[name=chkStdt]");//존재하는 stdt체크박스 리스트
				isChecked = chkAll.prop("checked");//체크된 stdt체크박스 리스트

				chkList.on("change", function () {
					allChecked = chkList.filter(":checked").length === chkList.length;
					chkAll.prop("checked", allChecked);
				})
				//========================================================================================
			}
			,error: function(error){
				console.log("error: ", error);
			}
		});
		
		//개별 체크박스가 변경될 때 "전체" 체크박스 상태 업데이트
		chkList = $("input[name=chkStdt]");//존재하는 stdt체크박스 리스트
		isChecked = chkAll.prop("checked");//체크된 stdt체크박스 리스트
		
		//"전체" 체크박스의 체크 상태를 해제
// 		chkAll.attr("checked",false);
		
		chkList.on("change", function () {
			allChecked = chkList.filter(":checked").length === chkList.length;
			chkAll.prop("checked", allChecked);
		})
		//End
	}
//End : 이수 상태 변경