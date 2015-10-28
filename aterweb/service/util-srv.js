(function(pNmModulo, pNmFactory) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory, ['$rootScope', '$http',
    function($rootScope, $http) {
        var UtilSrv = {
            indiceDe : function(arr, item) {
                for (var i = arr.length; i--;) {
                    if (angular.equals(arr[i], item)) {
                        return i;
                    }
                }
                return null;
            },
            dominio: function (dominio) {
                return $http.get($rootScope.servicoUrl + '/dominio', {params: dominio});
            }
        };
        return UtilSrv;
    }
]);

})('principal', 'UtilSrv');