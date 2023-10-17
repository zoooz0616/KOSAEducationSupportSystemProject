$(document).ready(
	//교육과정의 연도 select가 바뀌면
	$('#select_year').change( function(){
		//교육과정 select 내부의 option을 싹 비우고
		$('#select_class').empty();
		//목록을 채우기
		$('#select_class').append("<option value='' disabled selected>교육과정명을 선택하세요</option>");
		///*
		$.ajax({
			type: 'get',
			url: '/manager/get_class_list', // 서버의 엔드포인트 URL
			data: {
				year : $('#select_year').val()
			},
			success: function(response) {
				classList = response.classList;
				let newOption;
				$('#result_count').val(classList.length);
				for(let i = 0;i < classList.length;i++){
					newOption = $('<option/>');
					newOption.html(classList[i].clssNm);
					newOption.attr("value",classList[i]);
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
	//테이블을 싹 비우고
	//학생을 채우기
	//지원금도 비우기
	
)

// 등록하기 버튼을 누르면
// HTML에 있는 정보 싹 긁어다 insert 때려넣고
// 입력조건 창 새로고침 하기