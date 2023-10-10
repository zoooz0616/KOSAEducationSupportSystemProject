var colorBorder = '#33EF33';
var color_정상 = '#0E5881';
var color_결근 = 'orange';
//var color_결근 = '#3E799A';
var color_지각 = '#6E9BB3';
var color_조퇴 = '#5D8AA2';
var colorList = [color_정상, color_결근, color_지각, color_조퇴];

$.ajax({
	type: 'get',
	url: '/manager/main/wlog_cd',
	success: function(response) {
		wlogCdList = response.wlogCdList;
		console.log(wlogCdList);
		for(let i = 0; i<wlogCdList.length;i++){
			//wlogCdList[i] = wlogCdList[i][cmcdId];
			wlogCdList[i] = wlogCdList[i].cmcdNm;
		}
		console.log(wlogCdList);
		var ctx = document.getElementById('myChart').getContext('2d');
		var chart = new Chart(ctx, {
			// 만들기 원하는 차트의 유형
			//type: 'line',
			//type: 'pie',
			type: 'doughnut',
		
			// 데이터 집합을 위한 데이터
			data: {
				//labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
				labels: wlogCdList,
				datasets: [{
					label: 'My First dataset',
					backgroundColor: colorList,
					//borderColor: colorBorder,
					drowBorder: false,
					data: [18,1,2,1]
				}]
			},
			// 설정은 여기서 하세요
			options: {
				elements: {
					center: {
						text: 'Red is 2/3 the total numbers',
						color: '#FF6384', // Default is #000000
						fontStyle: 'Arial', // Default is Arial
						sidePadding: 20, // Default is 20 (as a percentage)
						minFontSize: 20, // Defdault is 20 (in px), set to false and text will not wrap.
						lineHeight: 25 // Default is 25 (in px), used for when text wraps
					}
				}
			}

		});
	}
	, error: function(error) {
		console.log(error);
	}
})