$(document).ready(function() {
	var countPerPage5 = 10; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData5 = [];

	updateMoneyTable();

	function updateMoneyTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/sbsdList',
			success: function(data) {
				todoData5 = data;
				$('.sbsdCnt').text(todoData5.length);
				setTable(1);
				setPaging(1);

				if (data.length == 0)
					$('.sbsdFoot').show();
				else
					$('.sbsdFoot').hide();
			}
		});
	}

	// 페이지 번호 클릭 이벤트 핸들러
	$(document).on('click', '.pages5>span', function() {
		if (!$(this).hasClass('active')) {
			$(this).parent().find('span.active').removeClass('active');
			$(this).addClass('active');

			setTable(Number($(this).text()));
		}
	});
	$(document).on('click', '.paging5>i', function() {
		const totalPage = Math.floor(todoData5.length / countPerPage5) + (todoData5.length % countPerPage5 == 0 ? 0 : 1);
		const id = $(this).attr('id');
		//console.log(id);

		if (id == 'first_page') {
			setTable(1);
			setPaging(1);
		} else if (id == 'prev_page') {
			let arrPages = [];
			$('.pages5>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const prevPage = _.min(arrPages) - showPageCnt;
			setTable(prevPage);
			setPaging(prevPage);
		} else if (id == 'next_page') {
			let arrPages = [];
			$('.pages5>span').each(function(idx, item) {
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
		const startIdx = (pageNum - 1) * countPerPage5;
		const endIdx = startIdx + countPerPage5;
		const filteredData = todoData5.slice(startIdx, endIdx);
		const num = $('.sbsdCnt').text();
		// 받은 데이터로 테이블을 업데이트합니다.
		var tbody = $('.sbsdTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.

		for (var i = 0; i < filteredData.length; i++) {
			var SubsidyVO = filteredData[i];

			var row = $('<tr class="sbsyRow"></tr>');
			row.append('<td><span>' + (num - i - startIdx) + '</span></td>');
			row.append('<td style="word-break: keep-all;">' + SubsidyVO.clssNm + '</td>');
			if (SubsidyVO.subsidyDd === '') {
				row.append('<td> - </td>');
			}
			else {
				row.append('<td>' + SubsidyVO.subsidyDd + '</td>');
			}
			const number = SubsidyVO.payment;
			if (SubsidyVO.payment === '') {
				row.append('<td> - </td>');
			}
			else {
				row.append('<td>' + number.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") + '원</td>');
			}
			if (SubsidyVO.sbsdCd === 'SSD0000001') {
				row.append('<td style="color:blue;">' + SubsidyVO.sbsdNm + '</td>');
			}
			else if (SubsidyVO.sbsdCd === 'SSD0000003') {
				row.append('<td style="color:red;">' + SubsidyVO.sbsdNm + '</td>');
			} else {
				row.append('<td style="color:black;">' + SubsidyVO.sbsdNm + '</td>');
			}

			tbody.append(row);
		}
	}

	// 페이징 정보를 설정하는 함수
	function setPaging(pageNum) {
		const currentPage = pageNum;
		const totalPage = Math.floor(todoData5.length / countPerPage5) + (todoData5.length % countPerPage5 == 0 ? 0 : 1);

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
		$('.pages5').html(sPagesHtml);
	}

	// 페이지 아이콘을 모두 표시하는 함수
	function showAllIcon() {
		$('#first_page').show();
		$('#prev_page').show();
		$('#next_page').show();
		$('#last_page').show();
	}
});

