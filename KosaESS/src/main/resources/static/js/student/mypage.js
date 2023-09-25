$(document).ready(function() {
	updateAplyTable();
	updateWlogTable();
	function updateAplyTable() {
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
					row.append('<td><button class="update">수정</button><button class="apply">수강</button><button class="cancel">지원취소</button><button class="drop">수강포기</button></td>');
					row.append('<td style="display:none;"><span>' + ApplyDetailDTO.aplyId + '</span></td>')

					var updateBtn = row.find('.update');
					var cancelBtn = row.find('.cancel');
					var aplyBtn = row.find('.apply');
					var dropBtn = row.find('.drop');

					if (ApplyDetailDTO.aplyCd === 'APL0000001' || ApplyDetailDTO.aplyCd === 'APL0000004' || ApplyDetailDTO.aplyCd === 'APL0000005' || ApplyDetailDTO.aplyCd === 'APL0000006') {
						updateBtn.addClass('hidden');
						cancelBtn.addClass('hidden');
						aplyBtn.addClass('hidden');
						dropBtn.addClass('hidden');
					}

					if (ApplyDetailDTO.aplyCd === 'APL0000002') {
						if (ApplyDetailDTO.aplyDate < time) {
							updateBtn.addClass('hidden');
							cancelBtn.removeClass('hidden');
							aplyBtn.addClass('hidden');
							dropBtn.addClass('hidden');
						} else {
							updateBtn.removeClass('hidden');
							cancelBtn.removeClass('hidden');
							aplyBtn.addClass('hidden');
							dropBtn.addClass('hidden');
						}
					}

					if (ApplyDetailDTO.aplyCd === 'APL0000003') {
						updateBtn.addClass('hidden');
						cancelBtn.addClass('hidden');
						aplyBtn.removeClass('hidden');
						dropBtn.removeClass('hidden');
					}

					tbody.append(row);
				}
			}
		});
	}

	var modal1 = $('.modal1');
	var applyTable = $('.applyTable');

	applyTable.on('click', '.update', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();
		modal1.show();

		// 모달 내부의 .aplyBtn 버튼 클릭 시
		modal1.on('click', '.aplyBtn', function() {
			let formData = new FormData();
			let file = document.querySelector("#fileInput1").files[0]; // 파일 인풋 필드에서 파일을 가져옴
			formData.append("formData", file); // FormData에 파일 추가

			$.ajax({
				type: 'POST',
				url: '/student/mypage/uploadFile/' + aplyId,
				data: formData, // FormData 사용
				processData: false,
				contentType: false,
				success: function() {
					modal1.hide(); // 모달을 숨기기 위해 hide() 메서드 사용
					alert("지원서를 수정하였습니다.");
					updateAplyTable();// 성공한 후 테이블 업데이트
				}
			});
		});
	});
	modal1.on('click', '.closeModalBtn', function() {
		modal1.hide();
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
				updateAplyTable();; // 성공한 후 테이블 업데이트
			}
		});
	});



	applyTable.on('click', '.apply', function() {
		var clickedRow = $(this).closest('tr');
		var aplyId = clickedRow.find('td:hidden span').text();

		$.ajax({
			type: 'POST',
			url: '/student/mypage/aplyList/confirm',
			data: {
				aplyCd: 'APL0000005',
				aplyId: aplyId
			},
			success: function(response) {
				if (response == true) {
					alert("수강이 확정되었습니다.");
					updateAplyTable();
				} else {
					alert("진행중인 수업이 존재합니다. \n 한 개이상의 교육을 수강할 수 없습니다.");
					updateAplyTable();// 성공한 후 테이블 업데이트
				}
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
				updateAplyTable(); // 성공한 후 테이블 업데이트
			}
		});
	});

	function updateWlogTable() {
		$.ajax({
			type: 'GET',
			url: '/student/mypage/wlogList',
			success: function(data) {
				// 받은 데이터로 테이블을 업데이트합니다.
				var tbody = $('.wlogTable tbody');
				tbody.empty(); // tbody 내용을 비웁니다.

				for (var i = 0; i < data.length; i++) {
					var WorklogVO = data[i];
					var row = $('<tr class="wlogRow"></tr>');
					var inTmDd = formatTimestamp(WorklogVO.inTm);
					console.log(inTmDd);
					if (WorklogVO.outTm == null)
						var outTmDd = '-';
					else
						var outTmDd = formatTimestamp(WorklogVO.outTm);

					row.append('<td><span >' + (i + 1) + '</span></td>');
					row.append('<td>' + WorklogVO.clssNm + '</td>');
					row.append('<td>' + inTmDd + '</td>');
					row.append('<td>' + outTmDd + '</td>');
					if (WorklogVO.wlogNm == '정상') {
						row.append('<td>' + WorklogVO.wlogNm + '</td>');
					} else {
						row.append('<td style="color: red;">' + WorklogVO.wlogNm + '</td>');
					}
					row.append('<td><button class="submitResn" >제출</button><button class="updateResn">수정</button></td>');
					row.append('<td class="resnStstus">' + WorklogVO.resnNm + '</td>');
					row.append('<td class="wlogId" style="display:none;">' + WorklogVO.wlogId + '</td>');
					row.append('<td class="resnId" style="display:none;">' + WorklogVO.resnId + '</td>');

					var wlogCd = WorklogVO.wlogCd;
					var resnId = WorklogVO.resnId;
					var submitResnElement = row.find('.submitResn');
					var updateResnElement = row.find('.updateResn');
					var resnStstusElement = row.find('.resnStstus');

					if (wlogCd !== 'WOK0000001') {
						if (resnId == null) {
							submitResnElement.removeClass('hidden');
							updateResnElement.addClass('hidden');
							resnStstusElement.addClass('hidden');
						}
						else {
							submitResnElement.addClass('hidden');
							updateResnElement.removeClass('hidden');
							resnStstusElement.removeClass('hidden');
						}
					} else {
						submitResnElement.addClass('hidden');
						updateResnElement.addClass('hidden');
						resnStstusElement.addClass('hidden');
					}
					tbody.append(row);
				}
			}
		});
	}

	updateWlogTable();
	var modal2 = $('.modal2');
	var wlogTable = $('.wlogTable');

	wlogTable.on('click', '.submitResn', function() {
		var clickedRow = $(this).closest('tr');
		var wlogId = clickedRow.find('td.wlogId').text();
		modal2.show();

		// 모달 내부의 .submitBtn 버튼 클릭 시
		modal2.on('click', '.submitBtn', function() {

			var resnText = $('.resnText1').val();
			var formData = new FormData();
			var file = document.querySelector("#fileInput2").files[0];
			formData.append("file", file);
			formData.append("resnText", resnText)
			console.log(resnText);
			console.log(formData);
			console.log(file);
			$.ajax({
				type: 'POST',
				url: '/student/mypage/submitResn/' + wlogId,
				data: formData,
				processData: false,
				contentType: false,
				success: function() {
					modal2.hide();
					alert("사유서를 제출하였습니다.");
					updateWlogTable();

				}
			});
		});

		modal2.on('click', '.closeBtn', function() {
			modal2.hide();
		});
	});

	var modal3 = $('.modal3');

	wlogTable.on('click', '.updateResn', function() {
		var clickedRow = $(this).closest('tr');
		var resnId = clickedRow.find('td.resnId').text();
		modal3.show();

		// 모달 내부의 .submitBtn 버튼 클릭 시
		modal3.on('click', '.submitBtn', function() {
			var resnText = $('.resnText2').val();
			var formData = new FormData();
			var file = document.querySelector("#fileInput3").files[0];
			formData.append("file", file);
			formData.append("resnText", resnText)
			console.log(resnText);
			console.log(formData);
			console.log(file);

			$.ajax({
				type: 'POST',
				url: '/student/mypage/uploadResn/' + resnId,
				data: formData,
				processData: false,
				contentType: false,
				success: function() {
					modal3.hide();
					alert("사유서를 업데이트하였습니다.");
					updateWlogTable();

				}
			});
		});

		modal3.on('click', '.closeBtn', function() {
			modal3.hide();
		});
	});
});

function formatTimestamp(timestamp) {
	const date = new Date(timestamp);

	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');
	const seconds = String(date.getSeconds()).padStart(2, '0');

	const formattedTimestamp = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

	return formattedTimestamp;
}

$("#fileInput1").on('change',function(){
  var fileName = $("#fileInput1").val();
  $(".upload-name1").val(fileName);
});

$("#fileInput2").on('change',function(){
  var fileName = $("#fileInput2").val();
  $(".upload-name2").val(fileName);
});

$("#fileInput3").on('change',function(){
  var fileName = $("#fileInput3").val();
  $(".upload-name3").val(fileName);
});

$('#passwordInput').on('keyup', function(event) {
		if (event.key === "Enter") {
			$('#checkPassword').trigger('click');
		}
	});