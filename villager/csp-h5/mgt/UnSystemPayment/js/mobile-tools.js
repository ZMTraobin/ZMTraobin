$(function() {
	var nav_a = $(".nav ul li a");
	nav_a.each(function() {
		$(this).click(function() {
			$(this).addClass("a-ative").parent().siblings().find("a").removeClass("a-ative");
		})
	});
	$(".content").each(function() {
		$(this).height($(this).height() / 5)
	});
	$(".content-order-re").toggle(function() {
		if (!$(this).parent(".content").is(":animated")) {
			$(this).parent(".content").animate({
				height: "" + $(this).parent(".content").height() * 5 + "px"
			}, 'slow')
		}
	}, function() {
		if (!$(this).parent(".content").is(":animated")) {
			$(this).parent(".content").animate({
				height: "" + $(this).parent(".content").height() / 5 + "px"
			}, 'slow')
		}
	})
})