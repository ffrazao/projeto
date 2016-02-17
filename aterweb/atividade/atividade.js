/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo */ 
/* jshint esnext: true */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    var ordem = 0;
    const ATIV_ASSUNTO_LIST = {
        ATIV_ASSUNTO_ID : ordem++,
        ATIV_ASSUNTO_ASSUNTO_ID : ordem++,
        ATIV_ASSUNTO_ASSUNTO_NOME : ordem++,
        ATIV_ASSUNTO_OBSERVACAO : ordem++,
    };
    ordem = 0;
    const ATIV_PESS_LIST = {
        ATIV_PESS_ID : ordem++,
        ATIV_PESS_PESSOA_ID : ordem++,
        ATIV_PESS_PESSOA_NOME : ordem++,
        ATIV_PESS_RESPONSAVEL : ordem++,
        ATIV_PESS_INICIO : ordem++,
        ATIV_PESS_ATIVO : ordem++,
        ATIV_PESS_TERMINO : ordem++,
        ATIV_PESS_DURACAO : ordem++,
    };
    ordem = 0;
    const OCORR_LIST = {
        OCORR_ID : ordem++,
        OCORR_USUARIO_ID : ordem++,
        OCORR_USUARIO_PESSOA_NOME : ordem++,
        OCORR_REGISTRO : ordem++,
        OCORR_RELATO : ordem++,
        OCORR_AUTOMATICO : ordem++,
        OCORR_INCIDENTE : ordem++,
    };
    ordem = 0;
    const CABEC = {
        ATIV_ID : ordem++,
        ATIV_CODIGO : ordem++,
        ATIV_FORMATO : ordem++,
        ATIV_FINALIDADE : ordem++,
        ATIV_NATUREZA : ordem++,
        ATIV_PRIORIDADE : ordem++,
        ATIV_INICIO : ordem++,
        ATIV_PREVISAO_CONCLUSAO : ordem++,
        ATIV_CONCLUSAO : ordem++,
        ATIV_DURACAO_ESTIMADA : ordem++,
        ATIV_DURACAO_REAL : ordem++,
        ATIV_DURACAO_SUSPENSAO : ordem++,
        ATIV_METODO_ID : ordem++,
        ATIV_METODO_NOME : ordem++,
        ATIV_PUBLICO_ESTIMADO : ordem++,
        ATIV_PUBLICO_REAL : ordem++,
        ATIV_SITUACAO : ordem++,
        ATIV_SITUACAO_DATA : ordem++,
        ATIV_PERCENTUAL_CONCLUSAO : ordem++,
        ATIV_DETALHAMENTO : ordem++,
        ATIV_INCLUSAO_USUARIO_ID : ordem++,
        ATIV_INCLUSAO_USUARIO_PESSOA_NOME : ordem++,
        ATIV_INCLUSAO_DATA : ordem++,
        ATIV_ALTERACAO_USUARIO_ID : ordem++,
        ATIV_ALTERACAO_USUARIO_PESSOA_NOME : ordem++,
        ATIV_ALTERACAO_DATA : ordem++,
        ATIV_ASSUNTO_LIST : [ordem++, ATIV_ASSUNTO_LIST],
        DEMAND_LIST : [ordem++, ATIV_PESS_LIST],
        EXECUT_LIST : [ordem++, ATIV_PESS_LIST],
        OCORR_LIST : [ordem++, OCORR_LIST],
    };

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).constant('CABEC', CABEC);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'AtividadeSrv',
        'TokenStorage', 'CABEC',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, AtividadeSrv,
            TokenStorage, CABEC) {

            $scope.CABEC = CABEC;

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, AtividadeSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
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

            // inicio das operaçoes atribuidas ao navagador

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais

            // nomes dos campos para listagem

            $scope.toggleChildren = function (scope) {
                scope.toggle();
            };
            $scope.selecionou = function (item, selecao) {
                item.selecionado = selecao.selected;
            };

            $scope.visible = function (item) {
                return !($scope.cadastro.apoio.localFiltro && 
                    $scope.cadastro.apoio.localFiltro.length > 0 && 
                    item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.visivel = function (filtro, no, folha) {
                if (!folha) {
                    return true;
                }
                return !(filtro && 
                    filtro.length > 0 && 
                    no.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1);
            };
            
            $scope.UtilSrv = UtilSrv;

            $scope.hoveringOver = function(value) {
                $scope.overStar = value;
                $scope.percent = 100 * (value / $scope.max);
            };

            // fim ações especiais

            // inicio trabalho tab
            
            // fim trabalho tab

            // inicio dos watches

            // fim dos watches
        }
    ]);
})('atividade', 'AtividadeCtrl', 'Cadastro de Atividades', 'atividade');