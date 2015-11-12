/*jslint browser: true, plusplus: true, loopfunc: true*/

(function() {
    'use strict';

    var frzArquivoModule = angular.module('frz.arquivo', ['ngFileUpload', 'ngImgCrop']);

    // controller para a barra de navegacao
    frzArquivoModule.controller('FrzArquivoCtrl', ['$scope', 'Upload', '$timeout', function($scope, Upload, $timeout) {
        $scope.$watch('files', function () {
            $scope.upload($scope.files);
        });
        $scope.$watch('file', function () {
            if ($scope.file != null) {
                $scope.files = [$scope.file]; 
            }
        });
        $scope.log = '';

        if ($scope.tipo === 'IMAGEM') {
            $scope.upload = function (dataUrl) {
                Upload.upload({url: 'https://angular-file-upload-cors-srv.appspot.com/upload', data: {file: Upload.dataUrltoBlob(dataUrl)},}).
                then(
                    function (response) {
                        $timeout(function () {$scope.result = response.data;});
                    }, 
                    function (response) {
                        if (response.status > 0) {
                            $scope.errorMsg = response.status + ': ' + response.data;
                        }
                    }, 
                    function (evt) {
                        $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                    }
                );
            };
        } else {
            $scope.upload = function (files) {
                if (files && files.length) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        if (!file.$error) {
                            Upload.upload({url: 'https://angular-file-upload-cors-srv.appspot.com/upload', data: {username: $scope.username, file: file}}).
                            progress(function (evt) {
                                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                                $scope.log = 'progress: ' + progressPercentage + '% ' + evt.config.data.file.name + '\n' + $scope.log;
                            }).
                            success(function (data, status, headers, config) {
                                $timeout(function() {$scope.log = 'file: ' + config.data.file.name + ', Response: ' + JSON.stringify(data) + '\n' + $scope.log;});
                            });
                        }
                    }
                }
            };
        }
    }]);

    // diretiva da barra de navegação de dados
    frzArquivoModule.directive('frzArquivo', ['$rootScope', function($rootScope) {
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-arquivo/frz-arquivo.html',
            scope: {
                ngModel: '=',
                exibeNomeBotao: '=',
                onAbrir: '&',
            },
            controller: 'FrzArquivoCtrl',
            link: function(scope, element, attributes) {
                scope.exibeNomeBotao = !angular.isUndefined(attributes.exibeNomeBotao) && (attributes.exibeNomeBotao.toLowerCase() === 'true');
                scope.exibeEstadoAtual = !angular.isUndefined(attributes.exibeEstadoAtual) && (attributes.exibeEstadoAtual.toLowerCase() === 'true');
                scope.funcionalidade = !angular.isUndefined(scope.funcionalidade) && scope.funcionalidade ? scope.funcionalidade.toUpperCase().trim() : null;
                scope.estados = {
                    'FILTRANDO': {botoes: ['ok', 'inclusao', 'limpar', 'voltar', 'acao', 'ajuda', ], },
                    'LISTANDO': {botoes: ['informacao', 'filtro', 'inclusao', 'primeiro', 'anterior', 'tamanhoPagina', 'proximo', 'ultimo', 'visualizacao', 'exclusao', 'acao', 'ajuda', ], }, 
                    'ESPECIAL': {botoes: ['informacao', 'inclusao', 'primeiro', 'anterior', 'tamanhoPagina', 'proximo', 'ultimo', 'edicao', 'exclusao', 'acao', 'ajuda', ], },
                    'INCLUINDO': {botoes: ['ok', 'limpar', 'cancelar', 'ajuda', ], },
                    'VISUALIZANDO': {botoes: ['informacao', 'filtro', 'inclusao', 'primeiro', 'anterior', 'proximo', 'ultimo', 'edicao', 'voltar', 'exclusao', 'acao', 'ajuda', ], },
                    'EDITANDO': {botoes: ['ok', 'restaurar', 'cancelar', 'ajuda', ], },
                    'EXCLUINDO': {botoes: ['ok', 'cancelar', 'ajuda', ], },
                };
                // executar o estado inicial do arquivo
                if (scope.onAbrir) {
                    scope.onAbrir();
                }
            },
        };
    }]);

}());