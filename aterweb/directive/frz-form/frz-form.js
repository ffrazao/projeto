/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {
    'use strict';

    var frzFormModule = angular.module('frz.form', []);

    // controller para a barra de navegacao
    frzFormModule.controller('FrzFormCtrl', ['$scope', function($scope) {
    }]);

    // diretiva da barra de navegação de dados
    frzFormModule.directive('frzForm', ['$timeout', function($timeout) {
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-form/frz-form.html',
            scope: {
                ngModel: '=',
                dados: '=',
                onAbrir: '&',
            },
            controller: 'FrzFormCtrl',
            link: function(scope, element, attributes) {
                if (!angular.isObject(scope.dados) || angular.isArray(scope.dados)) {
                    scope.dados = {};
                }

                // executar o estado inicial do form
                if (scope.onAbrir) {
                    scope.onAbrir();
                }
            },
        };
    }]);

}());