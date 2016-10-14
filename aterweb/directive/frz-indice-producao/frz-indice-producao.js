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
        $scope.limparFiltro = function() {
            $scope.filtro = '';
            $scope.ngModel.splice(0,$scope.ngModel.length);
        };
        $scope.marcarSelecionados = function(lista) {
            var sel = null;
            lista.forEach(function(v, k) {
                v.selecionado = false;
                for (sel in $scope.ngModel) {
                    if (v.id === $scope.ngModel[sel].id) {
                        v.selecionado = true;
                        break;
                    }
                }
                if (v.bemClassificacaoList && v.bemClassificacaoList.length) {
                    $scope.marcarSelecionados(v.bemClassificacaoList);
                }
            });
        };
        $scope.$watch('ngModel', function(n, o) {
            if (!$scope.ngModel || !$scope.dados) {
                return;
            }
            $scope.marcarSelecionados($scope.dados);
        }, true);
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