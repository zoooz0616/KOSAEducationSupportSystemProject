$(document).ready(function() {
	var countPerPage5 = 5; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData5 = [];

	updateMoneyTable();

	function updateMoneyTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/moneyList',
			success: function(data) {

				todoData5 = data;
				$('.moneyCnt').text(todoDat5.length);
				setTable(1);
				setPaging(1);
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
		const num = $('.totalRowCount').text();
		// 받은 데이터로 테이블을 업데이트합니다.
		var tbody = $('.moneyTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.

		/*for (var i = 0; i < filteredData.length; i++) {
			var moneyVO = filteredData[i];

			var row = $('<tr class="postRow"></tr>');
			row.append('<td><span>' + (num - i - startIdx) + '</span></td>');
			row.append('<td style="word-break: keep-all;"><a href="/student/inquiry/view/' + PostVO.postId + '"><span>' + PostVO.postTitle + '</span></a></td>');
			row.append('<td>' + PostVO.rgstDd + '</td>');
			if (PostVO.cmcdNm === '답변완료') {
				row.append('<td style="color:blue;">' + PostVO.cmcdNm + '</td>');
			}else {
				row.append('<td>' + PostVO.cmcdNm + '</td>');
			}

			var replyTd = $('<td></td>');
			if (PostVO.postCd === 'PST0000004') {
				replyTd.append('<input type="button" class="toggleReplyButton" value="확인하기" onclick = "toggleReply(this)">');
			} else {
				replyTd.append('<input type="button" class="toggleReplyButton" style="display: none;">');
			}
			row.append(replyTd);
			row.append('<td style="display: none;">' + PostVO.postId + '</td>');
			tbody.append(row);

			// 답변 컨테이너를 추가합니다.
			var replyContainer = $('<tr class="reply-container" style="display: none;"><td>' + PostVO.postContent + '</td></tr>');
			tbody.append(replyContainer);
		}*/
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
