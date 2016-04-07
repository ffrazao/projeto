(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    /* Add New States Above */

}]);

angular.module(pNmModulo).controller(pNmController,
    ['$state', 'toastr', 
    function($state, toastr) {
    	if ($state.params.mensagem) {
    		toastr.error($state.params.mensagem, 'Erro');
    	}
}]);

})('casa', 'CasaCtrl');