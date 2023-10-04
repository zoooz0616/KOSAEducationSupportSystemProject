document.addEventListener('DOMContentLoaded', function() {

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
	const buttons = document.querySelectorAll('.banner-button'); // 버튼 요소들을 모두 선택

	let interval = getInterval();

	// 초기 상태에서 첫 번째 버튼을 활성화
	buttons[currentIndex].classList.add('active');

	buttonLeft.addEventListener('click', () => {
		currentIndex--;
		currentIndex = currentIndex < 0 ? 0 : currentIndex;

		// 버튼 색상 변경
		updateButtonStyle();

		imgList.style.marginLeft = `-${banner.clientWidth * currentIndex}px`;
		clearInterval(interval);
		interval = getInterval();
	});

	buttonRight.addEventListener('click', () => {
		currentIndex++;
		currentIndex = currentIndex >= imgs.length ? imgs.length - 1 : currentIndex;

		// 버튼 색상 변경
		updateButtonStyle();

		imgList.style.marginLeft = `-${banner.clientWidth * currentIndex}px`;
		clearInterval(interval);
		interval = getInterval();
	});

	// 버튼 스타일을 업데이트하는 함수
	function updateButtonStyle() {
		buttons.forEach((button, index) => {
			if (index === currentIndex) {
				button.classList.add('active'); // 현재 인덱스 버튼 활성화
			} else {
				button.classList.remove('active'); // 나머지 버튼 비활성화
			}
		});
	}

	// 주기적으로 화면 넘기기
	function getInterval() {
		return setInterval(() => {
			currentIndex++;
			currentIndex = currentIndex >= imgs.length ? 0 : currentIndex;

			// 버튼 색상 변경
			updateButtonStyle();

			imgList.style.marginLeft = `-${banner.clientWidth * currentIndex}px`;
		}, 10000);
	}

});

var intmInput = document.getElementById('intm');
var outtmInput = document.getElementById('outtm');
var wlogBtn = document.querySelector('.wlogBtn');
var isAttendance = false; // 초기 상태를 출근(false)으로 설정

if (lastVO != null) {
	var lastInTmDate = timestampToFormattedString(lastInTm);
	if (lastOutTm === null) {
		intmInput.value = lastInTmDate;
		outtmInput.value = '';
		isAttendance = true; // 초기 상태를 출근으로 설정
		wlogBtn.value = "퇴근하기";
	} else {
		var lastOutTmDate = timestampToFormattedString(lastOutTm);
		intmInput.value = lastInTmDate;
		outtmInput.value = lastOutTmDate;
		wlogBtn.value = "출근하기";
	}
} else {
	outtmInput.value = '';
	outtmInput.value = '';
	wlogBtn.value = "출근하기";
}

function toggleInOut() {
	if (isAttendance) {
		// 퇴근 처리
		$.ajax({
			url: '/student/wlog/outlog',
			method: 'POST',
			data: {
				clssId: clssId,
			},
			success: function(newOutWlogVO) {
				var outTmdate = timestampToFormattedString(newOutWlogVO.outTm);
				outtmInput.value = outTmdate;
				isAttendance = false; // 출근 상태 해제
				wlogBtn.value = "출근하기";
			}
		});
	} else {
		// 출근 처리
		$.ajax({
			url: '/student/wlog/inlog',
			type: 'POST',
			data: {
				clssId: clssId,
			},
			success: function(newInWlogVO) {
				var inTmdate = timestampToFormattedString(newInWlogVO.inTm);
				intmInput.value = inTmdate;
				outtmInput.value = '';
				isAttendance = true; // 출근 상태 설정
				wlogBtn.value = "퇴근하기";
			}
		});
	}
}
function timestampToFormattedString(timestamp) {
	var date = new Date(timestamp);

	var year = date.getFullYear();
	var month = String(date.getMonth() + 1).padStart(2, '0');
	var day = String(date.getDate()).padStart(2, '0');
	var hours = String(date.getHours()).padStart(2, '0');
	var minutes = String(date.getMinutes()).padStart(2, '0');
	var seconds = String(date.getSeconds()).padStart(2, '0');

	var formattedString = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
	return formattedString;
}

var today = new Date();

var clssEndDd = new Date(IngclssEndDd);

// Dday 계산
var timeDiff = clssEndDd.getTime() - today.getTime();
var daysDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));

// Dday를 표시할 요소에 추가
var ddayElement = document.getElementById('dDay');
if (daysDiff > 0) {
	ddayElement.textContent = 'D-' + daysDiff;
} else if (daysDiff === 0) {
	ddayElement.textContent = 'D-day';
} else {
	ddayElement.textContent = '종료';
}


const rgstList = document.querySelector('.rgstList');
const aplyList = document.querySelector('.aplyList');

rgstList.addEventListener('click', () => {
	const selectParam = 3; // 이동하려는 페이지에서 원하는 select 매개변수 값을 설정하세요.
	const newUrl = '/student/mypage?select=' + selectParam;
	window.location.href = newUrl;
});

aplyList.addEventListener('click', () => {
	const selectParam = 2; // 이동하려는 페이지에서 원하는 select 매개변수 값을 설정하세요.
	const newUrl = '/student/mypage?select=' + selectParam;
	window.location.href = newUrl;
});