$(document).ready(function() {
	var countPerPage4 = 10; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData4 = [];

	updatePostTable();

	function updatePostTable() {
		$.ajax({
			type: 'POST',
			url: '/student/mypage/postList',
			success: function(data) {

				todoData4 = data;
				$('.postCnt').text(todoData4.length);
				setTable(1);
				setPaging(1);
				
				if (data.length == 0)
					$('.postFoot').show();
				else
					$('.postFoot').hide();
			}
		});
	}

	// 페이지 번호 클릭 이벤트 핸들러
	$(document).on('click', '.pages4>span', function() {
		if (!$(this).hasClass('active')) {
			$(this).parent().find('span.active').removeClass('active');
			$(this).addClass('active');

			setTable(Number($(this).text()));
		}
	});
	$(document).on('click', '.paging4>i', function() {
		const totalPage = Math.floor(todoData4.length / countPerPage4) + (todoData4.length % countPerPage4 == 0 ? 0 : 1);
		const id = $(this).attr('id');

		if (id == 'first_page') {
			setTable(1);
			setPaging(1);
		} else if (id == 'prev_page') {
			let arrPages = [];
			$('.pages4>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const prevPage = _.min(arrPages) - showPageCnt;
			setTable(prevPage);
			setPaging(prevPage);
		} else if (id == 'next_page') {
			let arrPages = [];
			$('.pages4>span').each(function(idx, item) {
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
		const startIdx = (pageNum - 1) * countPerPage4;
		const endIdx = startIdx + countPerPage4;
		const filteredData = todoData4.slice(startIdx, endIdx);
		const num = $('.postCnt').text();
		// 받은 데이터로 테이블을 업데이트합니다.
		var tbody = $('.postTable tbody');
		tbody.empty(); // tbody 내용을 비웁니다.

		for (var i = 0; i < filteredData.length; i++) {
			var PostVO = filteredData[i];

			var row = $('<tr class="postRow"></tr>');
			row.append('<td><span>' + (num - i - startIdx) + '</span></td>');
			row.append('<td style="word-break: keep-all;"><a href="/student/inquiry/view/' + PostVO.postId + '"><span>' + PostVO.postTitle + '</span></a></td>');
			row.append('<td>' + PostVO.rgstDd + '</td>');
			if (PostVO.postCd === 'PST0000004') {
				row.append('<td style="color:blue;">' + PostVO.cmcdNm + '</td>');
			} else {
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
		}
	}

	// 페이징 정보를 설정하는 함수
	function setPaging(pageNum) {
		const currentPage = pageNum;
		const totalPage = Math.floor(todoData4.length / countPerPage4) + (todoData4.length % countPerPage4 == 0 ? 0 : 1);

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
		$('.pages4').html(sPagesHtml);
	}

	// 페이지 아이콘을 모두 표시하는 함수
	function showAllIcon() {
		$('#first_page').show();
		$('#prev_page').show();
		$('#next_page').show();
		$('#last_page').show();
	}
});
function toggleReply(button) {
	var replyContainer = $(button).closest('tr').next('.reply-container');
	var postId = $(button).closest('tr').find('td:last-child').text().trim();
	var otherButtons = $('.toggleReplyButton').not(button);

	if (replyContainer.css('display') === 'none') {
		// 다른 답변 컨테이너를 닫습니다.
		$('.reply-container').slideUp(0);
		otherButtons.val("확인하기");

		// 현재 클릭한 답변 컨테이너를 엽니다.
		$.ajax({
			type: "POST",
			url: "/student/show/reply",
			data: {
				postId: postId
			},
			success: function(data) {
				var ul = $('<td colspan="5" style="padding:0;"></td>');
				for (var i = 0; i < data.length; i++) {
					var reply = data[i];
					var li = $('<li class="reply">');
					if (i == 0) {
						li.append('<div class="replyTop1">내용</div>')
						li.append('<div><pre>' + reply.inquiry.Postcontent + '</pre></div>');
						li.append('<div class="replyTop2">답변</div>')
						li.append('<div><img style="height: 50px;" src="/img/manager_navy.png">' + reply.inquiry.name + '</div>');
						li.append('<div style="padding-left:5%;">' + reply.inquiry.title + '</div>');
						li.append('<div style="word-break: keep-all; padding-left:5%;">' + reply.inquiry.replyContent + '</div>');
						li.append('<div style="color: lightslategray; padding-left:5%;">' + reply.inquiry.date + '</div>');
					} else {
						li.append('<div style="display:none;"></div>');
						li.append('<div><img style="height: 50px;" src="/img/manager_navy.png">' + reply.inquiry.name + '</div>');
						li.append('<div style="padding-left:5%;">' + reply.inquiry.title + '</div>');
						li.append('<div style="word-break: keep-all; padding-left:5%;">' + reply.inquiry.replyContent + '</div>');
						li.append('<div style="color: lightslategray; padding-left:5%;">' + reply.inquiry.date + '</div>');
					}
					ul.append(li);
				}

				replyContainer.html(ul).hide().slideDown(0);
				$(button).val("닫기");
			}
		});
	} else {
		// 답변 컨테이너를 닫고 버튼 텍스트를 "확인하기"로 변경합니다.
		replyContainer.slideUp(0);
		$(button).val("확인하기");
	}
}
