$(document).ready(
	//교육과정의 연도 select가 바뀌면
	$('#select_year').change(function() {
		//교육과정 select 내부의 option을 싹 비우고
		$('#select_class').empty();
		//목록을 채우기
		$('#select_class').append("<option value='' disabled selected>교육과정명을 선택하세요</option>");
		///*
		$.ajax({
			type: 'get',
			url: '/manager/get_class_list', // 서버의 엔드포인트 URL
			data: {
				year: $('#select_year').val()
			},
			success: function(response) {
				classList = response.classList;
				let newOption;
				$('#result_count').val(classList.length);
				for (let i = 0; i < classList.length; i++) {
					newOption = $('<option/>');
					newOption.html(classList[i].clssNm);
					newOption.attr("value", classList[i]);
					$('#select_class').append(newOption);
				}
				//총 몇 명인지 체크
			}
			, error: function(error) {
				console.log(error);
			}
		})
		//*/
	}),

	//교육과정 select가 바뀌면
	$('#select_class').change(function() {
		//1) 지원금 변경하기
		let targetSubsidy = ($('#select_class').val().split(",")[27]).split("=")[1];
		$('#subsidy_value').text(targetSubsidy);
		console.log(targetSubsidy);
		//2) 테이블을 싹 비우고 학생을 채우기
		//asd?
	}),

	//지급 기준 연 또는 월이 바뀌면 교육일수 option 개수 변경하기 
	$('#start_date_y,#start_date_m').change(function() {
		$('#max_wlog_cnt').empty();
		let newOption;
		switch ($('#start_date_m').val()) {
			case '1' :case '3':case '5':case '7':case '8':case '10':case '12':
				for (let i = 0; i < 31; i++) {
					newOption = $('<option />');
					newOption.html(i + 1);
					newOption.val(i + 1);
					$('#max_wlog_cnt').append(newOption);
				}
				break;
			case '2':
				if($('#start_date_y').val() % 4 == 0){
					for (let i = 0; i < 29; i++) {
						newOption = $('<option />');
						newOption.html(i + 1);
						newOption.val(i + 1);
						$('#max_wlog_cnt').append(newOption);
					}
				}else{
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
	})
)

// 등록하기 버튼을 누르면
// HTML에 있는 정보 싹 긁어다 insert 때려넣고
// 입력조건 창 새로고침 하기