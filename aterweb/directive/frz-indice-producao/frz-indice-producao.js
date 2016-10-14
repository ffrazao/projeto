/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {

    'use strict';

    var FrzIndiceProducaoModule = angular.module('frz.indice.producao', ['ui.tree', 'ui.tree-filter']);

    FrzIndiceProducaoModule.config(['uiTreeFilterSettingsProvider', function(uiTreeFilterSettingsProvider) {
        'ngInject';
        // config basica dos filtros de componente tree view
        uiTreeFilterSettingsProvider.addresses = ['nome', 'descricao'];
        uiTreeFilterSettingsProvider.descendantCollection = 'bemClassificacaoList';
    }]);

    FrzIndiceProducaoModule.controller('FrzIndiceProducaoCtrl', ['$scope', 'mensagemSrv', 'UtilSrv', '$filter', 
        function($scope, mensagemSrv, UtilSrv, $filter) {
        'ngInject';
        $scope.treeFilter = $filter('uiTreeFilter');
		$scope.compareFn = function(obj1, obj2){
		    return obj1 && obj2 && obj1.id === obj2.id;
		};
        $scope.toggleChildren = function(node) {
            node.toggle();
        };
        var configuraDados = function(lista, valor) {
            if (lista && lista.length) {
                lista.forEach(function(v, k) {
                    v.selecionado = valor;
                    configuraDados(v.bemClassificacaoList);
                });
            }
        };
        $scope.limparFiltro = function() {
            $scope.filtro = '';
            configuraDados($scope.ngModel, false);
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