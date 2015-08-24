(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    /* Add New States Above */

}]);

angular.module(pNmModulo).controller(pNmController,
    ['$stateParams', 'toastr',
    function($stateParams, toastr) {
        console.log($stateParams.mensagem);
}]);

})('casa', 'CasaCtrl');