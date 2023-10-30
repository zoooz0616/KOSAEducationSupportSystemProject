function updateInfo() {
	if($('.input_password').val()!=$('.input_password_confirm').val()){
		alertFade("비밀번호 확인이 일치하지 않습니다.","F9DCCB","FF333E")
		return;
	}
	$.ajax({
		type: 'post',
		url: '/manager/mypage/update_info',
		data: {
			inputPassword:$('.input_password').val()
			,confirmPassword:$('.input_password_confirm').val()
			,inputTel:$('.input_tel').val()
		},
		async: false,
		success: function(response) {
			$('.input_password').val('');
			$('.input_password_confirm').val('');
			alertFade(response.message,"CFDEE6","0E5881");
		}
		, error: function(error) {
			console.log(error);
			alertFade(response.message,"F9DCCB","FF333E")
		}
	})
}

function loadWorklog(){
	console.log(Math.floor(Math.random() * 10));
}

$(document).ready(
	loadWorklog(),
	setInterval(loadWorklog, 3000),
	
	//교육과정의 연도 select가 바뀌면
	$('#select_year').change(
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
	)
);
