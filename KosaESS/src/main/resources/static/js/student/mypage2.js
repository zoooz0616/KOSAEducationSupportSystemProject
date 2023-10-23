$(document).ready(function() {
	var countPerPage2 = 10; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData2 = [];

	updateRgstTable();

	function updateRgstTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/rgstList',
			success: function(data) {

				todoData2 = data;
				$('.rgstCnt').text(todoData2.length);
				setTable(1);
				setPaging(1);

				if (data.length == 0)
					$('.rgstFoot').show();
				else
					$('.rgstFoot').hide();
			}
		});
	}

	// 페이지 번호 클릭 이벤트 핸들러
	$(document).on('click', '.pages2>span', function() {
		if (!$(this).hasClass('active')) {
			$(this).parent().find('span.active').removeClass('active');
			$(this).addClass('active');

			setTable(Number($(this).text()));
		}
	});
	$(document).on('click', '.paging2>i', function() {
		const totalPage = Math.floor(todoData2.length / countPerPage2) + (todoData2.length % countPerPage2 == 0 ? 0 : 1);
		const id = $(this).attr('id');

		if (id == 'first_page') {
			setTable(1);
			setPaging(1);
		} else if (id == 'prev_page') {
			let arrPages = [];
			$('.pages2>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const prevPage = _.min(arrPages) - showPageCnt;
			setTable(prevPage);
			setPaging(prevPage);
		} else if (id == 'next_page') {
			let arrPages = [];
			$('.pages2>span').each(function(idx, item) {
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
		const startIdx = (pageNum - 1) * countPerPage2;
		const endIdx = startIdx + countPerPage2;
		const filteredData = todoData2.slice(startIdx, endIdx);
		const num = $('.rgstCnt').text();
		// 받은 데이터로 테이블을 업데이트합니다.
		var tbody = $('.rgstTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.

		for (var i = 0; i < filteredData.length; i++) {
			$('.RgstCnt').text('총 ' + filteredData.length + '개');
			var RegistrationVO = filteredData[i];
			var row = $('<tr class="rgstRow"></tr>');

			row.append('<td><span>' + (num - i - startIdx) + '</span></td>');

			row.append('<td><a href="/student/class/view/' + RegistrationVO.clssId + '">' + RegistrationVO.clssNm + '</a></td>');
			row.append('<td><span>' + RegistrationVO.clssStartDd + ' ~ ' + RegistrationVO.clssEndDd + '</span></td>');
			row.append('<td>' + RegistrationVO.rgstNm + '</td>');
			row.append('<td>' + RegistrationVO.clssTotalTm + '</td>');
			row.append('<td>' + RegistrationVO.stdtTmSum + '</td>');
			if (RegistrationVO.cmptRate >= 80.0) {
				row.append('<td style="color:blue;">' + RegistrationVO.cmptRate + '(%)</td>');
			} else {
				row.append('<td style="color:red;">' + RegistrationVO.cmptRate + '(%)</td>');
			}
			if (RegistrationVO.cmptCd === 'CMP0000001') {
				row.append('<td style="color:red;">' + RegistrationVO.cmptNm + '</td>');
			} else if (RegistrationVO.cmptCd === 'CMP0000003') {
				row.append('<td style="color:lightslategray;">' + RegistrationVO.cmptNm + '</td>');
			} else if (RegistrationVO.cmptCd === 'CMP0000002') {
				row.append('<td style="color:blue;">' + RegistrationVO.cmptNm + '</td>');
			} else {
				row.append('<td>' + RegistrationVO.cmptNm + '</td>');
			}
			if (RegistrationVO.cmptCd === 'CMP0000002') {
				/*row.append('<td class="rgstPrint" style="display: revert;"><a href="/download/file/' + RegistrationVO.fileId + '/' + RegistrationVO.fileSubId + '"><img style="height: 25px;" src="/img/file_icon.png" alt="file 아이콘"></a></td>');*/
				row.append('<td class="rgstPrint" style="display: revert;"><img style="height: 25px;cursor: pointer;" src="/img/file_icon.png" alt="file 아이콘"></td>');
			} else {
				row.append('<td class="rgstPrint" style="display: none;"></td >');
			}
			row.append('<td class="clssId" style="display:none;">' + RegistrationVO.clssId + '</td>');
			tbody.append(row);
		}
	}

	// 페이징 정보를 설정하는 함수
	function setPaging(pageNum) {
		const currentPage = pageNum;
		const totalPage = Math.floor(todoData2.length / countPerPage2) + (todoData2.length % countPerPage2 == 0 ? 0 : 1);

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
		$('.pages2').html(sPagesHtml);
	}

	// 페이지 아이콘을 모두 표시하는 함수
	function showAllIcon() {
		$('#first_page').show();
		$('#prev_page').show();
		$('#next_page').show();
		$('#last_page').show();
	}
});

var rgstRow = "";
var clssId = "";
$(document).on('click', '.rgstPrint', function() {
	rgstRow = $(this).closest('tr');
	clssId = rgstRow.find('td.clssId').text();
	$.ajax({
		type: 'POST',
		url: '/student/getAplyVO',
		data: { clssId: clssId },
		xhrFields: {
			responseType: 'blob' // Binary 데이터를 다운로드합니다.
		},
		success: function(data) {
			// PDF 다운로드 링크를 생성하고 클릭하여 다운로드합니다.
			var a = document.createElement('a');
			var url = window.URL.createObjectURL(data);
			a.href = url;
			a.download = stdtNm + '교육생_인증서.pdf'; // 파일 이름 지정
			document.body.appendChild(a);
			a.click();
			window.URL.revokeObjectURL(url); // 사용 후 URL을 해제합니다.
		}
	});
});
