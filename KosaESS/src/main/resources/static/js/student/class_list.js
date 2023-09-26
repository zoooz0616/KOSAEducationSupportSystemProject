$(document).ready(function() {

	var countPerPage = 10; // 페이지당 데이터 건수
	var showPageCnt = 5;
	let todoData = [];
	// 초기 페이지당 목록 수 설정

	// 페이지당 목록 수가 변경될 때
	$('#pageList').change(function() {
		countPerPage = parseInt($(this).val());
		// 페이지당 목록 수 변경에 따라 테이블 업데이트
		updateTable();
	});

	// 체크박스의 체크 여부에 따라 전달할 값을 저장할 변수
	var ingClass = '';

	// 체크박스의 체크 여부가 변경될 때
	$('#aply').change(function() {
		// 체크박스 상태를 가져옵니다.
		ingClass = $(this).is(':checked') ? 'CLS0000002' : '';

		// 검색 버튼 클릭 이벤트를 호출
		$('.searchBtn').trigger('click');
	});

	$('.searchInput').on('keyup', function(event) {
		if (event.key === "Enter") {
			$('.searchBtn').trigger('click');
		}
	});

	// 검색 버튼 클릭 시의 처리 코드
	$('.searchBtn').click(function() {
		// 검색어를 가져옵니다.
		var searchKeyword = $('.searchInput').val().trim();

		// Ajax 요청을 보냅니다.
		$.ajax({
			type: 'GET',
			url: '/student/class/search',
			data: {
				keyword: searchKeyword,
				ingClass: ingClass // 체크박스 상태에 따라 다른 값이 전달됩니다.
			},

			success: function(data) {
				// 받은 데이터로 테이블을 업데이트합니다.
				todoData = data;

				var classTable;
				if ($('.list-view').hasClass('active')) {
					// 리스트 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
					// 테이블 헤더 업데이트

					classTable = $('.list-style tbody');
					classTable.empty(); // 테이블 내용을 비웁니다.
					setTable(1);
					setPaging(1);

				} else if ($('.grid-view').hasClass('active')) {
					// 그리드 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
					classTable = $('.grid-style tbody');
					classTable.empty(); // 테이블 내용을 비웁니다.
					setTable(1);
					setPaging(1);
				}
			}
		});
	});

	// 초기 로딩 시 테이블 업데이트
	updateTable();

	function updateTable() {
		// 검색 버튼 클릭 이벤트 호출
		$('.searchBtn').trigger('click');
	}

	// list-view 클릭 시
	$('.list-view').on('click', function() {
		// common 클래스를 가진 li 요소의 클래스를 초기화
		$('.common li').removeClass('active');
		$(this).addClass('active');
		// 그리드 테이블과 관련된 클래스를 제거하고 리스트 테이블과 관련된 클래스를 추가
		$('.grid-style').removeClass('classTable');
		$('.list-style').addClass('classTable');


		$('.classImg').hide();
		// 그리드 테이블을 숨기고 리스트 테이블을 표시
		$('.grid-style').hide(); // 그리드 테이블 숨김
		$('.list-style').show(); // 리스트 테이블 표시
		updateTable();
	});

	// grid-view 클릭 시
	$('.grid-view').on('click', function() {
		// 모든 common 클래스를 가진 li 요소의 클래스를 초기화
		$('.common li').removeClass('active');
		$(this).addClass('active');
		// 리스트 테이블과 관련된 클래스를 제거하고 그리드 테이블과 관련된 클래스를 추가
		$('.list-style').removeClass('classTable');
		$('.grid-style').addClass('classTable');

		$('.classImg').show();
		// 리스트 테이블을 숨기고 그리드 테이블을 표시
		$('.list-style').hide(); // 리스트 테이블 숨김
		$('.grid-style').show(); // 그리드 테이블 표시
		updateTable();
	});



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
		var classTable;
		if ($('.list-view').hasClass('active')) {
			// 리스트 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
			// 테이블 헤더 업데이트

			classTable = $('.list-style tbody');
			classTable.empty(); // 테이블 내용을 비웁니다.

			for (var i = 0; i < filteredData.length; i++) {
				var classVO = filteredData[i];
				var row = $('<tr class="classRow"></tr>');

				// 각 열에 해당하는 데이터를 행에 추가합니다.
				row.append('<td><span >' + (i + startIdx + 1) + '</span></td>');
				row.append('<td style="font-size: 18px;">' + classVO.clssNm + '</td>');
				if (classVO.aplyStartDd == null) {
					row.append('<td><span>미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.aplyStartDd + '<br> ~ ' + classVO.aplyEndDd + '</span></td>');
				}
				if (classVO.aplyStartDd == null) {
					row.append('<td><span>미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.clssStartDd + '<br> ~ ' + classVO.clssEndDd + '</span></td>');
				}
				if (classVO.clssAdr == null) {
					row.append('<td><span>미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.clssAdr + '</span></td>');
				}
				if (classVO.limitCnt == 0) {
					row.append('<td><span>미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.limitCnt + '</span></td>');
				}
				if (classVO.cmcdNm == '접수중') {
					row.append('<td><span class="className" style="font-weight: bold;">' + classVO.cmcdNm + '</span></td>');
				} else {
					row.append('<td><span class="className" style="color: black;">' + classVO.cmcdNm + '</span></td>');
				} var applyBtn = $('<a>').addClass('applyBtn').attr('href', '/student/class/' + classVO.clssId).text('자세히보기');
				row.append($('<td>').append(applyBtn));
				// 행을 테이블에 추가합니다.
				classTable.append(row);
			}

		} else if ($('.grid-view').hasClass('active')) {
			// 그리드 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
			classTable = $('.grid-style tbody');
			classTable.empty(); // 테이블 내용을 비웁니다.

			for (var i = 0; i < filteredData.length; i++) {
				var classVO = filteredData[i];
				var row = $('<tr class="classRow"></tr>');

				// 각 열에 해당하는 데이터를 행에 추가합니다.
				if (classVO.cmcdNm == '접수중') {
					row.append('<td style="text-align: center;"><span class="className" style="font-weight: bold;">' + classVO.cmcdNm + '</span></td>');
				} else {
					row.append('<td style="text-align: center;"><span class="className" style="color: black;">' + classVO.cmcdNm + '</span></td>');
				}
				if (classVO.fileId != null) {
					row.append('<td class="classImg"><div style="width: 100%; text-align: center;"><img src="/student/file/' + classVO.fileId + '/' + classVO.fileSubId + '"></div>');
				} else {
					row.append('<td class="classImg"><div style="width: 100%; text-align: center;"><img src="/img/logo.png"></div>');
				}
				row.append('<td style="font-weight: bold; font-size: 18px;">' + classVO.clssNm + '</td>');
				if (classVO.aplyStartDd == null) {
					row.append('<td><span>지원:  미정  </span></td>');
				} else {
					row.append('<td><span>지원: </span><span>' + classVO.aplyStartDd + '~' + classVO.aplyEndDd + '</span></td>');
				}
				/*if (classVO.clssStartDd == null) {
					row.append('<td><span>교육기간:  미정  </span></td>');
				} else {
					row.append('<td><span>교육기간: </span><span>' + classVO.clssStartDd + ' ~ ' + classVO.clssEndDd + '</span></td>');
				}
				if (classVO.clssAdr == null) {
					if (classVO.limitCnt != 0)
						row.append('<td><span>장소: 미정  /  정원: ' + classVO.limitCnt + '</span></td>');
					else
						row.append('<td><span>장소: 미정  /  정원: 미정  </span></td>');
				} else {
					if (classVO.limitCnt != 0)
						row.append('<td><span>장소: ' + classVO.clssAdr + '  /  정원: ' + classVO.limitCnt + '</span></td>');
					else
						row.append('<td><span>장소: ' + classVO.clssAdr + '  /  정원: 미정  </span></td>');
				}*/
				var applyBtn = $('<a>').addClass('applyBtn').attr('href', '/student/class/' + classVO.clssId).text('자세히보기');
				row.append($('<td>').append(applyBtn));
				// 행을 테이블에 추가합니다.
				classTable.append(row);
			}
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
const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});
