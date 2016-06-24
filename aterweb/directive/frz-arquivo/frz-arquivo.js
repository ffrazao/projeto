/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */

(function() {
    'use strict';

    var frzArquivoModule = angular.module('frz.arquivo', ['ngFileUpload', 'ngImgCrop', 'webcam']);

    // controller para a barra de navegacao
    frzArquivoModule.controller('FrzArquivoCtrl', ['$scope', function($scope) {

        var _video = null, patData = null;

        $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

        // Setup a channel to receive a video property
        // with a reference to the video element
        // See the HTML binding in main.html
        $scope.channel = {};

        $scope.webcamError = false;
        $scope.onError = function (err) {
            $scope.$apply(
                function() {
                    $scope.webcamError = err;
                }
            );
        };

        $scope.onSuccess = function () {
            // The video element contains the captured camera data
            _video = $scope.channel.video;
            $scope.$apply(function() {
                $scope.patOpts.w = _video.width;
                $scope.patOpts.h = _video.height;
                //$scope.showDemos = true;
            });
        };

        $scope.onStream = function (stream) {
            // You could do something manually with the stream.
        };

        var getVideoData = function getVideoData(x, y, w, h) {
            var hiddenCanvas = document.createElement('canvas');
            hiddenCanvas.width = _video.width;
            hiddenCanvas.height = _video.height;
            var ctx = hiddenCanvas.getContext('2d');
            ctx.drawImage(_video, 0, 0, _video.width, _video.height);
            return ctx.getImageData(x, y, w, h);
        };

        /**
         * This function could be used to send the image data
         * to a backend server that expects base64 encoded images.
         *
         * In this example, we simply store it in the scope for display.
         */
        var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
            $scope.snapshotData = imgBase64;
        };

        $scope.makeSnapshot = function() {
            if (_video) {
                var patCanvas = document.querySelector('#snapshot');
                if (!patCanvas) {
                    return;
                }

                patCanvas.width = 240;//_video.width;
                patCanvas.height = 240;//_video.height;
                var ctxPat = patCanvas.getContext('2d');

                var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
                ctxPat.putImageData(idata, 0, 0);

                sendSnapshotToServer(patCanvas.toDataURL());

                patData = idata;
            }
        };
        
        /**
         * Redirect the browser to the URL given.
         * Used to download the image by passing a dataURL string
         */
        $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
            window.location.href = dataURL;
        };
        
        $scope.ativaWebcam = function() {
            $scope.snapshotData = null;
        };

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
                        scope.ngModel = "";
                        UtilSrv.uploadImagem(dataUrl).then(
                            function (response) {
                                $timeout(function () {
                                    scope.ngModel = {
                                        'md5': response.data.resultado.md5, 
                                        'nomeArquivo': response.data.resultado.arquivo,
                                    };
                                });
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
                        scope.ngModel = [];
                        scope.log = '';
                        var arquivos = UtilSrv.uploadArquivos(files);
                        for (var i in arquivos) {
                            arquivos[i].progress(function (evt) {
                                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                                scope.log = 'progress: ' + progressPercentage + '% ' + evt.config.data.file.name + '\n' + scope.log;
                            }).
                            success(function (data, status, headers, config) {
                                $timeout(function() {
                                    scope.log = 'file: ' + config.data.file.name + ', Response: ' + JSON.stringify(data) + '\n' + scope.log;
                                    for (var j in scope.ngModel) {
                                        if (angular.equals(scope.ngModel[j], data.resultado)) {
                                            return;
                                        }
                                    }

                                    var f, dataUpload, extensao, md5, nomeOriginal, tamanho, tipo, mimeTipo;

                                    f = config.data.file;

                                    dataUpload = moment().format('DD/MM/YYYY HH:mm:ss');
                                    extensao =  data.resultado.extensao;
                                    md5 = data.resultado.md5;
                                    nomeOriginal = f.name;
                                    tamanho = f.size;
                                    mimeTipo = f.type;
                                    tipo = 'A';

                                    var arquivo = {
                                        dataUpload : dataUpload,
                                        extensao : extensao,
                                        md5 : md5,
                                        nomeOriginal : nomeOriginal,
                                        tamanho : tamanho,
                                        tipo : tipo,
                                        mimeTipo: mimeTipo,
                                    };
                                    //data.resultado
                                    scope.ngModel.push(arquivo);
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