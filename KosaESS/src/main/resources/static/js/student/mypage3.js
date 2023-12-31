$(document).ready(function() {
	var countPerPage3 = 10; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData3 = [];

	updateWlogTable();
	var selectedValue = '';

	$('#wlogClssNm').change(function() {
		selectedValue = $(this).val();

		updateWlogTable();
	});

	function updateWlogTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/wlogList',
			data: {
				selectedClssNm: selectedValue
			},
			success: function(data) {
				// 받은 데이터로 테이블을 업데이트합니다.
				todoData3 = data;
				$('.wlogCnt').text(todoData3.length);
				setTable(1);
				setPaging(1);

				if (data.length == 0)
					$('.wlogFoot').show();
				else
					$('.wlogFoot').hide();
			}
		});
	};
	// 페이지 번호 클릭 이벤트 핸들러
	$(document).on('click', '.pages3>span', function() {
		if (!$(this).hasClass('active')) {
			$(this).parent().find('span.active').removeClass('active');
			$(this).addClass('active');

			setTable(Number($(this).text()));
		}
	});
	$(document).on('click', '.paging3>i', function() {
		const totalPage = Math.floor(todoData3.length / countPerPage3) + (todoData3.length % countPerPage3 == 0 ? 0 : 1);
		const id = $(this).attr('id');
		//console.log(id);

		if (id == 'first_page') {
			setTable(1);
			setPaging(1);
		} else if (id == 'prev_page') {
			let arrPages = [];
			$('.pages3>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const prevPage = _.min(arrPages) - showPageCnt;
			setTable(prevPage);
			setPaging(prevPage);
		} else if (id == 'next_page') {
			let arrPages = [];
			$('.pages3>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const nextPage = _.max(arrPages) + 1;
			setTable(nextPage);
			setPaging(nextPage);
		} else if (id == 'last_page') {
			const lastPage = Math.floor((totalPage - 1) / showPageCnt) * showPageCnt + 1;
			setTable(lastPage);
			setPaging(lastPage);
		}
	});

	/**
	 * 페이징 정보를 세팅합니다.
	 * @param {int} pageNum - Page Number
	 */

	// 데이터를 테이블로 출력하는 함수
	function setTable(pageNum) {
		const startIdx = (pageNum - 1) * countPerPage3;
		const endIdx = startIdx + countPerPage3;
		const filteredData = todoData3.slice(startIdx, endIdx);
		const num = $('.wlogCnt').text();

		var tbody = $('.wlogTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.
		for (var i = 0; i < filteredData.length; i++) {

			var WorklogVO = filteredData[i];
			var row = $('<tr class="wlogRow"></tr>');
			var inTmDd = formatTimestamp(WorklogVO.inTm);
			if (WorklogVO.outTm == null)
				var outTmDd = '-';
			else
				var outTmDd = formatTimestamp(WorklogVO.outTm);

			row.append('<td><span >' + (num - i - startIdx) + '</span></td>');
			row.append('<td>' + WorklogVO.clssNm + '</td>');
			row.append('<td>' + inTmDd + '</td>');
			row.append('<td>' + outTmDd + '</td>');
			if (WorklogVO.wlogCd == 'WOK0000001') {
				row.append('<td style="color:black;">' + WorklogVO.wlogNm + '</td>');
			} else if (WorklogVO.wlogCd === 'WOK0000002' || WorklogVO.wlogCd === 'WOK0000003') {
				row.append('<td style="color:#F1A77E;">' + WorklogVO.wlogNm + '</td>');
			} else {
				row.append('<td style="color:red;">' + WorklogVO.wlogNm + '</td>');
			}
			row.append('<td><button class="submitResn" >제출</button><button class="updateResn">수정</button></td>');
			row.append('<td class="resnStstus">' + WorklogVO.resnNm + '</td>');
			row.append('<td class="wlogId" style="display:none;">' + WorklogVO.wlogId + '</td>');
			row.append('<td class="resnId" style="display:none;">' + WorklogVO.resnId + '</td>');

			var wlogCd = WorklogVO.wlogCd;
			var resnId = WorklogVO.resnId;
			var resnCd = WorklogVO.resnNm;
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
					if (resnCd == 'RES0000002' || resnCd == 'RES0000003') {
						submitResnElement.addClass('hidden');
						updateResnElement.addClass('hidden');
						resnStstusElement.removeClass('hidden');
					} else {
						submitResnElement.addClass('hidden');
						updateResnElement.removeClass('hidden');
						resnStstusElement.removeClass('hidden');
					}
				}
			} else {
				submitResnElement.addClass('hidden');
				updateResnElement.addClass('hidden');
				resnStstusElement.addClass('hidden');
			}
			tbody.append(row);
		}
	}
	// 페이징 정보를 설정하는 함수
	function setPaging(pageNum) {
		const currentPage = pageNum;
		const totalPage = Math.floor(todoData3.length / countPerPage3) + (todoData3.length % countPerPage3 == 0 ? 0 : 1);

		showAllIcon();

		if (currentPage <= showPageCnt) {
			$('#first_page').hide();
			$('#prev_page').hide();
		}
		if (
			totalPage <= showPageCnt ||
			Math.floor((currentPage - 1) / showPageCnt) * showPageCnt + showPageCnt + 1 > totalPage
		) {
			$('#next_page').hide();
			$('#last_page').hide();
		}

		let start = Math.floor((currentPage - 1) / showPageCnt) * showPageCnt + 1;
		let sPagesHtml = '';
		for (const end = start + showPageCnt; start < end && start <= totalPage; start++) {
			sPagesHtml += '<span class="' + (start == currentPage ? 'active' : '') + '">' + start + '</span>';
		}
		$('.pages3').html(sPagesHtml);
	}

	// 페이지 아이콘을 모두 표시하는 함수
	function showAllIcon() {
		$('#first_page').show();
		$('#prev_page').show();
		$('#next_page').show();
		$('#last_page').show();
	}

	updateWlogTable();

	var modal2 = $('.modal2');
	var wlogTable = $('.wlogTable');
	var clickedRow = "";
	var wlogId = "";

	wlogTable.on('click', '.submitResn', function(event) {
		clickedRow = $(this).closest('tr');
		wlogId = clickedRow.find('td.wlogId').text();
		modal2.show();
		event.stopPropagation();
	});
	// 모달 내부의 .submitBtn 버튼 클릭 시
	modal2.on('click', '.submitBtn', function(event) {

		var resnText = $('.resnText1').val();
		var formData = new FormData();
		var files = document.querySelector("#fileInput2").files; // 파일 리스트를 가져옵니다.

		for (var i = 0; i < files.length; i++) { // 파일 리스트의 길이만큼 반복합니다.
			var file = files[i]; // 각 파일을 가져옵니다.
			formData.append("files[]", file); // 폼 데이터에 파일을 추가합니다.
		}
		if (resnText == '') {
			alert("사유를 입력하세요.");
			event.stopPropagation();
		} else {
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
					$('.resnText1').val('');  // .resnText1의 값을 지웁니다.
					$('#fileInput2').val(''); // #fileInput2의 값을 지웁니다.
					$('.upload-name2').val('');
					modal2.hide();
					alert("사유서를 제출하였습니다.");
					updateWlogTable();
				}
			});
		}
		event.stopPropagation();
	});

	modal2.on('click', '.closeBtn', function(event) {
		$('.resnText1').val('');  // .resnText1의 값을 지웁니다.
		$('#fileInput2').val(''); // #fileInput2의 값을 지웁니다.
		$('.upload-name2').val('');
		modal2.hide();
		event.stopPropagation();
	});

	var modal3 = $('.modal3');
	var clickedRow = "";
	var resnId = "";
	wlogTable.on('click', '.updateResn', function(event) {
		clickedRow = $(this).closest('tr');
		resnId = clickedRow.find('td.resnId').text();
		modal3.show();
		event.stopPropagation();
	});
	// 모달 내부의 .submitBtn 버튼 클릭 시
	modal3.on('click', '.submitBtn', function(event) {
		var resnText = $('.resnText2').val();
		var formData = new FormData();
		var files = document.querySelector("#fileInput3").files; // 파일 리스트를 가져옵니다.

		for (var i = 0; i < files.length; i++) { // 파일 리스트의 길이만큼 반복합니다.
			var file = files[i]; // 각 파일을 가져옵니다.
			formData.append("files[]", file); // 폼 데이터에 파일을 추가합니다.
		}
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
				$('.resnText2').val('');  // .resnText1의 값을 지웁니다.
				$('#fileInput3').val('');   // #fileInput2의 값을 지웁니다.
				$('.upload-name3').val('');
				modal3.hide();
				alert("사유서를 업데이트하였습니다.");
				updateWlogTable();
			}
		});
		event.stopPropagation();
	});

	modal3.on('click', '.closeBtn', function(event) {
		$('.resnText2').val('');  // .resnText1의 값을 지웁니다.
		$('#fileInput3').val('');   // #fileInput2의 값을 지웁니다.
		$('.upload-name3').val('');
		$('.modal3').hide();
		event.stopPropagation();
	});
});

