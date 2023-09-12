const modal = document.querySelector('.modal');
const openModalBtn = document.querySelector('.openModalBtn');
const closeModalBtn = document.querySelector('.closeModalBtn');

openModalBtn.addEventListener('click', () => {
	$.ajax({
		url: '/student/class/apply',
		method: 'GET',
		success: function(stdtId) {
			if (stdtId == "") {
				var result = confirm("로그인 후 지원이 가능합니다.\n로그인창으로 넘어가시겠습니까?");

				if (result) {
					window.location.href = '/login'; // 로그인 페이지로 이동
				}
			} else {
				$.ajax({
					url: '/student/upload/' + classId + '/check',
					method: 'GET',
					success: function(applyYN) {
						if (applyYN == '0') {
							modal.style.display = 'block'; // 모달 열기
						} else {
							alert("지원불가. \n 해당교육에 대한 지원 내역이 존재합니다. \n 교육 지원 목록으로 돌아갑니다.");
							window.location.href = '/student/class'; // 교육 지원 목록 페이지로 이동
						}
					}
				});
			}
		}
	});
});


closeModalBtn.addEventListener('click', () => {
	modal.style.display = 'none';
});

const backBtn = document.querySelector('.back');

backBtn.addEventListener('click', () => {
	location.replace('/student/class');
});

const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});


