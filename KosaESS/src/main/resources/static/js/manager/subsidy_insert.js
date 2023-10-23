//인 서 트
function insertSubsidy() {
	//대상이 없으면 중지
	if ($('#result_count').text()== 0) {
		alertFade("등록 할 기록이 없습니다.","F9DCCB","FF333E")
		return;
	}
	let subsidyVO = [];
	let dict;
	let targetTr;
	for(let i = 0;i < $('#result_count').text();i++){
		targetTr = $('#stdt_list_table_tbody tr[name=tr'+i+']');
		dict = {};
		dict['clssId']=$('#select_class option:selected').val()
		dict['stdtId']=targetTr.children('td[name="chkbox"]').children('input[type=checkbox]').val();
		dict['sbsdCd']=targetTr.children('td[name=sbsdCd]').children("select").val();
		dict['payment']=targetTr.children('td[name=payment]').children('input').val();
		dict['subsidyDd']=new Date($('#select_ym_y option:selected').val(),$('#select_ym_m option:selected').val()-1); 
		dict['maxWlogCnt']="maxWlogCnt >>> "+$('#max_wlog_cnt option:selected').val();
		dict['wlogCnt']=targetTr.children('td[name=wlogCnt]').text();
		dict['sbsdEtc']=targetTr.children('td[name=sbsdEtc]').children('input[type=text]').val();
		subsidyVO.push(dict);
		
	}
	
	//객체 배열을 json문자열로 직렬화 하기
	var jsonData = JSON.stringify(subsidyVO);
	console.log(jsonData);
	$.ajax({
		url : "/manager/subsidy/insert", // 컨트롤러 엔드포인트
		type : "post"
		//,dataType:"json"
		//,contentType : 'application/json'
		,data : {
			subsidyList: jsonData
		}
		,success : function(response) {
			if(response.result){
				alert("총 "+$('#result_count').text()+"건이 등록되었습니다.");
				location.href('/manager/subsidy');
			} else{
				alertFade("등록에 실패하였습니다.(asd)","F9DCCB","FF333E");
			}
		},
		error : function(error) {
			console.log("Error : ",error)
			alertFade("등록에 실패하였습니다.","F9DCCB","FF333E");
		}
	})
	/*
	//비동기로 배열 전달하기
	fetch('/admin/commoncode/update/groupcode', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: jsonData,
	})
	.then(response => response.json())
	.then(data => {
		console.log(data);
		if (data.message === "success") {
			alertFade(length+"건이 검색되었습니다.","CFDEE6","0E5881");
			//그룹코드리스트 다시 불러오기
			selectGroupCodeList();

		} else {
			alert("실패했습니다. 다시 시도해주세요.");
		}
	})
	.catch(error => {
		// 오류 처리
	});
		*/
}

//연, 월, TF로 월의 첫 또는 마지막 날 반환 
function getFirstDay(year, month, isFirst) {
	month = ("0" + month).slice(-2);
	let day;
	if (isFirst) {
		day = "01";
	} else {
		day = new Date(year, month, 0).getDate();
	}
	return year + "-" + month + "-" + day;
}

/*
//function selectClassFill(year) {
function selectClassFill() {
	let targetYear;
	targetYear = $('#select_year').val();
	if(year == null){
		targetYear = $('#select_year').val();
	}else{
		targetYear = year;
	}
	//교육과정 select 내부의 option을 싹 비우고 지원금을 리셋
	$('#select_class').empty();
	//목록을 채우기
	$('#select_class').append("<option value='' disabled selected>교육과정명을 선택하세요</option>");
	$.ajax({
		type: 'get',
		url: '/manager/get_class_list', // 서버의 엔드포인트 URL
		data: {
			year: targetYear
		},
		success: function(response) {
			let classList = response.classList;
			let newOption;
			for (let i = 0; i < classList.length; i++) {
				newOption = $('<option/>');
				newOption.html(classList[i].clssNm);
				newOption.attr("value", classList[i].clssId);
				newOption.attr("name", classList[i].clssSubsidy);
				$('#select_class').append(newOption);
			}
			//총 몇 명인지 체크
		}
		, error: function(error) {
			console.log(error);
		}
	})
}
*/
$(document).ready(
	//교육과정의 연도 select가 바뀌면
	$('#select_year').change(
		///*
		function() {
			//교육과정 select 내부의 option을 싹 비우고 지원금을 리셋
			$('#select_class').empty();
			$('#subsidy_value').text(0);
			//목록을 채우기
			$('#select_class').append("<option value='' selected>교육과정명을 선택하세요</option>");
			$.ajax({
				type: 'get',
				url: '/manager/get_class_list', // 서버의 엔드포인트 URL
				data: {
					year: $('#select_year').val()
				},
				success: function(response) {
					let classList = response.classList;
					let newOption;
					for (let i = 0; i < classList.length; i++) {
						newOption = $('<option/>');
						newOption.html(classList[i].clssNm);
						newOption.attr("value", classList[i].clssId);
						newOption.attr("name", classList[i].clssSubsidy);
						$('#select_class').append(newOption);
					}
					//총 몇 명인지 체크
				}
				, error: function(error) {
					console.log(error);
				}
			})
		}
		//*/
		//selectClassFill()
	),

	//교육과정 select가 바뀌면
	$('#select_class,#select_ym_y,#select_ym_m, #max_wlog_cnt').change(function() {
		/*
		console.log(typeof $('#select_class option:selected').val());
		console.log($('#select_class option:selected').val());
		*/

		//1_ 지원금 변경하기
		let targetSubsidy = $('#select_class option:selected').attr('name');
		$('#subsidy_value').text(targetSubsidy);
		//2_ 테이블을 싹 비우고
		$('#stdt_list_table_tbody').empty();
		//3_ 학생을 채우기
		let selectedClass = $('#select_class option:selected').val();
		let selectYear = $('#select_ym_y option:selected').val();
		let selectMonth = $('#select_ym_m option:selected').val();
		$.ajax({
			type: 'get',
			url: '/manager/subsidy/get_student_list',
			data: {
				classId: selectedClass
				, startDate: getFirstDay(selectYear, selectMonth, true)
				, endDate: getFirstDay(selectYear, selectMonth, false)
			},
			success: function(response) {
				let stdtList = response.stdtList;
				let sbsdCdList = response.sbsdCdList;
				$('#result_count').text(stdtList.length);
				//console.log($('#result_count').text());
				///*
				let newCell;
				let newRow;
				let chkBox;
				let wlogCnt;
				let sbsdCdSelect;
				let newOption;
				let wlogCntList;
				for (let i = 0; i < stdtList.length; i++) {
					newRow = $('<tr>');
					//체크박스
					newCell = $('<td>');
					chkBox = $('<input type="checkbox">')
					chkBox.attr("value", stdtList[i].stdtId)
					newCell.append(chkBox);
					newCell.attr("class", "fixed_length_cell");
					newCell.attr("name", "chkbox");
					newRow.append(newCell);
					//번호
					newCell = $('<td>');
					newCell.html(i + 1);
					newCell.attr("class", "fixed_length_cell");
					newRow.append(newCell);
					//이름
					newCell = $('<td>');
					newCell.html(stdtList[i].stdtNm);
					newCell.attr("class", "fixed_length_cell");
					newCell.attr("name", 'stdtNm');
					newRow.append(newCell);
					//이메일
					newCell = $('<td>');
					newCell.html(stdtList[i].userEmail);
					newCell.attr("name", 'userEmail');
					newRow.append(newCell);
					//이수상태
					newCell = $('<td>');
					newCell.html(stdtList[i].cmptNm);
					newCell.attr("value", stdtList[i].cmptCd);
					newCell.attr("class", "fixed_length_cell");
					newCell.attr("name", 'cmptCd');
					newRow.append(newCell);
					//출석
					wlogCntList = stdtList[i].wlogCnt.split(",");
					for (let j = 0; j < wlogCntList.length - 1; j++) {
						newCell = $('<td>');
						newCell.html(wlogCntList[j].substr(10, wlogCntList[j].length));
						newCell.attr("class", "number_cell");
						newRow.append(newCell);
						if (wlogCntList[j].substr(0, 10) == 'WOK0000001') {
							wlogCnt = wlogCntList[j].substr(10, wlogCntList[j].length);
							newCell.attr("name",wlogCnt);
						}
					}
					//출석일수
					//ajax
					newCell = $('<td>');
					newCell.html(wlogCnt);
					newCell.attr("class", "number_cell");
					newCell.attr("name", 'wlogCnt');
					newRow.append(newCell);
					//출석률
					newCell = $('<td>');
					newCell.html(Number(wlogCnt * 100.0 / $('#max_wlog_cnt option:selected').val()).toFixed(1));
					newCell.attr("class", "number_cell");
					newRow.append(newCell);
					//지급예정액
					newCell = $('<td>');
					newOption = $('<input type="number" min="0" max="99999999">');
					newOption.val(Number(wlogCnt * targetSubsidy / $('#max_wlog_cnt option:selected').val()).toFixed(0));
					newCell.attr("class", "fixed_length_cell");
					newCell.attr("name", 'payment');
					newCell.append(newOption);
					newRow.append(newCell);
					//지급상태
					newCell = $('<td>');
					sbsdCdSelect = $('<select>');
					for (let j = 0; j < sbsdCdList.length; j++) {
						newOption = $('<option>');
						newOption.html(sbsdCdList[j].cmcdNm);
						newOption.attr("value", sbsdCdList[j].cmcdId);
						//코드값이 지급예정 코드와 일치하면 SELECTED = true
						if (sbsdCdList[j].cmcdId == 'SSD0000001') {
							newOption.attr("selected", true);
						}
						sbsdCdSelect.append(newOption);
					}
					newCell.attr("name", 'sbsdCd');
					newCell.append(sbsdCdSelect);
					newCell.attr("class", "fixed_length_cell");
					newRow.append(newCell);
					//기타사항
					newCell = $('<td>');
					newOption = $('<input type="text">');
					newOption.attr('type', 'text');
					newCell.attr("class", "fixed_length_cell");
					newCell.attr("name", 'sbsdEtc');
					newCell.append(newOption);
					newRow.append(newCell);
					newRow.attr("name",'tr'+i);

					//행 추가
					$('#stdt_list_table_tbody').append(newRow);
				}
				//*/
			}
			, error: function(error) {
				console.log(error);
			}
		})
	}),

	//지급 기준 연 또는 월이 바뀌면 교육일수 option 개수 변경하기 
	$('#select_ym_y,#select_ym_m').change(function() {
		$('#max_wlog_cnt').empty();
		let newOption;
		switch ($('#select_ym_m').val()) {
			case '1': case '3': case '5': case '7': case '8': case '10': case '12':
				for (let i = 0; i < 31; i++) {
					newOption = $('<option />');
					newOption.html(i + 1);
					newOption.val(i + 1);
					$('#max_wlog_cnt').append(newOption);
				}
				break;
			case '2':
				if ($('#select_ym_y').val() % 4 == 0) {
					for (let i = 0; i < 29; i++) {
						newOption = $('<option />');
						newOption.html(i + 1);
						newOption.val(i + 1);
						$('#max_wlog_cnt').append(newOption);
					}
				} else {
					for (let i = 0; i < 28; i++) {
						newOption = $('<option />');
						newOption.html(i + 1);
						newOption.val(i + 1);
						$('#max_wlog_cnt').append(newOption);
					}
				}
				break;
			default:
				for (let i = 0; i < 30; i++) {
					newOption = $('<option />');
					newOption.html(i + 1);
					newOption.val(i + 1);
					$('#max_wlog_cnt').append(newOption);
				}
				break;
		}
	}),

	$('#select_year option:eq(1)').attr('selected', 'selected')
	//,selectClassFill()
)

// 등록하기 버튼을 누르면
// HTML에 있는 정보 싹 긁어다 insert 때려넣고
// 입력조건 창 새로고침 하기