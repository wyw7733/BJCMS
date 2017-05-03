// JavaScript Document 		date:2015-11-19
//表单验证
$(function(){
	$('#myFormInput2').find("span[class='help-block']").fadeTo(1,0);
	//点击重置按钮移除表单样式
	$("input[type='reset']").click(function (){
		$('#myFormInput2').find("span[class='help-block']").fadeTo(1,0);
		$('#myFormInput2Error').hide();
		$('.form-group').removeClass('has-error');
		$('.form-group').removeClass('has-success');
	
	});
	/*********************************input模式表单上传验证********************************************/	   
	//单文件上传控件选择文件后执行的操作
	$(".fileupload").change(function(){
		var value = $(this).val();
		if(value){
			var width = $(this).parent("span").prevAll(".uneditable-input").css('width');
			var ziNum = parseInt(width)/16;//显示字数，防止变形
			//$(this).parent("span").prevAll(".uneditable-input").find(".fileupload-preview").text(value.substring(0,ziNum)+"...");
			$(this).parent("span").prevAll(".uneditable-input").find("i").attr("data-original-title",value);
			$(this).parent("span").prevAll(".uneditable-input").find("i").fadeTo(1,1);
			$(this).parent("span").nextAll("span").fadeTo(1,1);
			//验证
			var required = $(this).attr("required");
			var fileType = $(this).attr("fileType");
			if(required){
				//需要验证必填
				if(!value){
					//没有值
					$(this).parents(".form-group").addClass("has-error");
					$(this).parents(".input-group").nextAll(".help-block").text("请上传文件！");
					$(this).parents(".input-group").nextAll(".help-block").fadeTo(1,1);
				}else if(value&&fileType){
					//验证上传类型
					var str = value.split(".");
					var fileHz = str[str.length - 1];
					if((fileType.toLowerCase()).indexOf(fileHz.toLowerCase())<0){
						//文件类型不匹配
						$(this).parents(".form-group").addClass("has-error");
						$(this).parents(".input-group").nextAll(".help-block").text("文件类型不匹配，请上传"+fileType+"的文件类型！");
						$(this).parents(".input-group").nextAll(".help-block").fadeTo(1,1);
					}else{
						$(this).parents(".form-group").removeClass("has-error").addClass("has-success");
						$(this).parents(".input-group").nextAll(".help-block").fadeTo(1,0);
					}
				}else{
					$(this).parents(".form-group").removeClass("has-error").addClass("has-success");
					$(this).parents(".input-group").nextAll(".help-block").fadeTo(1,0);	
				}
			}
		}
	});
	//单文件上传控件删除文件后执行的操作
	$(".fileupload").parent("span").nextAll("span").click(function(){
		$(this).prevAll(".uneditable-input").find(".fileupload-preview").text("");
		$(this).prevAll(".uneditable-input").find("i").attr("data-original-title","");
		$(this).prevAll(".uneditable-input").find("i").fadeTo(1,0);
		$(this).prev("span").find("input[type='file']").val("");
		$(this).fadeTo(1,0);
		//验证
		var required = $(this).prev("span").find("input[type='file']").attr("required");
		if(required){
			//需要验证
			$(this).parents(".form-group").addClass("has-error");
			$(this).parents(".input-group").nextAll(".help-block").text("请上传文件！");
			$(this).parents(".input-group").nextAll(".help-block").fadeTo(1,1);
		}
	});
	
	/*********************************图标模式表单上传验证********************************************/
	//单文件上传控件选择文件后执行的操作
	$(".fileuploadIocs").change(function(){
		var value = $(this).val();
		if(value){
			$(this).parent("span").prevAll(".uneditable-input").find("i").attr("data-original-title",value);
			$(this).parent("span").prevAll(".uneditable-input").find("i").fadeTo(1,1);
			$(this).parent("span").nextAll("span").fadeTo(1,1);
			//验证
			var required = $(this).attr("required");
			var fileType = $(this).attr("fileType");
			if(required){
				//需要验证必填
				if(!value){
					//没有值
					$(this).parents(".form-group").addClass("has-error");
					$(this).parents(".input-group").nextAll("i").attr("data-original-title","请上传文件！");
					$(this).parents(".input-group").nextAll("i").addClass("fa-warning");
				}else if(value&&fileType){
					//验证上传类型
					var str = value.split(".");
					var fileHz = str[str.length - 1];
					if((fileType.toLowerCase()).indexOf(fileHz.toLowerCase())<0){
						//文件类型不匹配
						$(this).parents(".form-group").addClass("has-error");
						$(this).parents(".input-group").prevAll("i").attr("data-original-title","文件类型不匹配，请上传"+fileType+"的文件类型！");
						$(this).parents(".input-group").prevAll("i").addClass("fa-warning");
					}else{
						$(this).parents(".form-group").removeClass("has-error").addClass("has-success");
						$(this).parents(".input-group").prevAll("i").removeClass("fa-warning").addClass("fa-check");
						$(this).parents(".input-group").prevAll("i").attr("data-original-title","");
						
					}
				}else{
					$(this).parents(".form-group").removeClass("has-error").addClass("has-success");
					$(this).parents(".input-group").prevAll("i").removeClass("fa-warning").addClass("fa-check");
					$(this).parents(".input-group").prevAll("i").attr("data-original-title","");
				}
			}
		}
	});
	//单文件上传控件删除文件后执行的操作
	$(".fileuploadIocs").parent("span").nextAll("span").click(function(){
		$(this).prevAll(".uneditable-input").find(".fileupload-preview").text("");
		$(this).prevAll(".uneditable-input").find("i").attr("data-original-title","");
		$(this).prevAll(".uneditable-input").find("i").fadeTo(1,0);
		$(this).prev("span").find("input[type='file']").val("");
		$(this).fadeTo(1,0);
		//验证
		var required = $(this).prev("span").find("input[type='file']").attr("required");
		if(required){
			//需要验证
			$(this).parents(".form-group").addClass("has-error");
			$(this).parents(".input-group").prevAll("i").attr("data-original-title","请上传文件！");
			$(this).parents(".input-group").prevAll("i").addClass("fa-warning");
		}
	});

//*****************************span提示表单1**************************************
	$("#myFormInput1").find(".form-control").blur(function(){
		formCheckInput(this);
	});
	$("#myFormInput1").find(".form-control").keyup(function(){
		formCheckInput(this);
	});
	$("#myFormInput1").find(".form-control").change(function(){
		formCheckInput(this);
	});
	//验证单选框
//	$(".form-group").find(".radio-list").mouseout(function(){
//		ridioCheck(this);								 
//	});
	//验证复选框
	$(".form-group").find(".checkbox-list").mouseout(function(){
		checkboxCheck(this);							 
	});
	//提交表单时验证
	$("#myFormInput1").submit(function(){
		$("#myFormInput1").find(".fileupload").change();
		$("#myFormInput1").find(".form-control").blur();
		$("#myFormInput1").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormInput1").find(".form-group").find(".radio-list").mouseout();
		var isNot = $("#myFormInput1").find(".form-group.has-error").length;
		if(isNot==0){
			$("#myFormInput1Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormInput1Error").fadeTo(1,1);
			return false;
		}
	});

//*****************************span提示表单2**************************************
	$("#myFormInput2").find(".form-control").blur(function(){
		formCheckInput(this);
	});
	$("#myFormInput2").find(".form-control").keyup(function(){
		formCheckInput(this);
	});
	$("#myFormInput2").find(".form-control").change(function(){
		formCheckInput(this);
	});
	//提交表单时验证
	$("#myFormInput2").submit(function(){
		$("#myFormInput2").find(".fileupload").change();
		$("#myFormInput2").find(".form-control").blur();
		$("#myFormInput2").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormInput2").find(".form-group").find(".radio-list").mouseout();
		var isNot = $("#myFormInput2").find(".form-group.has-error").length;
		if(isNot==0){
			$("#myFormInput2Error").hide();
			return true;
		}else{
			$("#myFormInput2Error").show();
			return false;
		}
	});

//*****************************span提示表单3**************************************
	$("#myFormInput3").find(".form-control").blur(function(){
		formCheckInput(this);
	});
	$("#myFormInput3").find(".form-control").keyup(function(){
		formCheckInput(this);
	});
	$("#myFormInput3").find(".form-control").change(function(){
		formCheckInput(this);
	});
	//提交表单时验证
	$("#myFormInput3").submit(function(){
		$("#myFormInput3").find(".fileupload").change();
		$("#myFormInput3").find(".form-control").blur();
		$("#myFormInput3").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormInput3").find(".form-group").find(".radio-list").mouseout();
		var isNot = $("#myFormInput3").find(".form-group.has-error").length;
		if(isNot==0){
			$("#myFormInput3Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormInput3Error").fadeTo(1,1);
			return false;
		}
	});
	
//*****************************span提示表单4**************************************
	$("#myFormInput4").find(".form-control").blur(function(){
		formCheckInput(this);
	});
	$("#myFormInput4").find(".form-control").keyup(function(){
		formCheckInput(this);
	});
	$("#myFormInput4").find(".form-control").change(function(){
		formCheckInput(this);
	});
	//提交表单时验证
	$("#myFormInput4").submit(function(){
		$("#myFormInput4").find(".fileupload").change();
		$("#myFormInput4").find(".form-control").blur();
		$("#myFormInput4").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormInput4").find(".form-group").find(".radio-list").mouseout();
		var isNot = $("#myFormInput4").find(".form-group.has-error").length;
		if(isNot==0){
			$("#myFormInput4Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormInput4Error").fadeTo(1,1);
			return false;
		}
	});

//*****************************Icons提示表单1**************************************
	$("#myFormIcons1").find(".form-control").blur(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons1").find(".form-control").keyup(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons1").find(".form-control").change(function(){
		formCheckIcons(this);
	});
	
	//提交按钮表单验证
	$("#myFormIcons1").submit(function(){
		$("#myFormIcons1").find(".fileuploadIocns").change();
		$("#myFormIcons1").find(".form-control").blur();
		$("#myFormIcons1").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormIcons1").find(".form-group").find(".radio-list").mouseout();
		//取出错误状态的控件的个数
		var isNot = $("#myFormIcons1").find(".form-group.has-error").length;
		//如果错误状态控件的个数为0，则表示表单填写成功
		if(isNot == 0){
			$("#myFormIcons1Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormIcons1Error").fadeTo(1,1);
			return false;
		}
	});
	
//*****************************Icons提示表单2**************************************
	$("#myFormIcons2").find(".form-control").blur(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons2").find(".form-control").keyup(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons2").find(".form-control").change(function(){
		formCheckIcons(this);
	});
	
	//提交按钮表单验证
	$("#myFormIcons2").submit(function(){
		$("#myFormIcons2").find(".fileuploadIocns").change();
		$("#myFormIcons2").find(".form-control").blur();
		$("#myFormIcons2").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormIcons2").find(".form-group").find(".radio-list").mouseout();
		//取出错误状态的控件的个数
		var isNot = $("#myFormIcons2").find(".form-group.has-error").length;
		//如果错误状态控件的个数为0，则表示表单填写成功
		if(isNot == 0){
			$("#myFormIcons2Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormIcons2Error").fadeTo(1,1);
			return false;
		}
	});
	
//*****************************Icons提示表单3**************************************
	$("#myFormIcons3").find(".form-control").blur(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons3").find(".form-control").keyup(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons3").find(".form-control").change(function(){
		formCheckIcons(this);
	});
	
	//提交按钮表单验证
	$("#myFormIcons3").submit(function(){
		$("#myFormIcons3").find(".fileuploadIocns").change();
		$("#myFormIcons3").find(".form-control").blur();
		$("#myFormIcons3").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormIcons3").find(".form-group").find(".radio-list").mouseout();
		//取出错误状态的控件的个数
		var isNot = $("#myFormIcons3").find(".form-group.has-error").length;
		//如果错误状态控件的个数为0，则表示表单填写成功
		if(isNot == 0){
			$("#myFormIcons3Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormIcons3Error").fadeTo(1,1);
			return false;
		}
	});
	
//*****************************Icons提示表单4**************************************
	$("#myFormIcons4").find(".form-control").blur(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons4").find(".form-control").keyup(function(){
		formCheckIcons(this);
	});
	$("#myFormIcons4").find(".form-control").change(function(){
		formCheckIcons(this);
	});
	
	//提交按钮表单验证
	$("#myFormIcons4").submit(function(){
		$("#myFormIcons4").find(".fileuploadIocns").change();
		$("#myFormIcons4").find(".form-control").blur();
		$("#myFormIcons4").find(".form-group").find(".checkbox-list").mouseout();
		$("#myFormIcons4").find(".form-group").find(".radio-list").mouseout();
		//取出错误状态的控件的个数
		var isNot = $("#myFormIcons4").find(".form-group.has-error").length;
		//如果错误状态控件的个数为0，则表示表单填写成功
		if(isNot == 0){
			$("#myFormIcons4Error").fadeTo(1,0);
			return true;
		}else{
			$("#myFormIcons4Error").fadeTo(1,1);
			return false;
		}
	});
});


//验证表单，以边框变色和span文字提示错误信息
function formCheckInput(object){
	var value =  $(object).val();
	value = value.replace(/[ ]/g,"");
	if(value==''||!value){
		value = false;
	}
	var required = $(object).attr("required");
	var max = $(object).attr("max");
	var min = $(object).attr("min");
	var checkType = $(object).attr("checkType");
	//验证必填
	if(required){
		//验证
		if(!value||value==''){
			$(object).parents(".form-group").addClass("has-error");
			if($(object).nextAll(".help-block").length){
				$(object).nextAll(".help-block").text("输入的内容不能为空！");
				$(object).nextAll(".help-block").fadeTo(1,1);
			}else{
				$(object).parent('.input-group').nextAll(".help-block").text("输入的内容不能为空！");
				$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
			}
		}else{
			$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
			if($(object).nextAll(".help-block").length){
				$(object).nextAll(".help-block").fadeTo(1,0);
			}else{
				$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
			}
		}
	}
	//验证最大长度
	if(max){
		if(value.length>max){
			$(object).parents(".form-group").addClass("has-error");
			if($(object).nextAll(".help-block").length){
				$(object).nextAll(".help-block").text("请输入1-"+max+"个字符！");
				$(object).nextAll(".help-block").fadeTo(1,1);
			}else{
				$(object).parent('.input-group').nextAll(".help-block").text("请输入1-"+max+"个字符！");
				$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
			}
		}else if(!required){
			$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
			if($(object).nextAll(".help-block").length){
				$(object).nextAll(".help-block").fadeTo(1,0);
			}else{
				$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
			}
		}
	}
	//验证最小长度
	if(min){
		if(value.length<min&&value){
			$(object).parents(".form-group").addClass("has-error");
			if($(object).nextAll(".help-block").length){
				$(object).nextAll(".help-block").text("输入的内容长度不能小于"+min+"个字符！");
				$(object).nextAll(".help-block").fadeTo(1,1);
			}else{
				$(object).parent('.input-group').nextAll(".help-block").text("输入的内容长度不能小于"+min+"个字符！");
				$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
			}
		}else if(!required){
			$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
			if($(object).nextAll(".help-block").length){
				$(object).nextAll(".help-block").fadeTo(1,0);
			}else{
				$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
			}
		}
	}
	//验证类型
	if(checkType){
		if(checkType=='email'){
			//邮箱验证
			var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正确的Email！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正确的Email！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='plusInt'){
			//验证正整数数字非0
			var myreg = /^\+?[1-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正整数！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正整数！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='zeroInt'){
			//验证正整数数字和0
			var myreg = /^\+?[0-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入0或正整数！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入0或正整数！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType.split("_")[0]=='floatNum'){
			//验证小数小数点最多N位
			var myreg = "";
			if(checkType.split("_")[1]==1){
				myreg = /^[-]?\d*\.?\d{1,1}$/;
			}else if(checkType.split("_")[1]==2){
				myreg = /^[-]?\d*\.?\d{1,2}$/;
			}else if(checkType.split("_")[1]==3){
				myreg = /^[-]?\d*\.?\d{1,3}$/;
			}else if(checkType.split("_")[1]==4){
				myreg = /^[-]?\d*\.?\d{1,4}$/;
			}else if(checkType.split("_")[1]==5){
				myreg = /^[-]?\d*\.?\d{1,5}$/;
			}else{
				myreg = /^[-]?\d*\.?\d{1,1}$/;
			}
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType.split("_")[0]=='PfloatNum'){
			//验证正小数小数点最多N位
			var myreg = "";
			if(checkType.split("_")[1]==1){
				myreg = /^\d*\.?\d{1,1}$/;
			}else if(checkType.split("_")[1]==2){
				myreg = /^\d*\.?\d{1,2}$/;
			}else if(checkType.split("_")[1]==3){
				myreg = /^\d*\.?\d{1,3}$/;
			}else if(checkType.split("_")[1]==4){
				myreg = /^\d*\.?\d{1,4}$/;
			}else if(checkType.split("_")[1]==5){
				myreg = /^\d*\.?\d{1,5}$/;
			}else{
				myreg = /^\d*\.?\d{1,1}$/;
			}
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType.split("_")[0]=='NfloatNum'){
			//验证负小数小数点最多N位
			var myreg = "";
			if(checkType.split("_")[1]==1){
				myreg = /^-\d*\.?\d{1,1}$/;
			}else if(checkType.split("_")[1]==2){
				myreg = /^-\d*\.?\d{1,2}$/;
			}else if(checkType.split("_")[1]==3){
				myreg = /^-\d*\.?\d{1,3}$/;
			}else if(checkType.split("_")[1]==4){
				myreg = /^-\d*\.?\d{1,4}$/;
			}else if(checkType.split("_")[1]==5){
				myreg = /^-\d*\.?\d{1,5}$/;
			}else{
				myreg = /^-\d*\.?\d{1,1}$/;
			}
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入负小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入负小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='phone'){
			//验证电话
			var tel=/^([0-9]{3,4}\-)?[0-9]{7,8}$/;
			var phone=/^(\+86)?1[0-9]{10}$/;
			if(!tel.test(value)&&!phone.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正确的手机或座机号码！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正确的手机或座机号码！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='userName'){
			//验证只能输入数字，字母，下划线
			var myreg =/^[a-zA-Z]{1}[0-9a-zA-Z_]{0,}$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("只能输入字母、数字或下划线，不能以数字和下划线开头！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("只能输入字母、数字或下划线，不能以数字和下划线开头！");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='URL'){
			var myreg=/^[a-zA-z]+:\/\/*/;
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("Rss地址不能为空，请输入Rss地址");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("Rss地址不能为空，请输入Rss地址");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正确的URl地址！<http://www.***.com>");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正确的URl地址！<http://www.***.com>");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='Directory'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("文件（夹）名称不能为空，请输入文件名称");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("文件（夹）名称不能为空，请输入文件名称");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入1-"+max+"个字符！");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入1-"+max+"个字符！");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='oldpwd'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入旧密码");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入原密码");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='newpwd'){
			var val=$(object).val();
			if(val.indexOf(" ")>=0){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请不要输入空格");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请不要输入空格");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入新密码");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入新密码");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='realname'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请您输入姓名");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请您输入姓名");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='news'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("栏目名称不能为空，请输入栏目名称");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("栏目名称不能为空，请输入栏目名称");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='sort'){
			var myreg = /^\+?[1-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正整数！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正整数");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("排序号不能为空，请输入排序号");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("排序号不能为空，请输入排序号");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='loginname'){
			var myreg =/^[a-zA-Z]{1}[0-9a-zA-Z_]{0,}$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("用户登录名不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("用户登录名不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='truename'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("姓名不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("姓名不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='groupname'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("用户组名称不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("用户组名称不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='size'){
			var myreg = /^\+?[1-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正整数！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正整数");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("空间大小不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("空间大小不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}
	}
}

//验证表单，以图标和气泡形式提示错误信息
function formCheckIcons(object){
	var value =  $(object).val();
	value = value.replace(/[ ]/g,"");
	if(value==''||!value){
		value = false;	
	}
	var required = $(object).attr("required");
	var max = $(object).attr("max");
	var min = $(object).attr("min");
	var checkType = $(object).attr("checkType");
	//验证必填
	if(required){
		//验证
		if(!value){
			$(object).parents(".form-group").addClass("has-error");
			if($(object).prevAll("i").length){
				$(object).prevAll("i").attr("data-original-title","输入的内容不能为空！");
				$(object).prevAll("i").addClass("fa-warning");
			}else{
				$(object).parent('.input-group').prevAll("i").attr("data-original-title","输入的内容不能为空！");
				$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
			}
		}else{
			$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
			if($(object).prevAll("i").length){
				$(object).prevAll("i").attr("data-original-title","");
				$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");	
			}else{
				$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
				$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
			}
		}
	}
	//验证最大长度
	if(max){
		if(value.length>max){
			$(object).parents(".form-group").addClass("has-error");
			if($(object).prevAll("i").length){
				$(object).prevAll("i").attr("data-original-title","输入的内容长度不能大于"+max+"！");
				$(object).prevAll("i").addClass("fa-warning");
			}else{
				$(object).parent('.input-group').prevAll("i").attr("data-original-title","输入的内容长度不能大于"+max+"！");
				$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
			}
		}else if(!required){
			$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
			if($(object).prevAll("i").length){
				$(object).prevAll("i").attr("data-original-title","");
				$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
			}else{
				$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
				$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
			}
		}
	}
	//验证最小长度
	if(min){
		if(value.length<min&&value){
			$(object).parents(".form-group").addClass("has-error");
			
			if($(object).prevAll("i").length){
				$(object).prevAll("i").attr("data-original-title","输入的内容长度不能小于"+min+"！");
				$(object).prevAll("i").addClass("fa-warning");
			}else{
				$(object).parent('.input-group').prevAll("i").attr("data-original-title","输入的内容长度不能小于"+min+"！");
				$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
			}
		}else if(!required){
			$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
			if($(object).prevAll("i").length){
				$(object).prevAll("i").attr("data-original-title","");
				$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
			}else{
				$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
				$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
			}
		}
	}
	//验证类型
	if(checkType){
		if(checkType=='email'){
			//邮箱验证
			var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入正确的Email地址！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入正确的Email地址！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType=='plusInt'){
			//验证正整数数字
			var myreg = /^\+?[1-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入正整数！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入正整数！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType=='zeroInt'){
			//验证正整数数字包括0
			var myreg = /^\+?[0-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入0或正整数！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入0或正整数！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType.split("_")[0]=='floatNum'){
			//验证小数小数点最多N位
			var myreg = "";
			if(checkType.split("_")[1]==1){
				myreg = /^[-]?\d*\.?\d{1,1}$/;
			}else if(checkType.split("_")[1]==2){
				myreg = /^[-]?\d*\.?\d{1,2}$/;
			}else if(checkType.split("_")[1]==3){
				myreg = /^[-]?\d*\.?\d{1,3}$/;
			}else if(checkType.split("_")[1]==4){
				myreg = /^[-]?\d*\.?\d{1,4}$/;
			}else if(checkType.split("_")[1]==5){
				myreg = /^[-]?\d*\.?\d{1,5}$/;
			}else{
				myreg = /^[-]?\d*\.?\d{1,1}$/;
			}
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType.split("_")[0]=='PfloatNum'){
			//验证正小数小数点最多N位
			var myreg = "";
			if(checkType.split("_")[1]==1){
				myreg = /^\d*\.?\d{1,1}$/;
			}else if(checkType.split("_")[1]==2){
				myreg = /^\d*\.?\d{1,2}$/;
			}else if(checkType.split("_")[1]==3){
				myreg = /^\d*\.?\d{1,3}$/;
			}else if(checkType.split("_")[1]==4){
				myreg = /^\d*\.?\d{1,4}$/;
			}else if(checkType.split("_")[1]==5){
				myreg = /^\d*\.?\d{1,5}$/;
			}else{
				myreg = /^\d*\.?\d{1,1}$/;
			}
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入正小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入正小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType.split("_")[0]=='NfloatNum'){
			//验证负小数小数点最多N位
			var myreg = "";
			if(checkType.split("_")[1]==1){
				myreg = /^-\d*\.?\d{1,1}$/;
			}else if(checkType.split("_")[1]==2){
				myreg = /^-\d*\.?\d{1,2}$/;
			}else if(checkType.split("_")[1]==3){
				myreg = /^-\d*\.?\d{1,3}$/;
			}else if(checkType.split("_")[1]==4){
				myreg = /^-\d*\.?\d{1,4}$/;
			}else if(checkType.split("_")[1]==5){
				myreg = /^-\d*\.?\d{1,5}$/;
			}else{
				myreg = /^-\d*\.?\d{1,1}$/;
			}
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入负小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入负小数，小数点后最多保留"+checkType.split("_")[1]+"位！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType=='phone'){
			//验证电话
			var tel=/^([0-9]{3,4}\-)?[0-9]{7,8}$/;
			var phone=/^(\+86)?1[0-9]{10}$/;
			if(!tel.test(value)&&!phone.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","请输入正确的手机或座机号码！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","请输入正确的手机或座机号码！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}
		}else if(checkType=='userName'){
			//验证只能输入数字，字母，下划线
			var myreg =/^[a-zA-Z]{1}[0-9a-zA-Z_]{0,}$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","只能输入字母、数字或下划线，不能以数字和下划线开头！");
					$(object).prevAll("i").addClass("fa-warning");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","只能输入字母、数字或下划线，不能以数字和下划线开头！");
					$(object).parent('.input-group').prevAll("i").addClass("fa-warning");
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).prevAll("i").length){
					$(object).prevAll("i").attr("data-original-title","");
					$(object).prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}else{
					$(object).parent('.input-group').prevAll("i").attr("data-original-title","");
					$(object).parent('.input-group').prevAll("i").removeClass("fa-warning").addClass("fa-check");
				}
			}else if(checkType=='URL'){
				var myreg=/^[a-zA-z]+:\/\/*/;
				if(!value||value==''){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("Rss地址不能为空，请输入Rss地址");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("Rss地址不能为空，请输入Rss地址");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(!myreg.test(value)&&value)
				{
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入正确的URl地址！<http://www.***.com>");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入正确的URl地址！<http://www.***.com>");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else if(checkType=='Directory'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("文件（夹）名称不能为空，请输入文件名称");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("文件（夹）名称不能为空，请输入文件名称");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入1-"+max+"个字符！");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入1-"+max+"个字符！");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='oldpwd'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入旧密码");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入旧密码");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='oldpwd'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入新密码");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入新密码");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='realname'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请您输入姓名");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请您输入姓名");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='news'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("栏目名称不能为空，请输入栏目名称");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("栏目名称不能为空，请输入栏目名称");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='sort'){
			var myreg = /^\+?[1-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正整数！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正整数");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("排序号不能为空，请输入排序号");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("排序号不能为空，请输入排序号");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='loginname'){
			var myreg =/^[a-zA-Z]{1}[0-9a-zA-Z_]{0,}$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("用户登录名不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("用户登录名不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字母、数字或下划线，不能以数字和下划线开头");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='truename'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("姓名不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("姓名不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='groupname'){
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("用户组名称不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("用户组名称不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个字符");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}else if(checkType=='size'){
			var myreg = /^\+?[1-9][0-9]*$/;
			if(!myreg.test(value)&&value)
			{
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("请输入正整数！");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("请输入正整数");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(!required){
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
			if(!value||value==''){
				$(object).parents(".form-group").addClass("has-error");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").text("空间大小不能为空，请输入");
					$(object).nextAll(".help-block").fadeTo(1,1);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").text("空间大小不能为空，请输入");
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
				}
			}else if(max){
				if(value.length>max){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
				}else if(value.length<min){
					$(object).parents(".form-group").addClass("has-error");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).nextAll(".help-block").fadeTo(1,1);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").text("请输入"+min+"-"+max+"个正整数");
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,1);
					}
					
				}else if(!required){
					$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
					if($(object).nextAll(".help-block").length){
						$(object).nextAll(".help-block").fadeTo(1,0);
					}else{
						$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
					}
				}
			}else{
				$(object).parents(".form-group").removeClass("has-error").addClass("has-success");
				if($(object).nextAll(".help-block").length){
					$(object).nextAll(".help-block").fadeTo(1,0);
				}else{
					$(object).parent('.input-group').nextAll(".help-block").fadeTo(1,0);
				}
			}
		}
	}
}

//验证单选框
function ridioCheck(obj){
	if(($(obj).attr("required"))!=''){
		if($(obj).find('input:radio[type="radio"]:checked').length==0){
			$(obj).parents(".form-group").addClass("has-error");
			$(obj).nextAll(".help-block").text("请选择一项！");
			$(obj).nextAll(".help-block").fadeTo(1,1);
		}else{
			$(obj).parents(".form-group").removeClass("has-error").addClass("has-success");
			$(obj).nextAll(".help-block").fadeTo(1,0);	
		}
	}
}
//验证复选框
function checkboxCheck(obj){
	if(($(obj).attr("required"))){
		var min = $(obj).attr("min");
		var max = $(obj).attr("max");
		if($(obj).find('input:checkbox[type="checkbox"]:checked').length==0){
			$(obj).parents(".form-group").addClass("has-error");
			$(obj).nextAll(".help-block").text("请选择选项！");
			$(obj).nextAll(".help-block").fadeTo(1,1);
		}else if(min&&$(obj).find('input:checkbox[type="checkbox"]:checked').length<min){
			$(obj).parents(".form-group").addClass("has-error");
			$(obj).nextAll(".help-block").text("请至少选择"+min+"项！");
			$(obj).nextAll(".help-block").fadeTo(1,1);
		}else if(max&&$(obj).find('input:checkbox[type="checkbox"]:checked').length>max){
			$(obj).parents(".form-group").addClass("has-error");
			$(obj).nextAll(".help-block").text("请至多选择"+max+"项！");
			$(obj).nextAll(".help-block").fadeTo(1,1);
		}else{
			$(obj).parents(".form-group").removeClass("has-error").addClass("has-success");
			$(obj).nextAll(".help-block").fadeTo(1,0);	
		}
	}
}
}