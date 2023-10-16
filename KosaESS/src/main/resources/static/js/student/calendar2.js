document.addEventListener('DOMContentLoaded', function() {
	var calendarEl = $('#calendar')[0];
	// full-calendar 생성하기
	var calendar = new FullCalendar.Calendar(calendarEl, {
		height: '730px',
		expandRows: true, // 화면에 맞게 높이 재설정
		slotMinTime: '08:00', // Day 캘린더에서 시작 시간
		slotMaxTime: '24:00', // Day 캘린더에서 종료 시간
		// 해더에 표시할 툴바
		headerToolbar: {
			left: 'prev,next today',
			center: 'title',
			right: 'dayGridMonth,timeGridWeek'
		},
		initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
		navLinks: true, // 날짜를 선택하면 Day 캘린더나 Week 캘린더로 링크
		editable: false, // 수정 가능?
		selectable: true, // 달력 일자 드래그 설정가능
		nowIndicator: true, // 현재 시간 마크
		dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
		locale: 'ko', // 한국어 설정
		// 이벤트 
		events: {
			url: '/student/stdtevents', // 이벤트 데이터를 가져오는 엔드포인트 설정
			method: 'GET',
			failure: function() {
				alert('이벤트 데이터를 가져올 수 없습니다.');
			},
			success: function(eventsData) {
				var rgstList = eventsData.rgst;
				var aplyList = eventsData.aply;
				console.log(eventsData);
				var transformedEvents = [];
				for (var i = 0; i < rgstList.length; i++) {
					var event = {
						backgroundColor: '#F1A77E',
						borderColor: '#F1A77E',
						title: rgstList[i].clssNm,
						start: rgstList[i].clssStartDd,
						end: rgstList[i].clssEndDd,
						url: 'student/class/view/' + rgstList[i].clssId
						// 필요한 경우 더 많은 이벤트 속성을 설정할 수 있습니다.
					};
					transformedEvents.push(event);
				}
				for (var i = 0; i < aplyList.length; i++) {
					var event = {
						title: aplyList[i].clssNm,
						start: aplyList[i].aplyStartDt,
						end: aplyList[i].aplyEndDt,
						url: 'student/class/view/' + aplyList[i].clssId
						// 필요한 경우 더 많은 이벤트 속성을 설정할 수 있습니다.
					};
					transformedEvents.push(event);
				}
				return transformedEvents;
			}
		}
	});
	// 캘린더 랜더링
	calendar.render();
});