(function($) {
	T5.extendInitializers(function() {
		function init(specs) {
			var textFld = $("#" + specs.id);
			if(specs.targetId) {
				textFld.autocomplete({
					focus:function(event, ui) {
						event.preventDefault();
						jQuery(this).val(ui.item.label);
					},
					select:function(event, ui) {
						event.preventDefault(); 
						jQuery(this).val(ui.item.label); 
						jQuery('#'+specs.targetId).val(ui.item.value);
					}
				});
			}

			var conf = {};

			if(specs.data) {
				conf.source = specs.data;
			} else {
				conf.source = function(request, response) {
					var params = {};
					var extra = $("#" + specs.id).data('extra');
					if (extra) {
						params["extra"] = extra;
					}
					params[specs.paramName] = request.term;
					var ajaxRequest = {
						url : specs.url,
						success : function(data) {
							response(eval(data));
						},
						data : "data=" + $.toJSON(params),
						dataType : "json",
						type : "POST"
					};
					$.ajax(ajaxRequest);
				};
			}
			
			if (specs.delay >= 0)
				conf.delay = specs.delay;
			if (specs.minLength >= 0)
				conf.minLength = specs.minLength;
			if (specs.options) {
				$.extend(conf, specs.options);
			}
			textFld.autocomplete(conf);
		}
		return {
			autocomplete : init
		};
	});
})(jQuery);