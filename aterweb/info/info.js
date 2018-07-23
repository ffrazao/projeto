(function (pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);

    angular.module(pNmModulo).config(['$stateProvider', '$sceDelegateProvider', function($stateProvider, $sceDelegateProvider) {
        'ngInject';

        $stateProvider.state('info', {
          url: '/info/:nome/:endereco/:programa',
          templateUrl: 'info/info.html',
          controller: 'InfoCtrl'
        });

        $sceDelegateProvider.resourceUrlWhitelist([
            'self',
            'https://cas.gdfnet.df.gov.br',
            'http://www.emater.df.gov.br',
            'http://extranet.emater.df.gov.br',
            'http://extranet.emater.df.gov.br/**',
            'https://painel.emater.df.gov.br',
        ]);
    }]);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PessoaSrv', '$rootScope', '$http', '$sce',
        function($scope, toastr, FrzNavegadorParams, $state, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, PessoaSrv, $rootScope, $http, $sce) {
            'ngInject';
            $scope.nomeFormulario = pNmFormulario;
            $scope.nome           = $state.params.nome;
            $scope.endereco       = $state.params.endereco;
            $scope.programa       = $state.params.programa;
            $scope.user           = $rootScope.token.username;
            $scope.unidade        = $rootScope.token.lotacaoAtual.apelidoSigla;
            $scope.unidadeTipo    = $rootScope.token.lotacaoAtual.classificacao;
            $scope.trustSrc = function(src) {
                return $sce.trustAsResourceUrl(src);
            };
            $scope.trustUsr = function() {
                return $rootScope.token.username;
            };
            $scope.trustURL = function(src) {
                if(src.indexOf("ematerweb")<0){
                    return $scope.trustSrc(src);
                } else  {
                    var url = "http://extranet.emater.df.gov.br/ematerweb/index.php";
                    return $scope.trustSrc(url) + "?user="+ $scope.trustUsr() + 
                                                  "&modulo="+$scope.programa +
                                                  "&unidade="+$scope.unidade +
                                                  "&unidadeTipo="+$scope.unidadeTipo;
                }
            };

        }
    ]);

}) ('info', 'InfoCtrl', 'Info', 'info');