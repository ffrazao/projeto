(function (pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);

    angular.module(pNmModulo).config(['$stateProvider', '$sceDelegateProvider', function($stateProvider, $sceDelegateProvider) {

        $stateProvider.state('info', {
          url: '/info/:nome/:endereco',
          templateUrl: 'info/info.html',
          controller: 'InfoCtrl'
        });

        $sceDelegateProvider.resourceUrlWhitelist([
            'self',
            'https://cas.gdfnet.df.gov.br',
            'http://www.emater.df.gov.br',
            'http://extranet.emater.df.gov.br',
            'http://extranet.emater.df.gov.br',
        ]);
    }]);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PessoaSrv', '$rootScope', '$http', '$sce',
        function($scope, toastr, FrzNavegadorParams, $state, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, PessoaSrv, $rootScope, $http, $sce) {
            $scope.nomeFormulario = pNmFormulario;
            $scope.nome           = $state.params.nome;
            $scope.endereco       = $state.params.endereco;
            $scope.login = function() {
                $http.post("https://cas.gdfnet.df.gov.br/owa/", {
                    flags:0,
                    forcedownlevel:0,
                    trusted:0,
                    username:'1227412@governo',
                    password:'.12274122006',
                    isUtf8:1,
                }).success(function(a,b,c,d,e) {
                    console.log(a,b,c,d,e);
                });
            };
            $scope.trustSrc = function(src) {
                return $sce.trustAsResourceUrl(src);
            };
        }
    ]);

}) ('info', 'InfoCtrl', 'Info', 'info');