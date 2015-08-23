angular.module('principal').factory('utilSrv',
		
function() {
	var utilSrv = {
		indiceDe : function(arr, item) {
			for (var i = arr.length; i--;) {
				if (angular.equals(arr[i], item)) {
					return i;
				}
			}
			return null;
		},
	};
	return utilSrv;
}

);