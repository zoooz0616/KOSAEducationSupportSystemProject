const chkAll = $("#chkAll");
const chkList = $("input[name=chk]");

//현재 체크된 목록을 반환
function getCheckedItems() {
	const checkedItems = [];
	chkList.each(function () {
		if ($(this).prop("checked")) {
			checkedItems.push($(this).val());
		}
	});
	checkedItems.push('CLSS');//이거 없으면 "전체" 해제가 작동하지 않음
	return checkedItems;
}

//체크된 목록을 받아 ajax로 class list 반환
function fetchClassList() {
	var filterString = getCheckedItems();
	var searchKeyword = $("#search_by_name").val();
	$.ajax({
		url : "/manager/class/search", // 컨트롤러 엔드포인트
		type : "GET",
		data : {
			searchKeyword : searchKeyword
			,filterString : filterString
		},
		success : function(classListResponse) {
			//서버에서 받은 응답 파싱하여 객체로 생성
			var filteredClassList = classListResponse.classList;
			var tbody =$("tbody");
			tbody.addClass('class_list_table_tbody');
			tbody.empty();
			
			alertFade(filteredClassList.length+"건이 검색되었습니다.","CFDEE6","0E5881");
			
			for (let i=0;i<filteredClassList.length;i++) {
				
				var newRow = document.createElement('tr');
				newRow.classList.add('class_list_table_row');
				newRow.id='class_list';
				
				var classRowNum =	document.createElement('td');
				var className =		document.createElement('td');
				var classPeriod =	document.createElement('td');
				var classAddress =	document.createElement('td');
				var classCnt =		document.createElement('td');
				var classLimit =	document.createElement('span');
				var classRgst =		document.createElement('a');
				var classClssCd =	document.createElement('td');
				
				classRowNum.innerHTML=i+1;
				var classNameA = document.createElement('a');
				classNameA.innerHTML=filteredClassList[i].clssNm;
				classNameA.setAttribute('href','/manager/class/'+filteredClassList[i].clssId);
				className.appendChild(classNameA);
				
				classPeriod.innerHTML=filteredClassList[i].clssStartDd+" ~ "+filteredClassList[i].clssEndDd;
				classAddress.innerHTML=filteredClassList[i].clssAdr;
				classRgst.innerHTML=filteredClassList[i].rgstCnt;
				classRgst.setAttribute('href','/manager/student?classId='+filteredClassList[i].clssId);
				classLimit.innerHTML="/"+filteredClassList[i].limitCnt;
				classCnt.appendChild(classRgst);
				classCnt.appendChild(classLimit);
				classClssCd.innerHTML=filteredClassList[i].clssCd;
				
				className.setAttribute("class","variable_length_cell");
				classAddress.setAttribute("class","variable_length_cell");
				classClssCd.setAttribute("class","variable_length_cell");
				
				newRow.appendChild(classRowNum);
				newRow.appendChild(className);
				newRow.appendChild(classPeriod);
				newRow.appendChild(classAddress);
				newRow.appendChild(classCnt);
				newRow.appendChild(classClssCd);
				tbody[0].appendChild(newRow);
			}
		},
		error : function(error) {
			console.log("Error : ",error)
			alertFade("검색에 실패하였습니다.","F9DCCB","FF333E");
		}
	});
}

//검색 버튼을 누르면 결과 반영
function search(){
	fetchClassList();
}

//초기화 버튼을 누르면 검색 조건을 초기화
function reset(){
	$('#search_by_name').val("");
	$('input[type=checkbox]').prop("checked",true);
}

//---------------------------------------------------------------
$(document).ready(function () {
	
	// "전체" 체크박스의 상태에 따라 나머지 체크박스의 상태를 변경
	chkAll.on("change", function () {
		const isChecked = chkAll.prop("checked");
		chkList.prop("checked", isChecked);
		//fetchClassList();
	});
	
	//개별 체크박스가 변경될 때 "전체" 체크박스 상태 업데이트
	chkList.on("change", function () {
		const allChecked = chkList.filter(":checked").length === chkList.length;
		chkAll.prop("checked", allChecked);
		//fetchClassList();
	});
});