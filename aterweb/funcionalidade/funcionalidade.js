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

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'FuncionalidadeSrv',
        'CABEC',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, FuncionalidadeSrv,
            CABEC) {

            $scope.CABEC = CABEC;

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, FuncionalidadeSrv);

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
            $scope.confirmarFiltrarAntes = function(filtro) {
                filtro.empresaList = [];
                filtro.unidadeOrganizacionalList = [];
                filtro.comunidadeList = [];
                var i, j, k;
                for (i in $scope.cadastro.apoio.localList) {
                    // filtrar as empressas
                    if ($scope.cadastro.apoio.localList[i].selecionado) {
                        filtro.empresaList.push({id: $scope.cadastro.apoio.localList[i].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica'});
                    } else {
                        for (j in $scope.cadastro.apoio.localList[i].unidadeList) {
                            // filtrar as unidades organizacionais
                            if ($scope.cadastro.apoio.localList[i].unidadeList[j].selecionado) {
                                filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id});
                            } else {
                                for (k in $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList) {
                                    // filtrar as unidades organizacionais
                                    if ($scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].selecionado) {
                                        filtro.comunidadeList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].id});
                                    }
                                }
                            }
                        }
                    }
                }                
                if ($scope.cadastro.apoio.unidadeOrganizacionalSomenteLeitura && !$scope.cadastro.filtro.unidadeOrganizacionalList.length && !$scope.cadastro.filtro.comunidadeList.length) {
                    toastr.error('Informe pelo menos uma comunidade', 'Erro ao filtrar');
                    throw 'Informe pelo menos uma comunidade';
                }
            };

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

            $scope.classeAtividade = function(situacao) {
                return {'atividade-cancelada' : situacao === 'X', 'atividade-nao-iniciada' : situacao === 'N'};
            };

            // fim ações especiais

            // inicio trabalho tab
            
            // fim trabalho tab

            // inicio dos watches

            // fim dos watches
        }
    ]);
})('funcionalidade', 'FuncionalidadeCtrl', 'Cadastro de Funcionalidades do Sistema', 'funcionalidade');