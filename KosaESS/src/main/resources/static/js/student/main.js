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

var intmInput = document.getElementById('intm');
var outtmInput = document.getElementById('outtm');
var wlogBtn = document.querySelector('.wlogBtn');

if (lastVO != null) {
	var lastInTmDate = timestampToFormattedString(lastInTm);
	if (lastOutTm === null) {
		intmInput.value = lastInTmDate;
		outtmInput.value = '';
		isAttendance = true;
		wlogBtn.value = "퇴근하기";
	} else {
		var lastOutTmDate = timestampToFormattedString(lastOutTm);
		intmInput.value = lastInTmDate;
		outtmInput.value = lastOutTmDate;
		wlogBtn.value = "출근하기";
		var isAttendance = false; // 출근 상태를 저장하는 변수
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
			}

		});
		wlogBtn.value = "출근하기";//변경되는 버튼 이름
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
			}
		});
		wlogBtn.value = "퇴근하기"; //변경되는 버튼 이름
	}
	isAttendance = !isAttendance; // 상태를 토글
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