/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {
    'use strict';

    var frzFormModule = angular.module('frz.form', []);

    // controller para a barra de navegacao
    frzFormModule.controller('FrzFormCtrl', ['$scope', 'mensagemSrv', function($scope, mensagemSrv) {
        $scope.abrirDetalhe = function(form, dd) {
            if (!dd) {
                dd = {};
            }
            var dados = angular.copy(dd);
            var conteudo = {formulario: form, dados: dados};
            var conf = '<frz-form ng-model="conteudo.formulario" dados="conteudo.dados"/>';
            mensagemSrv.confirmacao(false, conf, form.nome, conteudo).then(function(conteudo) {
                // processar o retorno positivo da modal
                angular.copy(conteudo.dados, dd);
            }, function() {
                // processar o retorno negativo da modal
                //$log.info('Modal dismissed at: ' + new Date());
            });
        };
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

                // registrar os observadores
                if (scope.ngModel.observar) {
                    for (var i in scope.ngModel.observar) {
                        scope.$watch(scope.ngModel.observar[i].expressao, scope.ngModel.observar[i].funcao, scope.ngModel.observar[i].colecao);
                    }
                }

                if (scope.ngModel.tipo === 'resumo_numero') {
                    var expr = '';
                    
                    for (var i in scope.ngModel.opcao) {
                        if (expr.length > 0) {
                            expr += ' + ';
                        }
                        expr += 'dados.' + scope.ngModel.opcao[i].codigo;
                    }

                    scope.$watch(expr, function() {
                        scope.dados[scope.ngModel.codigo] = 0;
                        for (var i in scope.ngModel.opcao) {
                            if (scope.dados[scope.ngModel.opcao[i].codigo] && (scope.ngModel.codigo !== scope.ngModel.opcao[i].codigo)) {
                                scope.dados[scope.ngModel.codigo] += parseFloat(scope.dados[scope.ngModel.opcao[i].codigo]); //.toPrecision(scope.ngModel.fracao ? scope.ngModel.fracao : 2);
                            }
                        }
                        console.log(scope.dados[scope.ngModel.codigo]);
                    }, true);
                }

                // executar o estado inicial do form
                if (scope.onAbrir) {
                    scope.onAbrir();
                }
            },
        };
    }]);

}());