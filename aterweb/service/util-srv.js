/*jslint evil: true */ 

(function(pNmModulo, pNmFactory) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap', 'ui.router', 'ngSanitize', 'ngAnimate', 'toastr', 'sticky',
  'ui.mask', 'ui.utils.masks',
  ]);

angular.module(pNmModulo).factory(pNmFactory, ['$rootScope', '$http', 'toastr', 'Upload', '$timeout',
    function($rootScope, $http, toastr, Upload, $timeout) {
        var erroInterno = function(data) {
            if (data && data.mensagem) {
                toastr.error('error', 'Erro ao Recuperar Informações', data.mensagem);
            } else {
                document.write(data);
            }
        };
        var sucessoInterno = function (ls, cb) {
            return function(data) {
                if (data && data.mensagem && data.mensagem === "OK") {
                    if (cb) {
                        cb(data.resultado[0]);
                    } else {
                        if (!ls) {
                            ls = [];
                        }
                        ls.splice(0, ls.length);
                        for (var i in data.resultado[0]) {
                            ls.push(data.resultado[0][i]);
                        }
                    }
                } else {
                    erroInterno(data);
                }
            };
        };
        var UtilSrv = {
            indiceDe : function(arr, item) {
                for (var i = arr.length; i--;) {
                    if (angular.equals(arr[i], item)) {
                        return i;
                    }
                }
                return null;
            },
            indiceDePorCampo : function(arr, item, campo) {
                if (!arr || !item || ! campo) {
                    return null;
                }
                for (var i = arr.length; i--;) {
                    if (angular.equals(arr[i][campo], item)) {
                        return arr[i];
                    }
                }
                return null;
            },
            dominio: function (parametroPesquisa) {
                return $http.get($rootScope.servicoUrl + '/dominio', {params: parametroPesquisa, cache: true});
            },
            dominioLista: function (lista, parametroPesquisa, callback) {
                this.dominio(parametroPesquisa).success(sucessoInterno(lista, callback)).error(erroInterno);
            },
            uploadImagem: function(dataUrl) {
                if (!dataUrl) {
                    toastr.error("Erro", "Informação vazia!");
                    return;
                }
                return Upload.upload({url: $rootScope.servicoUrl + '/perfil', data: {file: Upload.dataUrltoBlob(dataUrl)},});
            },
            uploadArquivos: function(files) {
                if (files && files.length) {
                    var retorno = [];
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        if (!file.$error) {
                            retorno.push(Upload.upload({url: $rootScope.servicoUrl + '/arquivo-subir', data: {file: file}}));
                        }
                    }
                    return retorno;
                }
            },
        };
        return UtilSrv;
    }
]);

})('utilSrv', 'UtilSrv');