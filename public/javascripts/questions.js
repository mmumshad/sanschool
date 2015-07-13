var questionNumber;
var question_type;

var question = {
	
	getQuestion: function(){
		var answer;
		
		$('iframe').ready(function () {
		    console.log("iFrame ready");
		    $('iframe').contents().find('html').on('click', function(){
		    	console.log("Click");
		    	var ContentAreaHtml = $('iframe').contents().find("#ECUEcontentArea").html();
		    	var present = ContentAreaHtml.indexOf('<h3 class="ng-binding">System Health</h3>') > -1;
		    	console.log("Present = " + present);
		    	//console.log("System healh = " + $('iframe').contents().find("#ECUEcontentArea div:contains('System Health')").length);
		    });
		    
		});
		
		console.log("Sending AJAX Request QuestionNumber=" + questionNumber);
		$.ajax('/challenge-question',{
			Type: 'POST',
			contentType: 'text/json',
			dataType: 'json',
			data:{"questionNumber": questionNumber},
			success: function(response){
				console.log("Ajax request completed successfully setting QN to =" + response.questionNumber);
				var question = response.question;
				answer = response.answer;
				questionNumber = response.questionNumber;
				question_type = response.question_type;
				//console.log(question);
				$('#questions').html(question).hide();
				$('#questions').slideDown(400);
				
			},
			complete: function(){
				$('#formContinue').hide();
				$('#questionForm').on('submit',function(event){
					event.preventDefault();
					var selectedAnswer= null;
					
					if(question_type == "text"){
						selectedAnswer = $('input[name=optradio]', '#questionForm').val();
					}else{
						selectedAnswer = $('input[name=optradio]:checked', '#questionForm').val();
					}
					

					console.log("Correct Answer is " + answer);
					console.log("Selected Answer is " + selectedAnswer);
					
					if(selectedAnswer == answer){
						console.log("Correct!!");
						$('#formSubmit').hide();
						$('#errorMessage').hide();
						$('#questionForm').off('submit');
						$('#formContinue').fadeIn();
						$('#correctMessage').fadeIn();
						$('#formContinue').on('click',function(event){
							event.preventDefault();
							$('#questionForm').slideUp(400,function(){question.getQuestion()});
						});
										
					}else{
						$('#errorMessage').slideDown();
						console.log("Wrong!!");
					}
					
				});
			}
		});
	}	
};



$(document).ready(function(){
	
	question.getQuestion();
		
});