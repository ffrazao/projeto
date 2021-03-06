/* global criarEstadosPadrao, toCamelCase, isUndefOrNull */ /* jslint evil: true */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';

        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'FormularioSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, FormularioSrv) {
            'ngInject';

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, FormularioSrv);

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
            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [
                    {
                        nome: 'Testar',
                        descricao: 'Testar a execução do formulário',
                        acao: function() {
                            var estado = $scope.navegador.estadoAtual();
                            if (estado === 'LISTANDO') {
                                if ($scope.navegador.selecao.tipo === 'U') {
                                    FormularioSrv.testar($scope, $scope.navegador.selecao.item[0]);
                                } else {
                                    for (var i in $scope.navegador.selecao.items) {
                                        FormularioSrv.testar($scope, $scope.navegador.selecao.items[i][0]);
                                    }
                                }
                            } else if (estado === 'VISUALIZANDO') {
                                FormularioSrv.testar($scope, $scope.cadastro.registro.id);
                            }
                        },
                        exibir: function() {
                            var estado = $scope.navegador.estadoAtual();
                            return (estado === 'VISUALIZANDO') || (estado === 'LISTANDO' && ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) ||
                                ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0));
                        },
                    },
                ];
                $rootScope.abrir(scp);
            };

            $scope.visualizarDepois = function(registro) {
                angular.copy(registro.formulario, registro);
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais

            $scope.cadastro.apoio.confirmacaoList = [];
            $scope.cadastro.apoio.elementoTipoList = [];
            $scope.cadastro.apoio.situacaoList = [];
            $scope.cadastro.apoio.formularioDestinoList = [];
            
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
                            funcaoRequerido: function() {return true;},
                        },
                        {
                            nome: 'Código do Formulário',
                            codigo: 'codigo',
                            tipo: 'string',
                            funcaoRequerido: function() {return true;},
                            tamanho: 3,
                        },
                        {
                            nome: 'Situação',
                            codigo: 'situacao',
                            tipo: 'escolha_unica',
                            funcaoRequerido: function() {return true;},
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
                            funcaoRequerido: function() {return true;},
                            tamanho: 2,
                        },
                        {
                            nome: 'Término da Coleta',
                            codigo: 'termino',
                            tipo: 'data',
                            tamanho: 2,
                        },
                        {
                            nome: 'Destino do Formulário',
                            codigo: 'destino',
                            tipo: 'escolha_unica',
                            funcaoRequerido: function() {return true;},
                            tamanho: 4,
                            opcao: {
                                codigo: 'codigo',
                                descricao: 'descricao',
                                lista: $scope.cadastro.apoio.formularioDestinoList,
                            },
                        },
                        {
                            nome: 'Subformulário',
                            codigo: 'subformulario',
                            tipo: 'escolha_unica',
                            funcaoRequerido: function() {return true;},
                            tamanho: 4,
                            opcao: {
                                codigo: 'codigo',
                                descricao: 'descricao',
                                lista: $scope.cadastro.apoio.confirmacaoList,
                            },
                        },
                        {
                            codigo: 'formularioVersaoList',
                            tipo: 'array',
                            nome: 'Versões do Formulário',
                            funcaoRequerido: function() {return true;},
                            opcao:
                                [
                                    {
                                        nome: 'Versão',
                                        codigo: 'versao',
                                        tipo: 'numero',
                                        tamanho: 2,
                                        somenteLeitura: true,
                                        funcaoRequerido: function() {return false;},
                                        opcao: {
                                            fracao: '0',
                                        }
                                    },
                                    {
                                        nome: 'Data de Início de Validade',
                                        codigo: 'inicio',
                                        tipo: 'data',
                                        funcaoRequerido: function() {return true;},
                                        tamanho: 2,
                                    },
                                    {
                                        codigo: 'formularioVersaoElementoList',
                                        tipo: 'array',
                                        nome: 'Elementos do Formulário',
                                        funcaoRequerido: function() {return true;},
                                        tamanho: 8,
                                        opcao:
                                            [
                                                {
                                                    nome: 'Elemento',
                                                    codigo: 'elemento',
                                                    tipo: 'objeto',
                                                    tamanho: 8,
                                                    funcaoRequerido: function() {return true;},
                                                    funcaoExibir: function(dados) {
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
                                                            '           <td>' + dados.tipo.nome + '</td>' +
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
                                                            funcaoRequerido: function() {return true;},
                                                        },
                                                        {
                                                            nome: 'Código',
                                                            codigo: 'codigo',
                                                            tipo: 'string',
                                                            funcaoRequerido: function() {return true;},
                                                        },
                                                        {
                                                            nome: 'Tipo',
                                                            codigo: 'tipo',
                                                            tipo: 'escolha_unica',
                                                            funcaoRequerido: function() {return true;},
                                                            opcao: {
                                                                codigo: 'codigo',
                                                                descricao: 'descricao',
                                                                lista: $scope.cadastro.apoio.elementoTipoList,
                                                            },

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
                                                            nome: 'Função Requerido',
                                                            codigo: 'requerido',
                                                            tipo: 'memo',
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
                                                            nome: 'Função Ao Iniciar',
                                                            codigo: 'funcaoAoIniciar',
                                                            tipo: 'memo',
                                                        },
                                                        {
                                                            nome: 'Opções',
                                                            codigo: 'opcao',
                                                            tipo: 'objeto',
                                                            tamanho: 8,
                                                            escondeForm: 'S',
                                                        },
                                                    ],
                                                },
                                                {
                                                    nome: 'Ordem',
                                                    codigo: 'ordem',
                                                    tipo: 'numero',
                                                    funcaoRequerido: function() {return true;},
                                                    tamanho: 2,
                                                    opcao: {
                                                        fracao: '0',
                                                    }
                                                },
                                            ],
                                    },
                                ],
                        },
                    ],
            };

            $scope.UtilSrv = UtilSrv;
            // fim ações especiais

            // inicio trabalho tab
            // fim trabalho tab

            // inicio dos watches
            $scope.$watch('cadastro.registro.nome', function() {
                if (isUndefOrNull($scope.cadastro.registro)) {
                    return;
                }
                var codigo = toCamelCase($scope.cadastro.registro.nome);
                if (isUndefOrNull(codigo)) {
                    return;
                }
                $scope.cadastro.registro.codigo = codigo.latinize();
            });

            $scope.$watch('cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual.nome', function() {
                if (isUndefOrNull($scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual)) {
                    return;
                }
                var codigo = toCamelCase($scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual.nome);
                if (isUndefOrNull(codigo)) {
                    return;
                }
                $scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual.codigo = codigo.latinize();
            }, true);

            $scope.$watch('cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual.tipo', function() {
                if (!$scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual) {
                    return;
                }
                var tipo = $scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual.tipo;
                var reg = {};
                for (var i in $scope.cadastro.apoio.elementoTipoList) {
                    if ($scope.cadastro.apoio.elementoTipoList[i].codigo === tipo) {
                        reg = $scope.cadastro.apoio.elementoTipoList[i];
                        break;
                    }
                }
                $scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].opcao[12].opcao = reg.opcao;
                $scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].formAtual.opcao = null;
                $scope.cadastro.apoio.formulario.opcao[7].opcao[2].opcao[0].opcao[12].escondeForm = reg.opcao ? 'N': 'S';

            }, true);

            // fim dos watches
        }
    ]);
})('formulario', 'FormularioCtrl', 'Cadastro de Formularios', 'formulario');