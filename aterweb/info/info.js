(function (pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);

    angular.module(pNmModulo).config(['$stateProvider', '$sceDelegateProvider', function($stateProvider, $sceDelegateProvider) {
        'ngInject';

        $stateProvider.state('info', {
          url: '/info/:nome/:endereco/:programa/:xw_width/:xw_height',
          templateUrl: 'info/info.html',
          controller: 'InfoCtrl'
        });

        $sceDelegateProvider.resourceUrlWhitelist([
            'self',
            'http://localhost/**',
            'http://localhost:8888/**',
            'https://cas.gdfnet.df.gov.br',
            'http://www.emater.df.gov.br',
            'http://homologa.emater.df.gov.br',
            'http://homologa.emater.df.gov.br/**',
            'http://extranet.emater.df.gov.br',
            'http://extranet.emater.df.gov.br/**',
            'https://painel.emater.df.gov.br',
            'http://web.emater.df.gov.br',
            'http://web.emater.df.gov.br/**',
        ]);
    }]);
  
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PessoaSrv', '$rootScope', '$http', '$sce',
        function($scope, toastr, FrzNavegadorParams, $state, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, PessoaSrv, $rootScope, $http, $sce) {
            'ngInject';
            $scope.nomeFormulario = pNmFormulario;
            $scope.nome           = $state.params.nome;
            $scope.endereco       = $state.params.endereco;
            $scope.programa       = $state.params.programa;
            $scope.xw_width       = $state.params.xw_width;
            $scope.xw_height      = $state.params.xw_height;
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
                var url = "";
                if(src.indexOf("ematerweb")===0){
                    //url = "http://extranet.emater.df.gov.br/ematerweb/index.php";
                    url = "http://web.emater.df.gov.br/aterphp/index-java.php";
                    //url = "http://homologa.emater.df.gov.br/aterphp/index-java.php";
                    //url = "http://localhost:8888/aterphp/index-java.php";
                    return $scope.trustSrc(url) + "?user="+ $scope.trustUsr() + 
                                                  "&modulo="+$scope.programa ;
                } else if(src.indexOf("video")===0){
                    url = "http://web.emater.df.gov.br/aterphp/videos/videos.php?video=";
                    //url = "http://localhost:8888/aterphp/videos/videos.php?video=";
                    return $scope.trustSrc(url) + $scope.programa ;
                } else  {
                    return $scope.trustSrc(src);
                }
            };

        }
    ]);

}) ('info', 'InfoCtrl', 'Info', 'info');