$(document).ready(function() {
	var countPerPage1 = 10; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData1 = [];

	updateAplyTable();
	function updateAplyTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/aplyList',
			success: function(data) {

				todoData1 = data;
				$('.aplyCnt').text(todoData1.length);
				setTable(1);
				setPaging(1);

				if (data.length == 0)
					$('.aplyFoot').show();
				else
					$('.aplyFoot').hide();
			}
		});
	}

	// 페이지 번호 클릭 이벤트 핸들러
	$(document).on('click', '.pages1>span', function() {
		if (!$(this).hasClass('active')) {
			$(this).parent().find('span.active').removeClass('active');
			$(this).addClass('active');

			setTable(Number($(this).text()));
		}
	});
	$(document).on('click', '.paging1>i', function() {
		const totalPage = Math.floor(todoData1.length / countPerPage1) + (todoData1.length % countPerPage1 == 0 ? 0 : 1);
		const id = $(this).attr('id');
		//console.log(id);

		if (id == 'first_page') {
			setTable(1);
			setPaging(1);
		} else if (id == 'prev_page') {
			let arrPages = [];
			$('.pages1>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const prevPage = _.min(arrPages) - showPageCnt;
			setTable(prevPage);
			setPaging(prevPage);
		} else if (id == 'next_page') {
			let arrPages = [];
			$('.pages1>span').each(function(idx, item) {
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
		const startIdx = (pageNum - 1) * countPerPage1;
		const endIdx = startIdx + countPerPage1;
		const filteredData = todoData1.slice(startIdx, endIdx);
		const num = $('.aplyCnt').text();
		// 받은 데이터로 테이블을 업데이트합니다.
		const time = Date.now();
		var tbody = $('.applyTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.

		for (var i = 0; i < filteredData.length; i++) {
			var ApplyDetailDTO = filteredData[i];
			var aplyEnd = formatTimestamp2(ApplyDetailDTO.aplyEndDt);
			var row = $('<tr class="aplyRow"></tr>');

			row.append('<td><span >' + (num - i - startIdx) + '</span></td>');
			var apply = $('<a>').attr('href', '/student/class/view/' + ApplyDetailDTO.clssId).text(ApplyDetailDTO.clssNm);
			row.append($('<td></td>').append(apply));
			row.append('<td><span>' + '~ ' + aplyEnd + '</span></td>');
			row.append('<td><span>' + ApplyDetailDTO.clssStartDd + ' ~ ' + ApplyDetailDTO.clssEndDd + '</span></td>');
			row.append('<td>' + ApplyDetailDTO.limitCnt + '</td>');
			if (ApplyDetailDTO.clssCd === 'CLS0000008') {
				row.append('<td style="color: lightslategray;">' + ApplyDetailDTO.clssCdNm + '</td>');
			} else if (ApplyDetailDTO.clssCd === ('CLS0000001')) {
				row.append('<td style="color:blue;">' + ApplyDetailDTO.clssCdNm + '</td>');
			} else if (ApplyDetailDTO.clssCd === ('CLS0000002')) {
				row.append('<td style="color:red;">' + ApplyDetailDTO.clssCdNm + '</td>');
			} else {
				row.append('<td>' + ApplyDetailDTO.clssCdNm + '</td>');
			}
			row.append('<td>' + ApplyDetailDTO.rgstDd + '</td>');
			if (ApplyDetailDTO.aplyCd === 'APL0000001' || ApplyDetailDTO.aplyCd === 'APL0000006' || ApplyDetailDTO.aplyCd === 'APL0000004') {
				row.append('<td style="color:lightslategray;">' + ApplyDetailDTO.cmcdNm + '</td>');
			} else if (ApplyDetailDTO.aplyCd === ('APL0000003')) {
				row.append('<td style="color:red;">' + ApplyDetailDTO.cmcdNm + '</td>');
			} else if (ApplyDetailDTO.aplyCd === ('APL0000005')) {
				row.append('<td style="color:blue;">' + ApplyDetailDTO.cmcdNm + '</td>');
			} else {
				row.append('<td>' + ApplyDetailDTO.cmcdNm + '</td>');
			}
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
	var modal1 = $('.modal1');
	var applyTable = $('.applyTable');
	var clickedRow = "";
	var aplyId = "";

	applyTable.on('click', '.update', function(event) {
		clickedRow = $(this).closest('tr');
		aplyId = clickedRow.find('td:hidden span').text();
		modal1.show();
		event.stopPropagation();
	});
	modal1.on('click', '.aplyBtn', function(event) {
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
				$('#fileInput1').val(''); // #fileInput2의 값을 지웁니다.
				$('.upload-name1').val('');
				modal1.hide(); // 모달을 숨기기 위해 hide() 메서드 사용
				alert("지원서를 수정하였습니다.");
				updateAplyTable();
			}
		});
		event.stopPropagation();
	});
	modal1.on('click', '.closeModalBtn', function(event) {
		$('#fileInput1').val(''); // #fileInput2의 값을 지웁니다.
		$('.upload-name1').val('');
		modal1.hide();
		event.stopPropagation();
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
					location.href = location.href;
				} else {
					alert("진행중인 수업이 존재합니다. \n 한 개이상의 교육을 수강할 수 없습니다.");
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


	// 페이징 정보를 설정하는 함수
	function setPaging(pageNum) {
		const currentPage = pageNum;
		const totalPage = Math.floor(todoData1.length / countPerPage1) + (todoData1.length % countPerPage1 == 0 ? 0 : 1);

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
		$('.pages1').html(sPagesHtml);
	}

	// 페이지 아이콘을 모두 표시하는 함수
	function showAllIcon() {
		$('#first_page').show();
		$('#prev_page').show();
		$('#next_page').show();
		$('#last_page').show();
	}
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

function formatTimestamp2(timestamp) {
	const date = new Date(timestamp);

	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');

	const formattedTimestamp = `${year}-${month}-${day} ${hours}:${minutes}`;

	return formattedTimestamp;
}




