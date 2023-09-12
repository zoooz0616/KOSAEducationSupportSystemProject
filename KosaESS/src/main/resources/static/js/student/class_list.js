$(document).ready(function() {
	// 초기 페이지당 목록 수 설정
	var itemsPerPage = 8; // 기본값 8로 설정

	// 페이지당 목록 수가 변경될 때
	$('#pageList').change(function() {
		itemsPerPage = parseInt($(this).val());
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
				var classTable;
				if ($('.list-view').hasClass('active')) {
					// 리스트 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
					// 테이블 헤더 업데이트

					classTable = $('.list-style tbody');
					classTable.empty(); // 테이블 내용을 비웁니다.

					// 데이터를 반복하면서 행을 생성하고 테이블에 추가합니다.
					for (var i = 0; i < data.length; i++) {
						if (i >= itemsPerPage) {
							break; // 페이지당 목록 수를 초과하면 추가하지 않습니다.
						}
						var classVO = data[i];
						var row = $('<tr class="classRow"></tr>');

						// 각 열에 해당하는 데이터를 행에 추가합니다.
						row.append('<td><span >' + (i + 1) + '</span></td>');
						row.append('<td style="font-size: 18px;">' + classVO.clssNm + '</td>');
						row.append('<td><span>' + classVO.aplyStartDd + '<br> ~ ' + classVO.aplyEndDd + '</span></td>');
						row.append('<td><span>' + classVO.clssStartDd + '<br> ~ ' + classVO.clssEndDd + '</span></td>');
						if (classVO.clssAdr == null) {
							row.append('<td><span>미정</span></td>');
						} else {
							row.append('<td><span>' + classVO.clssAdr + '</span></td>');
						}
						row.append('<td><span>' + classVO.limitCnt + '</span></td>');
						row.append('<td><span class="className">' + classVO.clssCdNm + '</span></td>');
						var applyBtn = $('<a>').addClass('applyBtn').attr('href', '/student/class/' + classVO.clssId).text('자세히보기');
						row.append($('<td>').append(applyBtn));
						// 행을 테이블에 추가합니다.
						classTable.append(row);
					}
					$('.totalRowCount').text(data.length);

				} else if ($('.grid-view').hasClass('active')) {
					// 그리드 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
					classTable = $('.grid-style tbody');
					classTable.empty(); // 테이블 내용을 비웁니다.

					// 데이터를 반복하면서 행을 생성하고 테이블에 추가합니다.
					for (var i = 0; i < data.length; i++) {
						if (i >= itemsPerPage) {
							break; // 페이지당 목록 수를 초과하면 추가하지 않습니다.
						}
						var classVO = data[i];
						var row = $('<tr class="classRow"></tr>');

						// 각 열에 해당하는 데이터를 행에 추가합니다.
						row.append('<td><span class="className">' + classVO.clssCdNm + '</span></td>');
						if (classVO.clssContent != null) {
							row.append('<td class="classImg"><div><img src="/img/' + classVO.clssContent + '"></div>');
						} else {
							row.append('<td class="classImg"><div><img src="/img/logo.png"></div>');
						}
						row.append('<td style="font-weight: bold; font-size: 18px;">' + classVO.clssNm + '</td>');
						row.append('<td><span>지원기간: </span><span>' + classVO.aplyStartDd + ' ~ ' + classVO.aplyEndDd + '</span></td>');
						row.append('<td><span>교육기간: </span><span>' + classVO.clssStartDd + ' ~ ' + classVO.clssEndDd + '</span></td>');
						if (classVO.clssAdr == null) {
							row.append('<td><span>장소: 미정  /  정원: ' + classVO.limitCnt + '</span></td>');
						} else {
							row.append('<td><span>장소: ' + classVO.clssAdr + '  /  정원: ' + classVO.limitCnt + '</span></td>');
						}
						var applyBtn = $('<a>').addClass('applyBtn').attr('href', '/student/class/' + classVO.clssId).text('자세히보기');
						row.append($('<td>').append(applyBtn));
						// 행을 테이블에 추가합니다.
						classTable.append(row);
					}
					$('.totalRowCount').text(data.length);
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

});



const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});
