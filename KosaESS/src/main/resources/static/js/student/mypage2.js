$(document).ready(function() {
	var countPerPage2 = 5; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData2 = [];

	updateRgstTable();

	function updateRgstTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/rgstList',
			success: function(data) {

				todoData2 = data;
				$('.rgstCnt').text('총 ' + todoData2.length + '건');
				setTable(1);
				setPaging(1);
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
		//console.log(id);

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
		// 받은 데이터로 테이블을 업데이트합니다.
		var tbody = $('.rgstTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.

		for (var i = 0; i < filteredData.length; i++) {
			$('.RgstCnt').text('총 ' + filteredData.length + '개');
			var RegistrationVO = filteredData[i];
			var row = $('<tr class="rgstRow"></tr>');

			row.append('<td><span >' + (i + 1) + '</span></td>');

			row.append('<td style="word-break: keep-all;"><a href="/student/class/view/' + RegistrationVO.clssId + '"><span>' + RegistrationVO.clssNm + '</span></a></td>');
			row.append('<td><span>' + RegistrationVO.clssStartDd + '<br> ~ ' + RegistrationVO.clssEndDd + '</span></td>');
			row.append('<td>' + RegistrationVO.rgstNm + '</td>');
			row.append('<td>' + RegistrationVO.cmptNm + '</td>');
			if (RegistrationVO.cmptCd === 'CMP0000002') {
				row.append('<td class="rgstPrint" style="display: revert;"><a href="/download/file/' + RegistrationVO.fileId + '/' + RegistrationVO.fileSubId + '"><img style="height: 25px;" src="/img/file_icon.png" alt="file 아이콘"></a></td>');
			} else {
				row.append('<td class="rgstPrint" style="display: none;"></td >');

			}

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

