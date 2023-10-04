const liElements = document.querySelectorAll('.sidebar ul li');
const contentDivs = document.querySelectorAll('.maincontent > div');

// 현재 선택된 메뉴의 인덱스를 저장하는 변수
let selectedMenuIndex = -1;

// 각 li 요소를 클릭했을 때의 동작을 정의합니다.
liElements.forEach(function(liElement, index) {
	liElement.addEventListener('click', function() {
		// 현재 선택된 메뉴의 인덱스를 업데이트합니다.
		selectedMenuIndex = index;

		// 모든 li 요소의 클래스를 초기화합니다.
		liElements.forEach(function(element) {
			element.classList.remove('active');
			// 배경색과 글씨 색상을 원래 상태로 변경
			element.style.backgroundColor = '';
			element.style.color = '';
			// 이미지를 원래 상태로 변경
			const imgElement = element.querySelector('img');
			if (imgElement) {
				const originalImageSrc = element.getAttribute('data-original-image');
				imgElement.src = originalImageSrc;
			}
		});

		// 모든 content를 숨깁니다.
		contentDivs.forEach(function(content) {
			content.style.display = 'none';
		});

		// 현재 클릭한 li 요소에 active 클래스를 추가합니다.
		this.classList.add('active');

		// 데이터 속성을 사용하여 이미지를 변경합니다.
		const imgElement = this.querySelector('img');
		if (imgElement) {
			const newImageSrc = this.getAttribute('data-image');
			imgElement.src = newImageSrc;
		}

		// 클릭한 li 요소의 배경색과 글씨 색상을 변경합니다.
		this.style.background = '#0E5881';
		this.style.color = 'white';

		// 선택한 li 요소에 해당하는 content만 보여줍니다.
		contentDivs[index].style.display = 'block';

		const selectParam = index + 1; // 여기에서 선택한 메뉴에 따라 원하는 매개변수 값을 생성하세요.
		const newUrl = window.location.pathname + '?select=' + selectParam;
		history.pushState(null, null, newUrl);
	});
});

// 페이지 로딩 시 URL에 select 매개변수가 있는지 확인하고 해당 메뉴를 활성화합니다.
function activateMenuFromUrl() {
	const urlParams = new URLSearchParams(window.location.search);
	const selectParam = urlParams.get('select');
	if (selectParam) {
		const selectedIndex = parseInt(selectParam) - 1;
		if (selectedIndex >= 0 && selectedIndex < liElements.length) {
			// 현재 선택된 메뉴의 인덱스를 업데이트하고 해당 메뉴를 활성화합니다.
			selectedMenuIndex = selectedIndex;
			liElements[selectedIndex].click();
		}
	}
}
window.addEventListener('popstate', function() {
	activateMenuFromUrl();
});

window.onpopstate = function() {
	if (selectedMenuIndex >= 0) {
		liElements[selectedMenuIndex].click();
		activateMenuFromUrl();
	}
};

// 페이지 로딩 시 활성화된 메뉴를 설정합니다.
document.addEventListener('DOMContentLoaded', function() {
	activateMenuFromUrl();
});
