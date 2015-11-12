/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {
    'use strict';

    var frzArquivoModule = angular.module('frz.arquivo', ['ngFileUpload', 'ngImgCrop']);

    // controller para a barra de navegacao
    frzArquivoModule.controller('FrzArquivoCtrl', ['$scope', function($scope) {
        $scope.$watch('files', function () {
            if (!$scope.files) {
                return;
            }
            $scope.upload($scope.files);
        });
        $scope.$watch('file', function () {
            if ($scope.file != null) {
                $scope.files = [$scope.file]; 
            }
        });
        $scope.log = '';
    }]);

    // diretiva da barra de navegação de dados
    frzArquivoModule.directive('frzArquivo', ['$timeout', 'UtilSrv', function($timeout, UtilSrv) {
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-arquivo/frz-arquivo.html',
            scope: {
                ngModel: '=',
                tipo: '=',
                onAbrir: '&',
            },
            controller: 'FrzArquivoCtrl',
            link: function(scope, element, attributes) {
                scope.tipo = attributes.tipo;
                if (scope.tipo === 'PERFIL') {
                    scope.upload = function (dataUrl) {
                        UtilSrv.uploadImagem(dataUrl).then(
                            function (response) {
                                $timeout(function () {scope.ngModel = response.data.mensagem;});
                            }, 
                            function (response) {
                                if (response.status > 0) {
                                    scope.errorMsg = response.status + ': ' + response.data;
                                }
                            }, 
                            function (evt) {
                                scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                            }
                        );
                    };
                } else {
                    scope.upload = function (files) {
                        var arquivos = UtilSrv.uploadArquivos(files);
                        for (var i in arquivos) {
                            arquivos[i].progress(function (evt) {
                                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                                scope.log = 'progress: ' + progressPercentage + '% ' + evt.config.data.file.name + '\n' + scope.log;
                            }).
                            success(function (data, status, headers, config) {
                                $timeout(function() {
                                    scope.log = 'file: ' + config.data.file.name + ', Response: ' + JSON.stringify(data) + '\n' + scope.log;
                                });
                            });
                        }
                    };
                }

                // executar o estado inicial do arquivo
                if (scope.onAbrir) {
                    scope.onAbrir();
                }
            },
        };
    }]);

}());