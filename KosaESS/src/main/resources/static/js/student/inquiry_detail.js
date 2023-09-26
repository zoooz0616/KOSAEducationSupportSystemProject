$(document).ready(function() {
	const countPerPage = 10; // 페이지당 데이터 건수
	const showPageCnt = 5;
	let todoData = [];

	// 검색 버튼 클릭 이벤트 핸들러
	$('.searchBtn').click(function() {
		// 검색어를 가져옵니다.
		var searchKeyword = $('.searchInput').val().trim();

		// Ajax 요청을 보냅니다.
		$.ajax({
			type: 'GET',
			url: '/student/inquiry/search',
			data: {
				keyword: searchKeyword,
			},
			success: function(data) {
				todoData = data;

				// 받은 데이터로 테이블을 업데이트합니다.
				var inquiryTable;
				inquiryTable = $('.inquiry-table tbody');
				inquiryTable.empty(); // 테이블 내용을 비웁니다.
				setTable(1);
				setPaging(1);
			}
		});
	});
	updateTable();

	// 페이지 번호 클릭 이벤트 핸들러
	$(document).on('click', '.pages>span', function() {
		if (!$(this).hasClass('active')) {
			$(this).parent().find('span.active').removeClass('active');
			$(this).addClass('active');

			setTable(Number($(this).text()));
		}
	});
	$(document).on('click', '.paging>i', function() {
		const totalPage = Math.floor(todoData.length / countPerPage) + (todoData.length % countPerPage == 0 ? 0 : 1);
		const id = $(this).attr('id');
		//console.log(id);

		if (id == 'first_page') {
			setTable(1);
			setPaging(1);
		} else if (id == 'prev_page') {
			let arrPages = [];
			$('.pages>span').each(function(idx, item) {
				arrPages.push(Number($(this).text()));
			});

			const prevPage = _.min(arrPages) - showPageCnt;
			setTable(prevPage);
			setPaging(prevPage);
		} else if (id == 'next_page') {
			let arrPages = [];
			$('.pages>span').each(function(idx, item) {
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
		const startIdx = (pageNum - 1) * countPerPage;
		const endIdx = startIdx + countPerPage;
		const filteredData = todoData.slice(startIdx, endIdx);


		const inquiryTable = $('.inquiry-table tbody');
		inquiryTable.empty();

		for (var i = 0; i < filteredData.length; i++) {
			var postVO = filteredData[i];
			var row = $('<tr class="inquiryRow"></tr>');
			row.append('<td><span >' + (i + startIdx + 1) + '</span></td>');
			row.append('<td><a><span class="goInquiryDetail" style="font-size: 17px;">' + postVO.postTitle + '</span></a></td>');
			if (postVO.mngrNm == null) {
				row.append('<td><span>' + postVO.stdtNm + '</span></td>')
			} else if (postVO.stdtNm == null) {
				row.append('<td><span>' + postVO.mngrNm + '</span></td>')
			}
			row.append('<td><span>' + postVO.rgstDd + '</span></td>')
			row.append('<td><span>' + postVO.postHit + '</span></td>')
			row.append('<td><span>' + postVO.cmcdNm + '</span></td>')
			row.append('<td style="display: none;"><span>' + postVO.postId + '</span></td>')
			inquiryTable.append(row);
		}
	}

	// 페이징 정보를 설정하는 함수
	function setPaging(pageNum) {
		const currentPage = pageNum;
		const totalPage = Math.floor(todoData.length / countPerPage) + (todoData.length % countPerPage == 0 ? 0 : 1);

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
		$('.pages').html(sPagesHtml);
	}

	// 페이지 아이콘을 모두 표시하는 함수
	function showAllIcon() {
		$('#first_page').show();
		$('#prev_page').show();
		$('#next_page').show();
		$('#last_page').show();
	}

	function updateTable() {
		// 검색 버튼 클릭 이벤트 호출
		$('.searchBtn').trigger('click');
	}
	$('.inquiry-table').on('click', '.goInquiryDetail', function() {
		var clickedRow = $(this).closest('tr');
		var postId = clickedRow.find('td:hidden span').text();
		$.ajax({
			type: 'POST',
			url: '/student/incrementHit',
			data: {
				postId: postId
			},
			success: function() {
				window.location.href = '/student/inquiry/' + postId;
			}
		});
	});
});
$('.searchInput').on('keyup', function(event) {
	if (event.key === "Enter") {
		$('.searchBtn').trigger('click');
	}
});
const backBtn = document.querySelector('.back');

backBtn.addEventListener('click', () => {
	location.replace('/student/inquiry');
});

const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});
