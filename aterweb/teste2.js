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
                        pais: "ur",
                        estado: [{
                                    reserva: 20,
                                    nome: 'Distrito Federal',
                                    id: 'df',
                                },
                                {
                                    reserva: 30,
                                    nome: 'Mato Grosso do Sul',
                                    id: 'ms',
                                },],
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
                        nome: 'Idade',
                        codigo: 'idade',
                        tipo: 'numero',
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
                        opcao: {
                            codigo: 'id',
                            descricao: 'nome',
                            lista: [
                                {
                                    populacao: 20,
                                    nome: 'Brasil',
                                    id: 'br',
                                },
                                {
                                    populacao: 100,
                                    nome: 'Argentina',
                                    id: 'ar',
                                },
                                {
                                    populacao: 10,
                                    nome: 'Paraguai',
                                    id: 'pa',
                                },
                                {
                                    populacao: 30,
                                    nome: 'Uruguai',
                                    id: 'ur',
                                },
                            ],
                        },
                    },
                    {
                        nome: 'Estado',
                        codigo: 'estado',
                        tipo: 'combo_multiplo',
                        opcao: {
                            codigo: 'id',
                            descricao: 'nome',
                            lista: [
                                {
                                    reserva: 20,
                                    nome: 'Distrito Federal',
                                    id: 'df',
                                },
                                {
                                    reserva: 100,
                                    nome: 'Goiás',
                                    id: 'go',
                                },
                                {
                                    reserva: 10,
                                    nome: 'Mato Grosso',
                                    id: 'mt',
                                },
                                {
                                    reserva: 30,
                                    nome: 'Mato Grosso do Sul',
                                    id: 'ms',
                                },
                            ],
                        },
                    },
                    {
                        nome: 'Municipio',
                        codigo: 'municipio',
                        tipo: 'combo_multiplo',
                        opcao: {
                            codigo: 'id',
                            descricao: 'nome',
                            lista: [
                                {
                                    nome: 'Goiania',
                                    id: 'go',
                                },
                                {
                                    nome: 'Anapolís',
                                    id: 'an',
                                },
                                {
                                    nome: 'Cristalina',
                                    id: 'cr',
                                },
                                {
                                    nome: 'Catalão',
                                    id: 'ct',
                                },
                            ],
                        },
                    },

                ],
            };
        }
    ]);
})('teste', 'TesteCtrl', 'Teste', 'teste');