(function (pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);

    angular.module(pNmModulo).config(['$stateProvider', '$sceDelegateProvider', function($stateProvider, $sceDelegateProvider) {
        'ngInject';

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
            'ngInject';
            $scope.nomeFormulario = pNmFormulario;
            $scope.nome           = $state.params.nome;
            $scope.endereco       = $state.params.endereco;
            $scope.trustSrc = function(src) {
                return $sce.trustAsResourceUrl(src);
            };
        }
    ]);

}) ('info', 'InfoCtrl', 'Info', 'info');