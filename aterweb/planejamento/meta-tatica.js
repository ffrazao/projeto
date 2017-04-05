/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';

        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'MetaTaticaSrv', 'SegurancaSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, MetaTaticaSrv, SegurancaSrv) {
            'ngInject';
            
            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario);

            var filtro = {};
            filtro.ano = 2017;
            MetaTaticaSrv.filtrar( filtro ).success( function( resposta ){  
                if( resposta.mensagem === "OK" ) {
                    $scope.navegador.dados = resposta.resultado ; 
                } 
            } );
            //$scope.navegador.dados = $scope.cadastro.apoio.metaTaticaList ; 


            
            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'meta-tatica', modalCadastro, pNmFormulario);
            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                $uibModalInstance.close({cadastro: angular.copy($scope.cadastro), selecao: angular.copy($scope.navegador.selecao)});
            };
            $scope.modalCancelar = function() {
                // Cancelar a modal
                $uibModalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.modalAbrir = function(size) {
                // abrir a modal
                var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: template,
                    controller: pNmController,
                    size: size,
                    resolve: {
                        modalCadastro: function() {
                            return $scope.cadastroBase();
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal
                    $scope.navegador.setDados(cadastroModificado.lista);
                }, function() {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
            // fim: atividades do Modal


            
            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $rootScope.abrir(scp);
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
            $scope.UtilSrv = UtilSrv;
            $scope.authoritiesCompare = function(obj1, obj2) {
                return obj1.perfil.id === obj2.perfil.id;
            };

            

            // nomes dos campos para listagem

            // fim ações especiais

            // inicio trabalho tab
            
            // fim trabalho tab

            // inicio dos watches

            // fim dos watches
        }
    ]);
})('metaTatica', 'MetaTaticaCtrl', 'Cadastro de Metas Taticas', 'Meta-Tatica');