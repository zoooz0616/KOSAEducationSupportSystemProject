/*
<!-- 얼럴트 -->
	<div class="look_at_me">
		<span class="read_this">이봐!</span>
	</div>
<!-- End : 얼럴트 -->

경고창은 
alertFade("교육과정을 선택하세요.","F9DCCB","FF333E")

성공창은 
alertFade(wlogList+"건이 검색되었습니다.","CFDEE6","0E5881");

 */

function alertFade(text, bgcolor, charcolor){
	$(".look_at_me").css("display","flex");
	$(".look_at_me").css("background-color","#"+bgcolor);
	$(".look_at_me").css("border","1px solid #"+charcolor);
	$(".look_at_me").css("color","#"+charcolor);
	$(".look_at_me .read_this").text(text);
	$(".look_at_me").fadeIn(200);
	$(".look_at_me").delay(800);
	$(".look_at_me").fadeOut(200);
}
