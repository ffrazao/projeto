/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
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
                submetido: $scope.navegador.submetido,
                codigo: 'formulario',
                tipo: 'array',
                nome: 'Formulário',
                opcao:
                    [
                        {
                            nome: 'Nome do Formulário',
                            codigo: 'nome',
                            tipo: 'string',
                            requerido: function() {return true;},
                        },
                        {
                            nome: 'Código do Formulário',
                            codigo: 'codigo',
                            tipo: 'string',
                            requerido: function() {return true;},
                            tamanho: 3,
                        },
                        {
                            nome: 'Situação',
                            codigo: 'situacao',
                            tipo: 'escolha_unica',
                            requerido: function() {return true;},
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
                            requerido: function() {return true;},
                            tamanho: 2,
                        },
                        {
                            nome: 'Término da Coleta',
                            codigo: 'termino',
                            tipo: 'data',
                            tamanho: 2,
                        },
                        {
                            codigo: 'formularioVersaoList',
                            tipo: 'array',
                            nome: 'Versões do Formulário',
                            requerido: function() {return true;},
                            opcao:
                                [
                                    {
                                        nome: 'Versão',
                                        codigo: 'versao',
                                        tipo: 'numero',
                                        fracao: '-1',
                                        tamanho: 2,
                                        somenteLeitura: true,
                                    },
                                    {
                                        nome: 'Data de Início de Validade',
                                        codigo: 'data',
                                        tipo: 'data',
                                        requerido: function() {return false;},
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
                                                    requerido: function() {return true;},
                                                    exibir: function(dados) {
                                                        if (!dados) {
                                                            return null;
                                                        }
                                                        var str = 
                                                            '<table class="table table-hover table-striped">' + 
                                                            '    <thead>'+
                                                            '       <tr>' +
                                                            '           <th>Nome</th>' +
                                                            '           <th>Código</th>' +
                                                            '           <th>Tipo</th>' +
                                                            '       </tr>' +
                                                            '    </thead>' +
                                                            '    <tbody>' +
                                                            '       <tr>' +
                                                            '           <td>' + dados.nome + '</td>' +
                                                            '           <td>' + dados.codigo + '</td>' +
                                                            '           <td>' + dados.tipo + '</td>' +
                                                            '       </tr>' +
                                                            '    <tbody>' +
                                                            '</table>';
                                                        return str;
                                                    },
                                                    opcao: [
                                                        {
                                                            nome: 'Nome',
                                                            codigo: 'nome',
                                                            tipo: 'string',
                                                            requerido: function() {return true;},
                                                        },
                                                        {
                                                            nome: 'Código',
                                                            codigo: 'codigo',
                                                            tipo: 'string',
                                                            requerido: function() {return true;},
                                                        },
                                                        {
                                                            nome: 'Função Requerido',
                                                            codigo: 'requerido',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Esconder no Formulário',
                                                            codigo: 'escondeForm',
                                                            tipo: 'escolha_unica',
                                                            opcao: {
                                                                codigo: 'id',
                                                                descricao: 'nome',
                                                                lista: [{id: 'S', nome: 'Sim'}, {id: 'N', nome: 'Não'}],
                                                            },
                                                        },
                                                        {
                                                            nome: 'Esconder na Lista',
                                                            codigo: 'escondeLista',
                                                            tipo: 'escolha_unica',
                                                            opcao: {
                                                                codigo: 'id',
                                                                descricao: 'nome',
                                                                lista: [{id: 'S', nome: 'Sim'}, {id: 'N', nome: 'Não'}],
                                                            },
                                                        },
                                                        {
                                                            nome: 'Tipo',
                                                            codigo: 'tipo',
                                                            tipo: 'combo_unico',
                                                            requerido: function() {return true;},
                                                            opcao: {
                                                                codigo: 'id',
                                                                descricao: 'nome',
                                                                lista: 
                                                                    [
                                                                        {id: 'array', nome: 'Conjunto de registros', 
                                                                         opcao: [
                                                                                {
                                                                                    nome: 'Nome',
                                                                                    codigo: 'nome',
                                                                                    tipo: 'string',
                                                                                    requerido: function() {return true;},
                                                                                },
                                                                                {
                                                                                    nome: 'Código',
                                                                                    codigo: 'codigo',
                                                                                    tipo: 'string',
                                                                                    requerido: function() {return true;},
                                                                                },
                                                                                {
                                                                                    nome: 'Função Requerido',
                                                                                    codigo: 'requerido',
                                                                                    tipo: 'memo',
                                                                                },
                                                                            ],
                                                                            }, 
                                                                        {id: 'cep', nome: 'CEP - Código de Endereçamento Postal'}, 
                                                                        {id: 'combo_multiplo', nome: 'Combo Multiplo'}, 
                                                                        {id: 'combo_unico', nome: 'Combo Único'}, 
                                                                        {id: 'data', nome: 'Data'}, 
                                                                        {id: 'escolha_multipla', nome: 'Escolha multipla'}, 
                                                                        {id: 'escolha_multipla_objeto', nome: 'Escolha Multipla (com Objetos)'}, 
                                                                        {id: 'escolha_unica', nome: 'Escolha Única'}, 
                                                                        {id: 'memo', nome: 'Memo'}, 
                                                                        {id: 'numero', nome: 'Número'}, 
                                                                        {id: 'objeto', nome: 'Objeto'}, 
                                                                        {id: 'resumo_numero', nome: 'Resumo Numérico'}, 
                                                                        {id: 'string', nome: 'Letras e Números'}, 
                                                                    ],
                                                            },

                                                        },
                                                        {
                                                            nome: 'Função Incluir Antes',
                                                            codigo: 'funcaoIncluirAntes',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Função Editar Antes',
                                                            codigo: 'funcaoEditarAntes',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Função Excluir Antes',
                                                            codigo: 'funcaoExcluirAntes',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Função Excluir Depois',
                                                            codigo: 'funcaoExcluirDepois',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Função Salvar Depois',
                                                            codigo: 'funcaoSalvarDepois',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Opções',
                                                            codigo: 'opcao',
                                                            tipo: 'objeto',
                                                            opcao: $scope.dados['tipo']['opcao'],
                                                        },
                                                    ],
                                                },
                                                {
                                                    nome: 'Ordem',
                                                    codigo: 'ordem',
                                                    tipo: 'numero',
                                                    fracao: '-1',
                                                    requerido: function() {return true;},
                                                    tamanho: 2,
                                                },
                                            ],
                                    },
                                ],
                        },
                    ],
            };
            // fim ações especiais

            // inicio trabalho tab
            // fim trabalho tab

            // inicio dos watches
           
            // fim dos watches
        }
    ]);
})('formulario', 'FormularioCtrl', 'Cadastro de Formularios', 'formulario');