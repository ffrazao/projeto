(function(pNmModulo, pNmFactory) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
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

})('principal', 'utilSrv');