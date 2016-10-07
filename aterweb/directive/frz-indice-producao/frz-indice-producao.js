/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {

    'use strict';

    var FrzIndiceProducaoModule = angular.module('frz.indice.producao', ['ui.tree']);

    // controller para a barra de navegacao
    FrzIndiceProducaoModule.controller('FrzIndiceProducaoCtrl', ['$scope', 'mensagemSrv', 'UtilSrv', function($scope, mensagemSrv, UtilSrv) {
        'ngInject';
		$scope.compareFn = function(obj1, obj2){
		    return obj1 && obj2 && obj1.id === obj2.id;
		};
        $scope.toggleChildren = function(scope) {
            scope.toggle();
        };

    }]);

    // diretiva da barra de navegação de dados
    FrzIndiceProducaoModule.directive('frzIndiceProducao', [function() {
        'ngInject';
        
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-indice-producao/frz-indice-producao.html',
            scope: {
                ngModel: '=',
                dados: '=',
                selecao: '=?',
                funcaoRequerido: '=?',
                onAbrir: '&',
            },
            controller: 'FrzIndiceProducaoCtrl',
            link: function(scope, element, attributes, $parse) {
            	if (!scope.ngModel) {
            		scope.ngModel = [];
            	}

                // executar o estado inicial
                if (scope.ngModel.onAbrir) {
                    scope.ngModel.onAbrir();
                }

            },
        };
    }]);
}());