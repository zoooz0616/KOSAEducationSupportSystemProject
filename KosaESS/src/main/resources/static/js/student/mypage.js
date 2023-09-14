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
	$('.applyTable').on('click', '.update', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();

		$.ajax({
			type: 'POST',
			url: '/student/mypage/aplyList/update',
			data: {
				aplyCd: 'APL0000002',
				aplyId: aplyId
			},
			success: function() {
				updateTable(); // 성공한 후 테이블 업데이트
			}
		});
	});

	// jQuery 이벤트 위임을 사용하여 동적으로 생성된 버튼에 이벤트 리스너를 추가합니다.
	$('.applyTable').on('click', '.cancel', function() {
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
				updateTable(); // 성공한 후 테이블 업데이트
			}
		});
	});


	$('.applyTable').on('click', '.apply', function() {
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
						updateTable();
					}
				});
			}
		});
	});

	$('.applyTable').on('click', '.drop', function() {
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
				updateTable(); // 성공한 후 테이블 업데이트
			}
		});
	});
});
