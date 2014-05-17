// this class that attaches a confirmation box with logic  to the 'onclick' event of any HTML element.

if (jQuery) {
	(function ($){
	    T5.extendInitializers(function(){
	          function confirmation(spec){
	                $("#"+spec.id).bind("click", function(e){                      
	                      if(!confirm(spec.message))
	                           e.preventDefault();
	                });
	          }
	          return { confirmation : confirmation}
	    });
	}) (jQuery);
} else {
	var Confirm = Class.create();
	Confirm.prototype = {
		initialize: function(element, message) {
			this.message = message;
			Event.observe($(element), 'click', this.doConfirm.bindAsEventListener(this));
		},
	
		doConfirm: function(e) {
			if(! confirm(this.message)) {
				e.stop();
			}
		}
	}
}