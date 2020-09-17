
$(document).ready(function(){
	
	var msg_template = '<p><span class="msg-block"><strong></strong><span class="time"></span><span class="msg"></span></span></p>';
	
	$('.chat-message button').click(function(){
		var input = $(this).siblings('span').children('input[type=text]');		
		if(input.val() != ''){
			add_message('You','../static/img/demo/av1.jpg',input.val(),true,"无");
		}		
	});
	
	$('.chat-message input').keypress(function(e){
		if(e.which == 13) {	
			if($(this).val() != ''){
				add_message('You','../static/img/demo/av1.jpg',$(this).val(),"无");
			}		
		}
	});

});
function dateFormat(fmt, date) {
	console.log(date)
	let ret;
	const opt = {
		"Y+": date.getFullYear().toString(),        // 年
		"m+": (date.getMonth() + 1).toString(),     // 月
		"d+": date.getDate().toString(),            // 日
		"H+": date.getHours().toString(),           // 时
		"M+": date.getMinutes().toString(),         // 分
		"S+": date.getSeconds().toString()          // 秒
		// 有其他格式化字符需求可以继续添加，必须转化成字符串
	};
	for (let k in opt) {
		ret = new RegExp("(" + k + ")").exec(fmt);
		if (ret) {
			fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
		};
	};
	return fmt;
}
function dateFormat2(format, time) {
	var t = new Date(time);
	var tf = function(i) {
		return (i < 10 ? '0' : '') + i
	};
	return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
		switch (a) {
			case 'yyyy':
				return tf(t.getFullYear());
				break;
			case 'MM':
				return tf(t.getMonth() + 1);
				break;
			case 'mm':
				return tf(t.getMinutes());
				break;
			case 'dd':
				return tf(t.getDate());
				break;
			case 'HH':
				return tf(t.getHours());
				break;
			case 'ss':
				return tf(t.getSeconds());
				break;
		}
	});
}
let chat_index = 0
let arrOfChanel = {"行会":"label-success","当前":"label-inverse","私聊":"label-important","喇叭":"label-primary","世界":"label-important","组队":"label-info"}
// dateFormat("YYYY-mm-dd HH:MM:SS", new Date(obj.dateTime))
function add_message(name,img,msg,clear,obj) {
	var  inner = $('#chat-messages-inner');
	var id = 'msg-'+chat_index;
	var idname = name.replace(' ','-').toLowerCase();
	inner.append('<p id="'+id+'" class="user-'+idname+'">'
		+'<span class="msg-block"><img src="'+img+'" alt="" /><strong>'+name+'</strong>&nbsp;&nbsp;<span class="label ' + arrOfChanel[obj.channelName] + '">' + obj.channelName + '</span>  <span class="time"> '+ obj.dateTime + '</span>'
		+'<span class="msg">'+msg+'</span></span></p>');
	$('#'+id).hide().fadeIn(800);
	if(clear) {
		$('.chat-message input').val('').focus();
	}
	$('#chat-messages').animate({ scrollTop: inner.height() },1000);
}
