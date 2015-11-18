/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {
    'use strict';

    var FrzTabelaModule = angular.module('frz.tabela', ['frz.navegador']);

    // controller para a barra de navegacao
    FrzTabelaModule.controller('FrzTabelaCtrl', ['$scope', function($scope) {
        $scope.onAbrir = function() {
            $scope.navegador.mudarEstado('ESPECIAL');
        }
        
    }]);

    // diretiva da barra de navegação de dados
    FrzTabelaModule.directive('frzTabela', ['FrzNavegadorParams', function(FrzNavegadorParams) {
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-tabela/frz-tabela.html',
            scope: {
                ngModel: '=',
                dados: '=',
                onAbrir: '&',
            },
            controller: 'FrzTabelaCtrl',
            link: function(scope, element, attributes) {
                scope.navegador = new FrzNavegadorParams(scope.dados); 

                // executar o estado inicial da tabela
                if (scope.onAbrir) {
                    scope.onAbrir();
                }
            },
        };
    }]);

}());