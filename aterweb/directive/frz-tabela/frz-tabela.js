/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {
    'use strict';

    var FrzTabelaModule = angular.module('frz.tabela', []);

    // controller para a barra de navegacao
    FrzTabelaModule.controller('FrzTabelaCtrl', ['$scope', function($scope) {
        
        
    }]);

    // diretiva da barra de navegação de dados
    FrzTabelaModule.directive('frzTabela', ['$timeout', function($timeout) {
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-tabela/frz-tabela.html',
            scope: {
                ngModel: '=',
                tipo: '=',
                onAbrir: '&',
            },
            controller: 'FrzTabelaCtrl',
            link: function(scope, element, attributes) {
                scope.tipo = attributes.tipo;

                // executar o estado inicial da tabela
                if (scope.onAbrir) {
                    scope.onAbrir();
                }
            },
        };
    }]);

}());