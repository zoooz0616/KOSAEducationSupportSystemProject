const liElements = document.querySelectorAll('.sidebar ul li');
const contentDivs = document.querySelectorAll('.maincontent > div');

// 각 li 요소를 클릭했을 때의 동작을 정의합니다.
liElements.forEach(function(liElement, index) {
	liElement.addEventListener('click', function() {
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
	});
});
$(document).ready(function() {
	liElements[0].click();
});