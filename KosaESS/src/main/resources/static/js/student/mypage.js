$(document).ready(function() {
	function updateTable() {
		$.ajax({
			type: 'GET',
			url: '/student/mypage/aplyList',
			success: function(data) {
				// 받은 데이터로 테이블을 업데이트합니다.
				const time = Date.now();
				var tbody = $('.applyTable tbody');
				tbody.empty(); // tbody 내용을 비웁니다.

				for (var i = 0; i < data.length; i++) {
					var ApplyDetailDTO = data[i];
					var row = $('<tr class="aplyRow"></tr>');

					row.append('<td><span >' + (i + 1) + '</span></td>');
					row.append('<td>' + ApplyDetailDTO.clssNm + '</td>');
					row.append('<td><span>' + ApplyDetailDTO.aplyStartDd + '<br> ~ ' + ApplyDetailDTO.aplyEndDd + '</span></td>');
					row.append('<td><span>' + ApplyDetailDTO.clssStartDd + '<br> ~ ' + ApplyDetailDTO.clssEndDd + '</span></td>');
					row.append('<td>' + ApplyDetailDTO.limitCnt + '</td>');
					row.append('<td>' + ApplyDetailDTO.rgstDd + '</td>');
					row.append('<td>' + ApplyDetailDTO.cmcdNm + '</td>');
					row.append('<td><button class="update">수정</button></td>');
					row.append('<td><button class="cancel">지원취소</button></td>');
					row.append('<td><button class="apply">수강</button></td>');
					row.append('<td><button class="drop">수강포기</button></td>');
					row.append('<td style="display:none;"><span>' + ApplyDetailDTO.aplyId + '</span></td>')

					var updateBtn = row.find('.update');
					var cancelBtn = row.find('.cancel');
					var aplyBtn = row.find('.apply');
					var dropBtn = row.find('.drop');

					if (ApplyDetailDTO.aplyCd === 'APL0000001' || ApplyDetailDTO.aplyCd === 'APL0000004' || ApplyDetailDTO.aplyCd === 'APL0000005' || ApplyDetailDTO.aplyCd === 'APL0000006') {
						updateBtn.hide();
						cancelBtn.hide();
						aplyBtn.hide();
						dropBtn.hide();
					}

					if (ApplyDetailDTO.aplyCd === 'APL0000002') {
						if (ApplyDetailDTO.aplyDate < time) { // aplyDate가 time을 지났을 때
							updateBtn.prop('disabled', true);
							cancelBtn.prop('disabled', false);
							aplyBtn.prop('disabled', true);
							dropBtn.prop('disabled', true);
						} else {
							updateBtn.prop('disabled', false);
							cancelBtn.prop('disabled', false);
							aplyBtn.prop('disabled', true);
							dropBtn.prop('disabled', true);
						}
					}

					if (ApplyDetailDTO.aplyCd === 'APL0000003') {
						updateBtn.prop('disabled', true);
						cancelBtn.prop('disabled', true);
						aplyBtn.prop('disabled', false);
						dropBtn.prop('disabled', false);
					}

					tbody.append(row);
				}
			}
		});
	}

	updateTable();
	var modal = $('.modal');
	var applyTable = $('.applyTable');

	applyTable.on('click', '.update', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();
		modal.show();

		// 모달 내부의 .aplyBtn 버튼 클릭 시
		modal.on('click', '.aplyBtn', function() {
			let formData = new FormData();
			let file = document.querySelector("#fileInput").files[0]; // 파일 인풋 필드에서 파일을 가져옴
			formData.append("formData", file); // FormData에 파일 추가

			$.ajax({
				type: 'POST',
				url: '/student/mypage/uploadFile/' + aplyId,
				data: formData, // FormData 사용
				processData: false,
				contentType: false,
				success: function() {
					modal.hide(); // 모달을 숨기기 위해 hide() 메서드 사용
					alert("지원서를 수정하였습니다.");
					updateTable(); // 성공한 후 테이블 업데이트
				}
			});
		});
	});
	modal.on('click', '.closeModalBtn', function() {
		modal.hide();
	});

	applyTable.on('click', '.cancel', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();

		$.ajax({
			type: 'POST',
			url: '/student/mypage/aplyList/update',
			data: {
				aplyCd: 'APL0000001',
				aplyId: aplyId
			},
			success: function() {
				alert("지원을 취소하셨습니다.");
				updateTable(); // 성공한 후 테이블 업데이트
			}
		});
	});



	applyTable.on('click', '.apply', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();

		$.ajax({
			type: 'POST',
			url: '/student/mypage/aplyList/update',
			data: {
				aplyCd: 'APL0000005',
				aplyId: aplyId
			},
			success: function() {
				$.ajax({
					type: 'POST',
					url: '/student/insert/rgstList',
					data: {
						aplyId: aplyId
					},
					success: function() {
						alert("수강이 확정되었습니다.");
						updateTable();
					}
				});
			}
		});
	});

	applyTable.on('click', '.drop', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();

		$.ajax({
			type: 'POST',
			url: '/student/mypage/aplyList/update',
			data: {
				aplyCd: 'APL0000006',
				aplyId: aplyId
			},
			success: function() {
				alert("수강을 포기하셨습니다.");
				updateTable(); // 성공한 후 테이블 업데이트
			}
		});
	});
});



