
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
let chat_index = 0
let arrOfChanel = {"行会":"label-success","当前":"label-inverse","私聊":"label-important","喇叭":"label-primary","世界":"label-important","组队":"label-info"}
function add_message(name,img,msg,clear,obj) {
	var  inner = $('#chat-messages-inner');
	var id = 'msg-'+chat_index;
	var idname = name.replace(' ','-').toLowerCase();
	inner.append('<p id="'+id+'" class="user-'+idname+'">'
		+'<span class="msg-block"><img src="'+img+'" alt="" /><strong>'+name+'</strong>&nbsp;&nbsp;<span class="label ' + arrOfChanel[obj.channelName] + '">' + obj.channelName + '</span>  <span class="time">- '+ obj.dateTime + '</span>'
		+'<span class="msg">'+msg+'</span></span></p>');
	$('#'+id).hide().fadeIn(800);
	if(clear) {
		$('.chat-message input').val('').focus();
	}
	$('#chat-messages').animate({ scrollTop: inner.height() },1000);
}
