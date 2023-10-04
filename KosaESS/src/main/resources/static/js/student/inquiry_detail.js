const backBtn = document.querySelector('.back');

backBtn.addEventListener('click', () => {
	location.replace('/student/inquiry/list');
});

const topBtn = document.querySelector(".moveTopBtn");

// 버튼 클릭 시 맨 위로 이동
topBtn.addEventListener('click', () => {
	window.scrollTo({ top: 0, behavior: "smooth" });
});


const currentPageURL = window.location.href;

// URL에서 마지막 부분 추출
const parts = currentPageURL.split('/');
const postId = parts[parts.length - 1];

// 추출한 Post ID 사용하기
console.log("Post ID:", postId);

const updateBtn = document.querySelector(".update");

// 버튼 클릭 시 맨 위로 이동
updateBtn.addEventListener('click', () => {
	window.location.href = '/student/inquiry/updateform/' + postId;
});

const deleteBtn = document.querySelector(".delete");

// 버튼 클릭 시 맨 위로 이동
deleteBtn.addEventListener('click', () => {
	$.ajax({
		url: '/student/inquiry/delete/' + postId,
		method: 'GET',
		success: function() {
			alert("삭제되었습니다.");
			window.location.href = '/student/inquiry/list';
		}
	});
});