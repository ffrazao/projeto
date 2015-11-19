/* global criarEstadosPadrao, alert */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'frz.tabela']);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'FrzNavegadorParams',
        function($scope, FrzNavegadorParams) {

            $scope.vlr = [
                    {
                        sigla: "BSM",
                        nome: "Brasil Sem Miséria",
                        telefone: [{ddd: '61', numero: '8443-3030'}, {ddd: '61', numero: '8441-3030'}],
                    },
                    {
                        nome: "INCRA",
                    },
                    {
                        nome: "ATEPA",
                    },
                ];

            $scope.formulario = {
                codigo: "documento",
                tipo: "array",
                nome: "Documento",
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
                        nome: 'Nome do Grupo',
                        codigo: 'nome',
                        tipo: 'string',
                    },
                    {
                        nome: 'Sigla do Grupo',
                        codigo: 'sigla',
                        tipo: 'string',
                    },
                    {
                        codigo: "telefone",
                        tipo: "array",
                        nome: "Telefone",
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
                                codigo: "ramal",
                                tipo: "array",
                                nome: "ramal",
                                opcao: [
                                    {
                                        nome: 'Ramal',
                                        codigo: 'ramal',
                                        tipo: 'string',
                                    },
                                ],
                            },
                        ],
                    },
                    {
                        nome: 'Pais',
                        codigo: 'pais',
                        tipo: 'escolha_unica',
                        opcao: [
                            {
                                descricao: 'Brasil',
                                codigo: 'br',
                            },
                            {
                                descricao: 'Argentina',
                                codigo: 'ar',
                            },
                            {
                                descricao: 'Paraguai',
                                codigo: 'pa',
                            },
                            {
                                descricao: 'Uruguai',
                                codigo: 'ur',
                            },
                        ],
                    },
                    {
                        nome: 'Estado',
                        codigo: 'estado',
                        tipo: 'escolha_unica',
                        opcao: [
                            {
                                descricao: 'Distrito Federal',
                                codigo: 'df',
                            },
                            {
                                descricao: 'Goias',
                                codigo: 'go',
                            },
                            {
                                descricao: 'Mato Grosso',
                                codigo: 'mt',
                            },
                            {
                                descricao: 'Mato Grosso do Sul',
                                codigo: 'ms',
                            },
                        ],
                    },
                    {
                        nome: 'Municipio',
                        codigo: 'municipio',
                        tipo: 'escolha_unica',
                        opcao: [
                            {
                                descricao: 'Goiania',
                                codigo: 'go',
                            },
                            {
                                descricao: 'Anapolís',
                                codigo: 'an',
                            },
                            {
                                descricao: 'Cristalina',
                                codigo: 'cr',
                            },
                            {
                                descricao: 'Catalão',
                                codigo: 'ct',
                            },
                        ],
                    },

                ],
            };
        }
    ]);
})('teste', 'TesteCtrl', 'Teste', 'teste');