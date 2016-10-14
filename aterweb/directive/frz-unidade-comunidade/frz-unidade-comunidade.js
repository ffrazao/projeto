/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {

    'use strict';

    var FrzUnidadeComunidadeModule = angular.module('frz.unidade.comunidade', ['ui.tree', 'ui.tree-filter']);

    FrzUnidadeComunidadeModule.config(['uiTreeFilterSettingsProvider', function(uiTreeFilterSettingsProvider) {
        'ngInject';
        // config basica dos filtros de componente tree view
        uiTreeFilterSettingsProvider.addresses = ['nome', 'descricao'];
        uiTreeFilterSettingsProvider.descendantCollection = 'bemClassificacaoList';
    }]);

    FrzUnidadeComunidadeModule.filter('unidadeOrganizacionalComunidadeFltr', function() {
        'ngInject';
        var isVisivel = function (item, filtro) {
            return !(filtro && filtro.length > 0 && item.nome.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1);
        };
        return function (unidadeOrganizacionalList, filtro) {
            var result = [], i, encontrou;
            if (unidadeOrganizacionalList) {
                angular.forEach(unidadeOrganizacionalList, function(unidadeOrganizacional) {
                    if (unidadeOrganizacional.comunidadeList) {
                        encontrou = false;
                        for (i = 0; i < unidadeOrganizacional.comunidadeList.length; i++) {
                            if (isVisivel(unidadeOrganizacional.comunidadeList[i], filtro)) {
                                encontrou = true;
                                result.push(unidadeOrganizacional);
                                break;
                            }
                        }
                        if (!encontrou) {
                            if (isVisivel(unidadeOrganizacional, filtro)) {
                                result.push(unidadeOrganizacional);
                            }
                        }
                    }
                });
            }
            return result;
        };
    });

    FrzUnidadeComunidadeModule.controller('FrzUnidadeComunidadeCtrl', ['$scope', 'mensagemSrv', 'UtilSrv', '$filter', 
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
                    configuraDados(v.unidadeList);
                    configuraDados(v.comunidadeList);
                });
            }
        };
        $scope.limparFiltro = function() {
            $scope.filtro = '';
            configuraDados($scope.ngModel, false);
        };

        $scope.visible = function(item) {
            return !($scope.filtro &&
                $scope.filtro.length > 0 &&
                item.nome.trim().toLowerCase().latinize().indexOf($scope.filtro.trim().toLowerCase().latinize()) === -1);
        };
    }]);

    // diretiva da barra de navegação de dados
    FrzUnidadeComunidadeModule.directive('frzUnidadeComunidade', [function() {
        'ngInject';
        
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-unidade-comunidade/frz-unidade-comunidade.html',
            scope: {
                ngModel: '=',
                onAbrir: '&',
            },
            controller: 'FrzUnidadeComunidadeCtrl',
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