/*
div사이즈 동적으로 구하기
*/
const banner = document.querySelector('.banner');
const imgList = document.querySelector('.img-list');
const imgs = document.querySelectorAll('.imgs');
let currentIndex = 0; // 현재 슬라이드 화면 인덱스

imgs.forEach((inner) => {
	inner.style.width = `${banner.clientWidth}px`; // inner의 width를 모두 banner의 width로 만들기
})

imgList.style.width = `${banner.clientWidth * imgs.length}px`; // imgList의 width를 inner의 width * inner의 개수로 만들기

/*
버튼에 이벤트 등록하기
*/
const buttonLeft = document.querySelector('.button-left');
const buttonRight = document.querySelector('.button-right');

buttonLeft.addEventListener('click', () => {
	currentIndex--;
	currentIndex = currentIndex < 0 ? 0 : currentIndex; // index값이 0보다 작아질 경우 0으로 변경
	imgList.style.marginLeft = `-${banner.clientWidth * currentIndex}px`; // index만큼 margin을 주어 옆으로 밀기
	clearInterval(interval); // 기존 동작되던 interval 제거
	interval = getInterval(); // 새로운 interval 등록
});

buttonRight.addEventListener('click', () => {
	currentIndex++;
	currentIndex = currentIndex >= imgs.length ? imgs.length - 1 : currentIndex; // index값이 inner의 총 개수보다 많아질 경우 마지막 인덱스값으로 변경
	imgList.style.marginLeft = `-${banner.clientWidth * currentIndex}px`; // index만큼 margin을 주어 옆으로 밀기
	clearInterval(interval); // 기존 동작되던 interval 제거
	interval = getInterval(); // 새로운 interval 등록
});

/*
주기적으로 화면 넘기기
*/
const getInterval = () => {
	return setInterval(() => {
		currentIndex++;
		currentIndex = currentIndex >= imgs.length ? 0 : currentIndex;
		imgList.style.marginLeft = `-${banner.clientWidth * currentIndex}px`;
	}, 10000);
}

let interval = getInterval(); // interval 등록

var isAttendance = false; // 출근 상태를 저장하는 변수

function toggleInOut() {
	var intmInput = document.getElementById('intm');
	var outtmInput = document.getElementById('outtm');
	var wlogBtn = document.querySelector('.wlogBtn');
	
	

	if (isAttendance) {
		// 퇴근 처리

		var outlog = getCurrentDt();

		$.ajax({
			url: '/student/wlog/outlog',
			method: 'POST',
			data: {
				clssId: clssId,
				outlog: outlog
			},
			success: function(data) {
			}

		});
		outtmInput.value = getCurrentDt();

		wlogBtn.value = "출근하기";//변경되는 버튼 이름
	} else {
		// 출근 처리
		var inlog = getCurrentDt();

		$.ajax({
			url: '/student/wlog/inlog',
			type: 'POST',
			data: {
				clssId: clssId,
				inlog: inlog
			},

			success: function(newInWlogVO) {
				intmInput.value = newInWlogVO.InTm;
			}
		});
		wlogBtn.value = "퇴근하기"; //변경되는 버튼 이름
	}
	isAttendance = !isAttendance; // 상태를 토글
}

function getCurrentDt() {
	var now = new Date();
	var year = now.getFullYear();
	var month = (now.getMonth() + 1 < 10 ? '0' : '') + (now.getMonth() + 1);
	var day = (now.getDate() < 10 ? '0' : '') + now.getDate();
	var hours = (now.getHours() < 10 ? '0' : '') + now.getHours();
	var minutes = (now.getMinutes() < 10 ? '0' : '') + now.getMinutes();
	var seconds = (now.getSeconds() < 10 ? '0' : '') + now.getSeconds();
	var formattedTime = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;

	return formattedTime;
}