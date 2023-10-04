const backBtn = document.querySelector('.back');

backBtn.addEventListener('click', () => {
	location.replace('/student/notice/list');
});

const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});