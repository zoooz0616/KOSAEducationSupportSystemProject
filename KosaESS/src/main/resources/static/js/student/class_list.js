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
				$('.totalRowCount').text(data.length);
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
				if (data.length == 0)
					$('.classFoot').show();
				else
					$('.classFoot').hide();
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

	var clssId = "";
	const modal = document.querySelector('.modal');
	// 데이터를 테이블로 출력하는 함수
	function setTable(pageNum) {
		const startIdx = (pageNum - 1) * countPerPage;
		const endIdx = startIdx + countPerPage;
		const filteredData = todoData.slice(startIdx, endIdx);
		const num = $('.totalRowCount').text();
		var classTable;

		if ($('.list-view').hasClass('active')) {
			// 리스트 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
			// 테이블 헤더 업데이트

			classTable = $('.list-style tbody');
			classTable.empty(); // 테이블 내용을 비웁니다.

			for (var i = 0; i < filteredData.length; i++) {
				var classVO = filteredData[i];
				var aplyStart = formatTimestamp(classVO.aplyStartDt);
				var aplyEnd = formatTimestamp(classVO.aplyEndDt);
				var row = $('<tr class="classRow"></tr>');


				// 각 열에 해당하는 데이터를 행에 추가합니다.
				row.append('<td><span >' + (num - i - startIdx) + '</span></td>');
				var applyBtn = $('<a>').attr('href', '/student/class/view/' + classVO.clssId).text(classVO.clssNm);
				row.append($('<td style=" text-overflow: ellipsis;overflow: hidden; white-space: nowrap;"></td>').append(applyBtn));
				if (classVO.aplyEndDt == null) {
					row.append('<td><span style="color: lightslategray;">미정</span></td>');
				} else {
					row.append('<td><span>' + aplyStart + ' ~ ' + aplyEnd + '</span></td>');
				}
				if (classVO.clssStartDd == null) {
					row.append('<td><span style="color: lightslategray;">미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.clssStartDd + ' ~ ' + classVO.clssEndDd + '</span></td>');
				}
				if (classVO.clssAdr == null) {
					row.append('<td><span style="color: lightslategray;">미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.clssAdr + '</span></td>');
				}
				if (classVO.limitCnt == 0) {
					row.append('<td><span style="color: lightslategray;">미정</span></td>');
				} else {
					row.append('<td><span>' + classVO.limitCnt + '</span></td>');
				}
				if (classVO.clssCd == 'CLS0000002') {
					row.append('<td><span class="className" style="font-weight: bold;">' + classVO.cmcdNm + '</span></td>');
					var applyButtonCell = $('<td></td>');
					var applyButton = $('<button class="openModalBtn">지원하기</button>');
					var applyInput = $('<input type="hidden" name="clssId" value="' + classVO.clssId + '">');
					applyButtonCell.append(applyButton);
					applyButtonCell.append(applyInput);
					row.append(applyButtonCell);
				} else if (classVO.clssCd == 'CLS0000008') {
					row.append('<td><span class="className" style="color: lightslategray;">' + classVO.cmcdNm + '</span></td>');
					row.append('<td></td>');
				} else if (classVO.clssCd == 'CLS0000001') {
					row.append('<td><span class="className" style="color: blue">' + classVO.cmcdNm + '</span></td>');
					row.append('<td></td>');
				} else {
					row.append('<td><span class="className" style="color: black;">' + classVO.cmcdNm + '</span></td>');
					row.append('<td></td>');
				}

				classTable.append(row);
			}
			var openModalBtns = $('.openModalBtn');
			// openModalBtn에 클릭 이벤트 리스너 추가
			openModalBtns.click(function() {
				// 클릭된 버튼의 data-clssId 값을 가져오기 위해 this를 사용
				clssId = $(this).closest('tr').find('input[name="clssId"]').val(); // $(this)를 사용하여 클릭된 버튼의 정보를 가져옵니다.
				console.log(clssId);
				$.ajax({
					url: '/student/class/apply',
					method: 'GET',
					success: function(stdtId) {
						if (stdtId == "") {
							var result = confirm("로그인 후 지원이 가능합니다.\n로그인 화면으로 이동하시겠습니까?");
							if (result) {
								window.location.href = '/login'; // 로그인 페이지로 이동
							}
						} else {
							$.ajax({
								url: '/student/upload/' + clssId + '/check',
								method: 'GET',
								success: function(applyYN) {
									if (applyYN == '0') {
										modal.style.display = 'block'; // 모달 열기
									} else {
										alert("지원불가. \n 해당교육에 대한 지원 내역이 존재합니다.");
									}
								}
							});
						}
					}
				});
			});
		} else if ($('.grid-view').hasClass('active')) {
			// 그리드 뷰 버튼이 활성화되어 있으면 리스트 테이블을 업데이트
			classTable = $('.grid-style tbody');
			classTable.empty(); // 테이블 내용을 비웁니다.

			for (var i = 0; i < filteredData.length; i++) {
				var classVO = filteredData[i];
				var aplyEnd = formatTimestamp(classVO.aplyEndDt);
				var row = $('<tr class="classRow"></tr>');

				// 각 tr 요소를 순회하면서 5의 배수인 경우 클래스를 추가합니다.
				if ((i + 1) % 5 === 0) {
					$(row).addClass('last');
				}

				// 각 열에 해당하는 데이터를 행에 추가합니다.
				if (classVO.clssCd == 'CLS0000002') {
					row.append('<td style="text-align: center;"><span class="className" style="font-weight: bold;">' + classVO.cmcdNm + '</span></td>');
				} else if (classVO.clssCd == 'CLS0000008') {
					row.append('<td style="text-align: center;"><span class="className" style="font-weight: bold; color: lightslategray;">' + classVO.cmcdNm + '</span></td>');
				} else if (classVO.clssCd == 'CLS0000001') {
					row.append('<td style="text-align: center;"><span class="className" style="font-weight: bold; color: blue">' + classVO.cmcdNm + '</span></td>');
				}
				else {
					row.append('<td style="text-align: center;"><span class="className" style="color: black;">' + classVO.cmcdNm + '</span></td>');
				}
				if (classVO.fileId != null) {
					row.append('<td class="classImg"><div style="width: 100%; text-align: center;"><img src="/student/file/' + classVO.fileId + '/' + classVO.fileSubId + '"></div>');
				} else {
					row.append('<td class="classImg"><div style="width: 100%; text-align: center;"><img src="/img/logo.png"></div>');
				}
				row.append('<td style="font-weight: bold; font-size: 18px; word-break: keep-all; text-overflow: ellipsis;overflow: hidden; white-space: nowrap;">' + classVO.clssNm + '</td>');
				if (classVO.aplyEndDt == null) {
					row.append('<td><span>지원마감일시:  미정  </span></td>');
				} else {
					row.append('<td><span>지원마감일시: </span><span>' + aplyEnd + '</span></td>');
				}
				var applyBtn = $('<a>').addClass('applyBtn').attr('href', '/student/class/view/' + classVO.clssId).text('자세히보기');
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

	var aplyBtn = document.querySelector(".aplyBtn");
	aplyBtn.addEventListener("click", function(event) {
		// 버튼이 disabled 상태인 경우에만 알림을 띄웁니다.
		var x = document.getElementById("fileInput");
		var selectedFiles = x.files;
		if (selectedFiles.length < 1) {
			alert("지원서 파일을 첨부해주세요.");
			event.preventDefault();
		} else {
			let formData = new FormData();
			let file = document.querySelector("#fileInput").files[0]; // 파일 인풋 필드에서 파일을 가져옴
			formData.append("file", file); // FormData에 파일 추가
			console.log(clssId);
			console.log(file);
			console.log(formData);
			$.ajax({
				type: 'POST',
				url: '/student/upload/' + clssId,
				data: formData, // FormData 사용
				processData: false,
				contentType: false,
				success: function() {
					modal.style.display = 'none'; // 모달을 숨기기 위해 hide() 메서드 사용
					alert("제출이 완료되었습니다.");
					window.location.href = '/student/class/list';
				}
			});
		}
	});
	var closeModalBtn = document.querySelector('.closeModalBtn');
	closeModalBtn.addEventListener('click', function(event) {
		modal.style.display = 'none';
		event.stopPropagation();
	});

});
const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});

function formatTimestamp(timestamp) {
	const date = new Date(timestamp);

	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');

	const formattedTimestamp = `${year}-${month}-${day} ${hours}:${minutes}`;

	return formattedTimestamp;
}

$("#fileInput").on('change', function() {
	var files = $("#fileInput")[0].files[0];
	var fileNames = files.name;
	console.log(fileNames);
	$(".upload-name").val(fileNames);
});

