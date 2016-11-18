/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {

    'use strict';

    var FrzUnidadeColaboradorModule = angular.module('frz.unidade.colaborador', ['ui.tree', 'ui.tree-filter']);

    FrzUnidadeColaboradorModule.config(['uiTreeFilterSettingsProvider', function(uiTreeFilterSettingsProvider) {
        'ngInject';
        // config basica dos filtros de componente tree view
        uiTreeFilterSettingsProvider.addresses = ['nome', 'descricao'];
        uiTreeFilterSettingsProvider.descendantCollection = 'bemClassificacaoList';
    }]);

    FrzUnidadeColaboradorModule.filter('unidadeOrganizacionalColaboradorFltr', function() {
        'ngInject';
        var isVisivel = function (item, filtro) {
            return !(filtro &&
                filtro.length > 0 && ((item) && (item.emprego) && (item.emprego.pessoaRelacionamentoList[0]) && (item.emprego.pessoaRelacionamentoList[0].pessoa) && (item.emprego.pessoaRelacionamentoList[0].pessoa.nome) &&
                item.emprego.pessoaRelacionamentoList[0].pessoa.nome.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1));
        };
        return function (lista, filtro) {
            var result = [], i, encontrou;
            if (lista) {
                angular.forEach(lista, function(valor) {
                    if (valor.descendenteList) {
                        encontrou = false;
                        for (i = 0; i < valor.descendenteList.length; i++) {
                            if (isVisivel(valor.descendenteList[i], filtro)) {
                                encontrou = true;
                                result.push(valor);
                                break;
                            }
                        }
                        if (!encontrou) {
                            if (isVisivel(valor, filtro)) {
                                result.push(valor);
                            }
                        }
                    }
                });
            }
            return result;
        };
    });

    FrzUnidadeColaboradorModule.controller('FrzUnidadeColaboradorCtrl', ['$scope', 'mensagemSrv', 'UtilSrv', '$filter', 
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
            $scope.ngModel.length = 0;
        };

        $scope.visible = function(item) {
            return !($scope.filtro &&
                $scope.filtro.length > 0 && ((item) && (item.emprego) && (item.emprego.pessoaRelacionamentoList[0]) && (item.emprego.pessoaRelacionamentoList[0].pessoa) && (item.emprego.pessoaRelacionamentoList[0].pessoa.nome) &&
                item.emprego.pessoaRelacionamentoList[0].pessoa.nome.trim().toLowerCase().latinize().indexOf($scope.filtro.trim().toLowerCase().latinize()) === -1));
        };
    }]);

    // diretiva da barra de navegação de dados
    FrzUnidadeColaboradorModule.directive('frzUnidadeColaborador', [function() {
        'ngInject';
        
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-unidade-colaborador/frz-unidade-colaborador.html',
            scope: {
                ngModel: '=',
                dados: '=',
                onAbrir: '&',
            },
            controller: 'FrzUnidadeColaboradorCtrl',
            link: function(scope, element, attributes, $parse) {
                // executar o estado inicial
                if (scope.ngModel && scope.ngModel.onAbrir) {
                    scope.ngModel.onAbrir();
                }
            },
        };
    }]);
}());