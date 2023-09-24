var paramList = new URLSearchParams(location.search);
var thisClassId = paramList.get('classId');
var chkAll = $("#chkAllStdt");//전체 체크박스
var chkList = $("input[name=checkedStdt]");//존재하는 stdt체크박스 리스트
var isChecked = chkAll.prop("checked");//체크된 stdt체크박스 리스트
var allChecked = chkList.filter(":checked").length === chkList.length;
var stdtListTable = document.getElementById('stdt_list_table_tbody');//tbody 선택하기
var date = new Date();
var year = date.getFullYear();

//오늘 기준 이번 달의 첫 날 반환
function getFirstDay(){var month = ("0" + (1 + date.getMonth())).slice(-2);var day = "01";return year + "-" + month + "-" + day;}
//오늘 기준 이번 달의 마지막 날 반환
function getLastDay(){var month = ("0" + (1 + date.getMonth())).slice(-2);var lastDay = new Date(year,month, 0).getDate();return year + "-" + month + "-" + lastDay;}
//tbody의 모든 내용을 삭제
function clearTable(){if(stdtListTable.firstChild != null){while (stdtListTable.firstChild) {stdtListTable.removeChild(stdtListTable.firstChild);}}}

//현재 체크된 목록을 반환 <<< 3번 할 때 쓸 예정
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
				console.log(document.getElementById('classId').options[i].seleted);
				break;
			}
		}
	}
}
//End
//---------------------------------------------------------------------------
// 1. 체크박스 컨트롤
$(document).ready(
	//document.getElementById('classId').value = document.getElementById('classId').options[document.getElementById('classId').selectedIndex];
	
	//"전체" 체크박스의 상태에 따라 나머지 체크박스의 상태를 변경
	chkAll.on("change", function () {
		isChecked = chkAll.prop("checked");
		chkList.prop("checked", isChecked);
	}),
	//End
	
	//개별 체크박스가 변경될 때 "전체" 체크박스 상태 업데이트
	chkList.on("change", function () {
		allChecked = chkList.filter(":checked").length === chkList.length;
		chkAll.prop("checked", allChecked);
	}),
	//End
	
	//주소의 classId Param이 null 아니라면, 해당 id에 해당하는 이름으로 select 박스 값으로 선택하기
	changeTargetClassId(),
	//End
	
	//주소의 classId param이 null이 아니라면 select 박스의 값을 param의 id와 같은 것으로 변경
	changeSelectBox()
	//End
	
);
//End : 체크박스 컨트롤

// 2. 검색 누르면 검색하세요
	function search(){
		//1) tbody의 내용물을 비운다
		clearTable();
		$("#chkAllStdt").prop("checked",false);
		
		//2) tbody에 붙일 내용물을 만든다
		var targetClassId = document.getElementById('classId').options[document.getElementById('classId').selectedIndex].value.split("(")[1].split(",")[0].split("=")[1];
		//var targetClassId = thisClassId;
		
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
		if(startDate==''){startDate=getFirstDay();}
		if(endDate==''){endDate=getLastDay();}
		
		$.ajax({
			type:'get',
			url:'/manager/student/search', // 서버의 엔드포인트 URL
// 			/*data: { classId<<컨트롤러에서 받는 위치: classId<<클라이언트가 보내는거})*/
			data: {
				classId:targetClassId
 				, startDate:startDate
 				, endDate:endDate
			},
			async:false,
			success: function(stdtListResponse) {
				clearTable();
				
				//교육과정명 변경 
				$(".title_a").text(stdtListResponse.thisClassVO.clssNm);
				$(".title_a").attr("id", "className");
				$(".title_a").val(stdtListResponse.thisClassVO.clssId);
				$(".title_a").attr("href", "/manager/class/"+stdtListResponse.thisClassVO.clssId);
				
				//인원 수 입력
				var stdtList = stdtListResponse.stdtList;
				document.getElementById("stdtCnt").innerHTML=stdtList.length;
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
						}
						},error: function(error){console.log("error: ", error);}
					})
					
					//▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽수정 필요
					var cmptRate = document.createElement('td');
					cmptRate.innerHTML=(stdtList[i].cmptRate).toFixed(1)+" %";
					//△△△△△△△△△△△△△△△△△△△△△△△△△△△수정 필요
					
					var rowJob = document.createElement('td');
					rowJob.innerHTML=stdtList[i].jobCd;
					
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
		chkAll.prop("checked", false);
		
		chkList.on("change", function () {
			allChecked = chkList.filter(":checked").length === chkList.length;
			chkAll.prop("checked", allChecked);
		})
		//End
		//셀렉트 박스 값에 이수상태 변경이 영향을 받지 않도록 classId 값을 hidden에 저장
		function changeHiddenClassId(){
			$('#class_id_save').val(document.getElementById('classId').options[document.getElementById('classId').selectedIndex].value.split("(")[1].split(",")[0].split("=")[1]);
		}
		changeHiddenClassId();
		//End
	}
//End : 검색 버튼 누르면 검색

// 3. 이수 상태 변경
	function cmptUpdate(button){
// 		//1. 선택받은 아이들을 가져온다(var targetList)
		var targetList = getCheckedStdt();
	
// 		//2. 바꿀 값이 뭔지 가져온다(var cmptValue)+어떤 수업인지도 가져온다
		var targetCmptId = button.value;
		//var thisClassId = $(".title_a").val();
		var targetClassId = $("#class_id_save").val();

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
				clearTable();
				var startDate = document.getElementById("startDate").value;
				var endDate = document.getElementById("endDate").value;
				if(startDate==''){startDate=getFirstDay();}
				if(endDate==''){endDate=getLastDay();}
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
					},
					async:false,
					success: function(stdtListResponse) {
						
						console.log(targetClassId);
						
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
								}
								},error: function(error){console.log("error: ", error);}
							})
							
							//▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽수정 필요
							var cmptRate = document.createElement('td');
							cmptRate.innerHTML=(stdtList[i].cmptRate).toFixed(1)+" %";
							//△△△△△△△△△△△△△△△△△△△△△△△△△△△수정 필요
							
							var rowJob = document.createElement('td');
							rowJob.innerHTML=stdtList[i].jobCd;
							
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