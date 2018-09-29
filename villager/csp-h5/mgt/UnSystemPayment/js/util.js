var cityPicker3 = null ;
var category_one = null ; 
var category_two = null ; 
var category_three = null ; 
var userPicker = null ;
(function($, doc) {
	mui.init();
	//时间控件
	btns = mui(".datetime")
	btns.each(function(i, btn) {
		btn.addEventListener('tap', function() {
//			var optionsJson = this.getAttribute('data-options') || '{}';
//			var options = JSON.parse(optionsJson);
			var id = this.getAttribute('id');
			/*
			 * 首次显示时实例化组件
			 * 示例为了简洁，将 options 放在了按钮的 dom 上
			 * 也可以直接通过代码声明 optinos 用于实例化 DtPicker
			 */
//			var picker = new mui.DtPicker(options);
			var picker = new mui.DtPicker({
				type: 'datetime',
				beginDate: new Date(now.getFullYear()+','+(now.getMonth()+1)+','+now.getDate())
			});
			picker.show(function(rs) {
				/*
				 * rs.value 拼合后的 value
				 * rs.text 拼合后的 text
				 * rs.y 年，可以通过 rs.y.vaue 和 rs.y.text 获取值和文本
				 * rs.m 月，用法同年
				 * rs.d 日，用法同年
				 * rs.h 时，用法同年
				 * rs.i 分（minutes 的第二个字母），用法同年
				 */
				document.getElementById(id).value = rs.value;
				/* 
				 * 返回 false 可以阻止选择框的关闭
				 * return false;
				 */
				/*
				 * 释放组件资源，释放后将将不能再操作组件
				 * 通常情况下，不需要示放组件，new DtPicker(options) 后，可以一直使用。
				 * 当前示例，因为内容较多，如不进行资原释放，在某些设备上会较慢。
				 * 所以每次用完便立即调用 dispose 进行释放，下次用时再创建新实例。
				 */
				picker.dispose();
			});
		}, false);
	});
	mui.ready(function() {
		userPicker = new mui.PopPicker();
		var showUserPickerButton = doc.getElementById('showPark');
		var userResult = doc.getElementById('showPark');
		showUserPickerButton.addEventListener('tap', function(event) {
			userPicker.show(function(items) {
				userResult.value = items[0].text;
				property_number = items[0].value;
				property_name=items[0].text;
			});
		}, false);
		//三级级联示例
		cityPicker3 = new $.PopPicker({
			layer: 3
		});
		
		var showCityPickerButton = doc.getElementById('threeLevel');
		var cityResult3 = doc.getElementById('threeLevel');
		showCityPickerButton.addEventListener('tap', function(event) {
			cityPicker3.show(function(items) {
				
				if(items[0].text == null || items[0].text == ''){
					cityResult3.value = '' + " " + '' + " " + '';
				}else{
					if(items[1].text == null || items[1].text == ''){
						cityResult3.value = items[0].text + " " + '' + " " + '';
					}else{
						if(items[2].text == null || items[2].text == ''){
							cityResult3.value = items[0].text + " " + items[1].text + " " + "";
						}else{
							cityResult3.value = items[0].text + " " + items[1].text + " " + items[2].text;
						}
					}
				}
				
				
				if(items[0].value == null || items[0].value == ''){
					category_one = '' ;
				}else{
					category_one = items[0].value ;
				}
				if(items[1].value == null || items[1].value == ''){
					category_two = '' ;
				}else{
					category_two = items[1].value ;
				}
				if(items[2].value == null || items[2].value == ''){
					category_three = '' ;
				}else{
					category_three = items[2].value ;
				}
				try{
					if(cityResult3.value.indexOf("公共区域")==-1){
						getSelectArea();
						document.getElementById("house_pick1").classList.remove("mui-hidden");
						document.getElementById("house_pick2").classList.remove("mui-hidden");
					}else if(cityResult3.value.indexOf("公共区域")!=-1){
						getSelectArea();
						document.getElementById("house_pick1").classList.add("mui-hidden");
						document.getElementById("house_pick2").classList.add("mui-hidden");

					}
				}catch(e){
					console.log(e)
				}
			});
		},false);
		
		
		showThreeLevelList();
		showCustomer_house();
		
		
	});
})(mui, document);

var showImage = function(src) {
	var docObj = document.getElementById("doc");
	var imgObjPreview = document.getElementById("preview");
	var deletePic=document.getElementById("deletePic");
	var coverPic=document.getElementById("coverPic");
	if (src != null ) {
		imgObjPreview.style.display = 'block';
		imgObjPreview.style.width = '100px';
		imgObjPreview.style.height = '100px';
		imgObjPreview.src = src ;
		deletePic.style.display="block";
		coverPic.style.display="block";
		deletePic.addEventListener("tap",function(){
			imgObjPreview.style.display="none";
			deletePic.style.display="none";
			coverPic.style.display="none";
		})
	} else {
		docObj.select();
		var imgSrc = document.selection.createRange().text;
		var localImagId = document.getElementById("localImag");

		localImagId.style.width = "100px";
		localImagId.style.height = "100px";

		try {
			localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
			localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
		} catch (e) {
			alert("您上传的图片格式不正确，请重新选择!");
			return false;
		}
		imgObjPreview.style.display = 'none';
		document.selection.empty();
	}
	return true;
}