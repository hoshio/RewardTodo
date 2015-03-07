$(function(){
	$("#sendbtn").click( function(){
		var jsondata = {
				'input': $("#input").val()
		};
		$.post("/ajax",jsondata,function(json){
			var res = json.tweet;
			console.log(json);
			var todos = "<ul>";
			for(var i in json){
			    todos += "<li>" + "id:" + json[i].id + ", todo:" + json[i].todo_name + "</li>";
			}
			todos += "</ul>";
			$("#output").html(todos);
		},
		"json"
		);
	})
})
