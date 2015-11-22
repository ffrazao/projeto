/* global criarEstadosPadrao, alert */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'frz.tabela']);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'FrzNavegadorParams', 'UtilSrv',
        function($scope, FrzNavegadorParams, UtilSrv) {

            $scope.apoio = {
                paisList: [],
                estadoList: [],
                municipioList: [],
                utilizacao: 0.0,
            };

            UtilSrv.dominioLista($scope.apoio.paisList, {ent: 'Pais'});

            $scope.documento = [
                    {
                        sigla: 'BSM',
                        nome: 'Brasil Sem Miséria',
                        telefone: [{ddd: '61', numero: '8443-3030'}, {ddd: '61', numero: '8441-3030'}],
                    },
                    {
                        nome: 'INCRA',
                    },
                    {
                        nome: 'ATEPA',
                    },
                ];

            $scope.formulario = {
                codigo: 'documento',
                tipo: 'array',
                nome: 'Documento',
                observar: [
                    {
                        expressao: 'dados.pais', 
                        funcao: function(novo) {
                            console.log('pais mudou', novo);
                            if (novo && novo.id) {
                                UtilSrv.dominioLista($scope.apoio.estadoList, {ent: 'Estado', npk: 'pais.id', vpk: novo.id});
                            } else {
                                $scope.apoio.estadoList.length = 0;
                            }
                        },
                        colecao: true,
                    },
                    {
                        expressao: 'dados.estado', 
                        funcao: function(novo) {
                            console.log('estado mudou', novo);
                            if (novo && novo.id) {
                                UtilSrv.dominioLista($scope.apoio.municipioList, {ent: 'Municipio', npk: 'estado.id', vpk: novo.id});
                            } else {
                                $scope.apoio.municipioList.length = 0;
                            }
                        },
                    },
                ],
                onIncluirAntes: function(form, dados) {
                    dados['nome'] = 'xpto';
                },
                onEditarAntes: function(form, dados) {
                    console.log ('vai editar', form, dados);
                },
                onSalvarDepois: function(form, dados, acao) {
                    console.log ('salvou', form, dados, acao);
                },
                onExcluirAntes: function(form, dados) {
                    console.log ('vai excluir', form, dados);
                },
                onExcluirDepois: function(form, dados) {
                    console.log ('excluiu', form, dados);
                },
                opcao: [
                    {
                        nome: 'Espaço Utilização da Área (ha)',
                        codigo: 'utilizacao',
                        tipo: 'resumo_numero',
                        fracao: 3,
                        observar: [
                            {
                                expressao: 'dados.pastagem + dados.reserva',
                                funcao: function(novo) {

                                },
                            },
                        ],
                        opcao: [
                            {
                                nome: 'Pastagem',
                                codigo: 'pastagem',
                                tipo: 'numero',
                                fracao: 3,
                            },
                            {
                                nome: 'Reserva',
                                codigo: 'reserva',
                                tipo: 'numero',
                                fracao: 3,
                            },
                            {
                                nome: 'Total',
                                codigo: 'utilizacao',
                                tipo: 'numero',
                                somenteLeitura: true,
                                fracao: 3,
                            },
                        ]
                    },
                    {
                        nome: 'Nome do Grupo',
                        codigo: 'nome',
                        tipo: 'string',
                    },
                    {
                        escondeLista: 'S',
                        nome: 'Sigla do Grupo',
                        codigo: 'sigla',
                        tipo: 'string',
                    },
                    {
                        escondeLista: 'S',
                        nome: 'Idade',
                        codigo: 'idade',
                        tipo: 'numero',
                        fracao: -1,
                    },
                    {
                        nome: 'Validade',
                        codigo: 'validade',
                        tipo: 'data',
                    },
                    {
                        nome: 'CEP',
                        codigo: 'cep',
                        tipo: 'cep',
                    },
                    {
                        nome: 'Observações',
                        codigo: 'observacao',
                        tipo: 'memo',
                    },
                    {
                        codigo: 'telefone',
                        tipo: 'array',
                        nome: 'Telefone',
                        observar: [
                            {   
                                expressao: 'dados["ramal"]', 
                                funcao: function(novo) {
                                    console.log('ramal mudou', novo);
                                },
                                colecao: true,
                            },
                        ],
                        opcao: [
                            {
                                nome: 'DDD',
                                codigo: 'ddd',
                                tipo: 'string',
                            },
                            {
                                nome: 'Número do Telefone',
                                codigo: 'numero',
                                tipo: 'string',
                            },
                            {
                                codigo: 'ramal',
                                tipo: 'array',
                                nome: 'ramal',
                                opcao: [
                                    {
                                        nome: 'Ramal',
                                        codigo: 'ramal',
                                        tipo: 'string',
                                    },
                                ],
                                observar: [
                                    {
                                        expressao: 'dados["ramal"]', 
                                        funcao: function(novo) {
                                            console.log('num mudou', novo);
                                        },
                                    },
                                ],
                            },
                        ],
                    },
                    {
                        nome: 'Pais',
                        codigo: 'pais',
                        tipo: 'combo_unico',
                        opcao: {
                            codigo: 'id',
                            descricao: 'nome',
                            lista: $scope.apoio.paisList,
                        },
                    },
                    {
                        nome: 'Estado',
                        codigo: 'estado',
                        tipo: 'combo_unico',
                        opcao: {
                            codigo: 'id',
                            descricao: 'nome',
                            lista: $scope.apoio.estadoList,
                        },
                    },
                    {
                        nome: 'Municipio',
                        codigo: 'municipio',
                        tipo: 'combo_multiplo',
                        opcao: {
                            codigo: 'id',
                            descricao: 'nome',
                            lista: $scope.apoio.municipioList,
                        },
                    },

                ],
            };
        }
    ]);
})('teste', 'TesteCtrl', 'Teste', 'teste');