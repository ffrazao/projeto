/* global confirm */

(function (pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngSanitize', 'ngAnimate', 'toastr', 'sticky',
        'ui.mask', 'ui.utils.masks', 'ui.navbar', 'ngCookies', 'uiGmapgoogle-maps', 'ngFileUpload', 'ngTagsInput', 'ui.tree',
        'qrScanner', 'webcam', 
        'mensagemSrv', 'utilSrv']);

    angular.module(pNmModulo).config(['$stateProvider', '$sceDelegateProvider', function($stateProvider, $sceDelegateProvider) {
        'ngInject';

        $stateProvider.state('offline', {
          url: '/offline',
          templateUrl: 'offline/index.html',
          controller: pNmController,
        });

    }]);

    angular.module(pNmModulo).factory('$uibModalInstance', function () {
      'ngInject';
      return null;
    });
    angular.module(pNmModulo).factory('modalCadastro', function () {
      'ngInject';
      return null;
    });

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', '$state', '$uibModal', '$log', '$uibModalInstance', 'UtilSrv', 'mensagemSrv', '$rootScope', '$http', '$sce',
        function($scope, toastr, $state, $uibModal, $log, $uibModalInstance, UtilSrv, mensagemSrv, $rootScope, $http, $sce) {
            'ngInject';
            
            $scope.onSuccess = function(data) {
                console.log(data);
            };
            $scope.onError = function(error) {
                //console.log(error);
            };
            $scope.onVideoError = function(error) {
                //console.log(error);
            };

            $rootScope.globalLocalizacao = "pt-br";

            var keyOffline = "regOffLine";
            var salvaLista = function () {
                localStorage.setItem(keyOffline, JSON.stringify($scope.cadastro.lista));
            };

            if (!$scope.cadastro) {
                var lista = localStorage.getItem(keyOffline);
                $scope.cadastro = {lista: lista ? JSON.parse(lista) : [], registro: {}};
                $scope.indice = null;

            }
            $scope.salvar = function(duplicar) {
                if (!duplicar && $scope.indice !== null) {
                    $scope.cadastro.lista[$scope.indice] = angular.copy($scope.cadastro.registro);
                } else {
                    $scope.cadastro.lista.push(angular.copy($scope.cadastro.registro));
                }
                $scope.cadastro.registro = {};
                $scope.editando=false;
                salvaLista();
            };

            $scope.novo = function() {
                $scope.indice=null;
                $scope.cadastro.registro = {};
                $scope.editando=true;
            };
            $scope.editar = function(indice) {
                $scope.indice = indice;
                $scope.cadastro.registro = angular.copy($scope.cadastro.lista[indice]);
                $scope.editando=true;
            };
            $scope.excluir = function(indice) {
                $scope.cadastro.lista.splice(indice, 1);
                salvaLista();
            };
        }
    ]);

}) ('offline', 'OfflineCtrl', 'Módulo Offline', 'offline');

window.addEventListener('load', function(e) {
  window.applicationCache.addEventListener('updateready', function(e) {
    if (window.applicationCache.status === window.applicationCache.UPDATEREADY) {
      // Browser downloaded a new app cache.
      // Swap it in and reload the page to get the new hotness.
      window.applicationCache.swapCache();
      if (confirm('Uma nova versão do aplicativo esta disponível. Deseja atualizar agora?')) {
        window.location.reload();
      }
    } else {
      // Manifest didn't changed. Nothing new to server.
    }
  }, false);
}, false);
