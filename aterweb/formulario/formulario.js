/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$modal', '$log', '$modalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'FormularioSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $modal, $log, $modalInstance, modalCadastro, UtilSrv, mensagemSrv, FormularioSrv) {

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, FormularioSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($modalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                $modalInstance.close($scope.navegador.selecao);
            };
            $scope.modalCancelar = function() {
                // Cancelar a modal
                $modalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.modalAbrir = function(size) {
                // abrir a modal
                var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
                var modalInstance = $modal.open({
                    animation: true,
                    template: template,
                    controller: pNmController,
                    size: size,
                    resolve: {
                        modalCadastro: function() {
                            return {filtro: {}, lista: [], registro: {}, original: {}, apoio: [],};
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
            $scope.cadastro.apoio.formulario = {
                codigo: 'formulario',
                tipo: 'array',
                nome: 'Formulário',
                opcao:
                    [
                        {
                            nome: 'Nome do Formulário',
                            codigo: 'nome',
                            tipo: 'string',
                            requerido: true,
                        },
                        {
                            nome: 'Código do Formulário',
                            codigo: 'codigo',
                            tipo: 'string',
                            requerido: true,
                            tamanho: 3,
                        },
                        {
                            nome: 'Situação',
                            codigo: 'situacao',
                            tipo: 'escolha_unica',
                            requerido: true,
                            opcao: {
                                codigo: 'codigo',
                                descricao: 'descricao',
                                lista: [{codigo: 'A', descricao: 'Ativo'},{codigo: 'I', descricao: 'Inativo'},],
                            }
                        },
                        {
                            nome: 'Início da Coleta',
                            codigo: 'inicio',
                            tipo: 'data',
                            requerido: true,
                            tamanho: 2,
                        },
                        {
                            nome: 'Término da Coleta',
                            codigo: 'termino',
                            tipo: 'data',
                            requerido: true,
                            tamanho: 2,
                        },
                        {
                            codigo: 'formularioVersaoList',
                            tipo: 'array',
                            nome: 'Versões do Formulário',
                            opcao:
                                [
                                    {
                                        nome: 'Versão',
                                        codigo: 'versao',
                                        tipo: 'numero',
                                        fracao: '-1',
                                        requerido: true,
                                        tamanho: 2,
                                        somenteLeitura: true,
                                    },
                                    {
                                        nome: 'Data de Início de Validade',
                                        codigo: 'data',
                                        tipo: 'data',
                                        requerido: true,
                                        tamanho: 2,
                                    },
                                    {
                                        codigo: 'formularioVersaoElementoList',
                                        tipo: 'array',
                                        nome: 'Elementos do Formulário',
                                        opcao:
                                            [
                                                {
                                                    nome: 'Elemento',
                                                    codigo: 'elemento',
                                                    tipo: 'objeto',
                                                    requerido: true,
                                                    opcao: [
                                                        {
                                                            nome: 'Nome',
                                                            codigo: 'nome',
                                                            tipo: 'string',
                                                            requerido: true,
                                                        },
                                                        {
                                                            nome: 'Código',
                                                            codigo: 'codigo',
                                                            tipo: 'string',
                                                            requerido: true,
                                                        },
                                                        {
                                                            nome: 'Tipo',
                                                            codigo: 'tipo',
                                                            tipo: 'string',
                                                            requerido: true,
                                                        },
                                                    ],
                                                },
                                                {
                                                    nome: 'Ordem',
                                                    codigo: 'ordem',
                                                    tipo: 'numero',
                                                    fracao: '-1',
                                                    requerido: true,
                                                    tamanho: 2,
                                                },
                                            ],
                                    },
                                ],
                        },
                    ],
            }
            // fim ações especiais

            // inicio trabalho tab
            // fim trabalho tab

            // inicio dos watches
           
            // fim dos watches
        }
    ]);
})('formulario', 'FormularioCtrl', 'Cadastro de Formularios', 'formulario');