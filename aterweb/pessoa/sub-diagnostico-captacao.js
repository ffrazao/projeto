/* global StringMask:false, converterStringParaData */ /* jslint evil: true */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'PessoaSrv', 'FormularioSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, PessoaSrv, FormularioSrv) {

    $scope.cadastro.apoio.tecnicoUnidadeList = [
        {username: 'a', nome: 'Jose',},
        {username: 'b', nome: 'Maria',},
    ];

    /*UtilSrv.dominioLista($scope.cadastro.apoio.tecnicoUnidadeList, {ent:['Usuario']}, function(resultado) {
        if (!$scope.cadastro.apoio.tecnicoUnidadeList) {
            $scope.cadastro.apoio.tecnicoUnidadeList = [];
        }
        $scope.cadastro.apoio.tecnicoUnidadeList.length = 0;
        for (var i in resultado) {
            $scope.cadastro.apoio.tecnicoUnidadeList.push({username: resultado[i]['username'], nome: resultado[i]['pessoa']['nome'],});
        }
    });*/

    $scope.cadastro.apoio.coletaFrm = {
        nome: 'Coleta do Formulário',
        codigo: 'coletaList',
        tipo: 'array',
        opcao: [
            {
                nome: 'Versão Formulário',
                codigo: 'formularioVersao',
                tipo: 'string',
                escondeLista: 'S',
                escondeForm: 'S',
            },
            {
                nome: 'Data da Coleta',
                codigo: 'dataColeta',
                tipo: 'data',
            },
            {
                nome: 'Coleta Finalizada?',
                codigo: 'finalizada',
                tipo: 'escolha_unica',
                opcao: {
                    codigo: 'codigo',
                    descricao: 'descricao',
                    lista: [
                        {
                            codigo: 'S', 
                            descricao: 'Sim',
                        },
                        {
                            codigo: 'N', 
                            descricao: 'Não',
                        },
                    ],
                },
            },
            {
                nome: 'Usuario',
                codigo: 'usuario',
                tipo: 'objeto_exibe_string',
                somenteLeitura: 'S',
                exibirString: function(usuario) {
                    return usuario != null && usuario.pessoa != null ? usuario.pessoa.nome : "";
                },
                ver: function(lista, item) {
                    console.log(lista, item);
                },
                selecionar: function(lista, item) {
                    console.log(lista, item);
                },
                limpar: function(lista, item) {
                    console.log(lista, item);
                    lista[item] = null;
                },
            },
            {
                nome: 'Formulário',
                codigo: 'valor',
                tipo: 'objeto',
            },
        ],
        funcaoIncluirAntes: function(form, dd) {
            dd.formularioVersao = {id: $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].id};
            dd.usuario = $scope.token;
            dd.dataColeta = $scope.hoje();
            dd.finalizada = 'N';

            var id = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].versao;

            var f = this.opcao[4];
            if (!id || !versao) {
                toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                return;
            }
            FormularioSrv.visualizar(id).success(function (resposta) {
                if (resposta.mensagem === 'OK') {
                    var formulario = FormularioSrv.montar($scope, resposta.resultado.formulario, versao);
                    f.opcao = formulario.opcao;
                }
            });
        },
        funcaoEditarAntes: function(form, dd) {
            dd.formularioVersao = {id: $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].id};

            if (dd.valorString && !dd.valor) {
                var x = null;
                eval('x = ' + dd.valorString);
                // converter string para data
                x = converterStringParaData(x);
                console.log(x);
                dd.valor = x;
            }

            var id = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].versao;
            var f = this.opcao[4];
            if (!id || !versao) {
                toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                return;
            }
            FormularioSrv.visualizar(id).success(function (resposta) {
                if (resposta.mensagem === 'OK') {
                    var formulario = FormularioSrv.montar($scope, resposta.resultado.formulario, versao);
                    f.opcao = formulario.opcao;
                }
            });
        },
    };

    $scope.$on('abaDiagnosticoAtivada', function(e) {  
        /*if ($scope.cadastro.registro.diagnosticoList) {
            return;
        }*/
        if (!$scope.cadastro.apoio.filtroFormulario) {
            $scope.cadastro.apoio.filtroFormulario = {
                vigente: ['S'],
            };
        }
        $scope.cadastro.apoio.filtroFormulario.destino = ['PE'];
        if ($scope.cadastro.registro.publicoAlvoConfirmacao === 'S') {
            $scope.cadastro.apoio.filtroFormulario.destino.push('PA');
        }
        $scope.preparaClassePessoa($scope.cadastro.registro);
        $scope.cadastro.apoio.filtroFormulario.pessoa = {
            '@class': $scope.cadastro.registro['@class'], 
            'id': $scope.cadastro.registro['id'],
        };

        $scope.cadastro.apoio.filtroFormulario.subformulario = ['N'];
        PessoaSrv.formularioFiltrarComColeta($scope.cadastro.apoio.filtroFormulario).success(function (resposta) {
            if (resposta.mensagem === 'OK') {
                $scope.cadastro.registro.diagnosticoList = angular.copy(resposta.resultado);
                var i;
                for (i in $scope.cadastro.registro.diagnosticoList) {
                    if ($scope.cadastro.registro.diagnosticoList[i][8] && $scope.cadastro.registro.diagnosticoList[i][8][0]) {
                        $scope.cadastro.registro.diagnosticoList[i][9] = $scope.cadastro.registro.diagnosticoList[i][8][0];
                    } else {
                        $scope.cadastro.registro.diagnosticoList[i][9] = null;
                    }
                }
                $scope.pessoaDiagnosticoCaptacaoNvg.setDados($scope.cadastro.registro.diagnosticoList);
            }
        });

    });

    //$scope.cadastro.registro.diagnosticoList = [{"data":"01/03/2010 14:32","nome":"asdf","versao":"23"}];

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.diagnosticoList)) {
            $scope.cadastro.registro.diagnosticoList = [];
        }
        $scope.pessoaDiagnosticoCaptacaoNvg = new FrzNavegadorParams($scope.cadastro.registro.diagnosticoList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.diagnosticoList) {
            if (angular.equals($scope.cadastro.registro.diagnosticoList[j].diagnosticoCaptacao.endereco, conteudo.diagnosticoCaptacao.endereco)) {
                if ($scope.cadastro.registro.diagnosticoList[j].cadastroAcao === 'E') {
                    return true;
                } else {
                    toastr.error('Registro já cadastrado');
                    return false;
                }
            }
        }
        return true;
    };
    var editarItem = function (destino, item) {
        mensagemSrv.confirmacao(true, 'pessoa-diagnosticoCaptacao-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.diagnosticoCaptacao.endereco = angular.copy(conteudo.diagnosticoCaptacao.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.cadastro.registro.diagnosticoList) {
                    $scope.cadastro.registro.diagnosticoList = [];
                    $scope.pessoaDiagnosticoCaptacaoNvg.setDados($scope.cadastro.registro.diagnosticoList);
                }
                $scope.cadastro.registro.diagnosticoList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.pessoaDiagnosticoCaptacaoNvg.mudarEstado('ESPECIAL');
        $scope.pessoaDiagnosticoCaptacaoNvg.estados()['ESPECIAL'].botoes.push('filtro');
        $scope.pessoaDiagnosticoCaptacaoNvg.botao('filtro').exibir = function() {return true;};
        $scope.pessoaDiagnosticoCaptacaoNvg.botao('inclusao').exibir = function() {return false;};
        $scope.pessoaDiagnosticoCaptacaoNvg.botao('edicao').exibir = function() {return false;};
        $scope.pessoaDiagnosticoCaptacaoNvg.botao('exclusao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        var item = {diagnosticoCaptacao: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaDiagnosticoCaptacaoNvg.selecao.tipo === 'U' && $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item) {
            item = angular.copy($scope.pessoaDiagnosticoCaptacaoNvg.selecao.item);
            editarItem($scope.pessoaDiagnosticoCaptacaoNvg.selecao.item, item);
        } else if ($scope.pessoaDiagnosticoCaptacaoNvg.selecao.items && $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items.length) {
            for (i in $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.diagnosticoList) {
                    if (angular.equals($scope.pessoaDiagnosticoCaptacaoNvg.selecao.items[i], $scope.cadastro.registro.diagnosticoList[j])) {
                        item = angular.copy($scope.cadastro.registro.diagnosticoList[j]);
                        editarItem($scope.cadastro.registro.diagnosticoList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaDiagnosticoCaptacaoNvg.selecao.tipo === 'U' && $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.diagnosticoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.diagnosticoList[j].diagnosticoCaptacao.endereco, $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item.diagnosticoCaptacao.endereco)) {
                        //$scope.cadastro.registro.diagnosticoList.splice(j, 1);
                        $scope.cadastro.registro.diagnosticoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item = null;
                $scope.pessoaDiagnosticoCaptacaoNvg.selecao.selecionado = false;
            } else if ($scope.pessoaDiagnosticoCaptacaoNvg.selecao.items && $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.diagnosticoList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.diagnosticoList[j].diagnosticoCaptacao.endereco, $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items[i].diagnosticoCaptacao.endereco)) {
                            //$scope.cadastro.registro.diagnosticoList.splice(j, 1);
                            $scope.cadastro.registro.diagnosticoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaDiagnosticoCaptacaoNvg.selecao.items.splice(i, 1);
                }
            }
        }, function () {
        });
    };

    $scope.agir = function() {};
    $scope.ajudar = function() {};
    $scope.alterarTamanhoPagina = function() {};
    $scope.cancelar = function() {};
    $scope.cancelarEditar = function() {};
    $scope.cancelarExcluir = function() {};
    $scope.cancelarFiltrar = function() {};
    $scope.cancelarIncluir = function() {};
    $scope.confirmar = function() {};
    $scope.confirmarEditar = function() {};
    $scope.confirmarExcluir = function() {};
    $scope.confirmarFiltrar = function() {};
    $scope.confirmarIncluir = function() {};
    $scope.filtrar = function() {};
    $scope.folhearAnterior = function() {};
    $scope.folhearPrimeiro = function() {};
    $scope.folhearProximo = function() {};
    $scope.folhearUltimo = function() {};
    $scope.informacao = function() {};
    $scope.limpar = function() {};
    $scope.paginarAnterior = function() {};
    $scope.paginarPrimeiro = function() {};
    $scope.paginarProximo = function() {};
    $scope.paginarUltimo = function() {};
    $scope.restaurar = function() {};
    $scope.visualizar = function() {};
    $scope.voltar = function() {};
    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

})('pessoa', 'PessoaDiagnosticoCaptacaoCtrl', 'Captação de diagnósticos da pessoa');

/*
    $scope.cadastro.apoio.diagnosticoFrm = 
        {
            nome: 'Captar Diagnósticos',
            codigo: 'diagnosticoList',
            tipo: 'array',
            opcao:
                [
                    {
                        nome: 'Data da Coleta',
                        codigo: 'data',
                        tipo: 'data',
                    },
                    {
                        nome: 'Nome da Coleta',
                        codigo: 'nome',
                        tipo: 'string',
                    },
                    {
                        nome: 'Versão da Coleta',
                        codigo: 'versao',
                        tipo: 'string',
                    },
                    {
                        nome: 'Técnico Responsável pela coleta',
                        codigo: 'tecnico',
                        tipo: 'combo_unico',
                        opcao: {
                            lista: $scope.cadastro.apoio.tecnicoUnidadeList,
                            codigo: 'username',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Vinculação (Entidade/Organização)',
                        codigo: 'vinculacao',
                        tipo: 'combo_unico',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: 'Associação',
                                },
                                {
                                    id: 2, 
                                    nome: 'Sindicato',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Nome Entidade',
                        codigo: 'nomeEntidade',
                        tipo: 'string',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Benefício Social',
                        codigo: 'beneficioSocial',
                        tipo: 'combo_multiplo',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: 'Aposentadoria Pensão',
                                },
                                {
                                    id: 2, 
                                    nome: 'CTPS Assinada',
                                },
                                {
                                    id: 3, 
                                    nome: 'Necessidades Especiais',
                                },
                                {
                                    id: 4, 
                                    nome: 'Programas Sociais',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Forma de Utilização do Espaço Rural',
                        codigo: 'formaUtilizacaoEspacoRural',
                        tipo: 'combo_multiplo',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: 'Laser',
                                },
                                {
                                    id: 2, 
                                    nome: 'Moradia',
                                },
                                {
                                    id: 3, 
                                    nome: 'Outras',
                                },
                                {
                                    id: 4, 
                                    nome: 'Preservação Ambiental',
                                },
                                {
                                    id: 5, 
                                    nome: 'Prestação de Serviço',
                                },
                                {
                                    id: 6, 
                                    nome: 'Produção',
                                },
                                {
                                    id: 7, 
                                    nome: 'Turismo Rural',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Força de Trabalho Contratada Eventual R$',
                        codigo: 'forcaTrabalhoContratada',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Força de Trabalho',
                        codigo: 'forcaTrabalho',
                        tipo: 'combo_unico',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: '1',
                                },
                                {
                                    id: 2, 
                                    nome: '2',
                                },
                                {
                                    id: 3, 
                                    nome: '3',
                                },
                                {
                                    id: 4, 
                                    nome: '4',
                                },
                                {
                                    id: 5, 
                                    nome: '5',
                                },
                                {
                                    id: 6, 
                                    nome: '6',
                                },
                                {
                                    id: 7, 
                                    nome: '7',
                                },
                                {
                                    id: 8, 
                                    nome: '8',
                                },
                                {
                                    id: 9, 
                                    nome: '9',
                                },
                                {
                                    id: 10, 
                                    nome: '10',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Salário Mensal R$',
                        codigo: 'salarioMensal',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Fonte de Renda/ Patrimônio R$',
                        codigo: 'fonteRendaPatrimonio',
                        tipo: 'nome',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Renda Anual Bruta R$',
                        codigo: 'rendaAnualBruta',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Assalariado R$',
                        codigo: 'assalariado',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Outras Rendas R$',
                        codigo: 'outrasRendas',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Programa de Governo - Inclusão Social',
                        codigo: 'programaGovernoInclusaoSocial',
                        tipo: 'escolha_multipla',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: 'Brasil sem Miséria',
                                },
                                {
                                    id: 2, 
                                    nome: 'INCRA',
                                },
                                {
                                    id: 3, 
                                    nome: 'ATEPA',
                                },
                                {
                                    id: 4, 
                                    nome: 'Sustentabilidade',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Programa de Governo - Renda',
                        codigo: 'programaGovernoRenda',
                        tipo: 'escolha_multipla',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: 'PAA',
                                },
                                {
                                    id: 2, 
                                    nome: 'PNAE',
                                },
                                {
                                    id: 3, 
                                    nome: 'PAPA',
                                },
                                {
                                    id: 4, 
                                    nome: 'CAR',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Quantas pessoas no seu domicílio/casa, INCLUINDO VOCÊ, recebem algum tipo de renda (de trabalho, aposentadoria, pensão, benefício, bolsa, etc.) fora da renda proveniente das atividades agrícolas na propriedade?',
                        codigo: 'totalRendaAtivos',
                        tipo: 'escolha_unica',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: '1',
                                },
                                {
                                    id: 2, 
                                    nome: '2',
                                },
                                {
                                    id: 3, 
                                    nome: '3',
                                },
                                {
                                    id: 4, 
                                    nome: '4',
                                },
                                {
                                    id: 5, 
                                    nome: '5',
                                },
                                {
                                    id: 6, 
                                    nome: '6',
                                },
                                {
                                    id: 7, 
                                    nome: '7',
                                },
                                {
                                    id: 8, 
                                    nome: '8',
                                },
                                {
                                    id: 9, 
                                    nome: '9',
                                },
                                {
                                    id: 10, 
                                    nome: '10',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Somando a renda bruta (de trabalho, aposentadoria, pensão, benefício, bolsa etc.) de todas as pessoas do seu domicílio, INCLUINDO VOCÊ, qual é o total aproximado por mês?',
                        codigo: 'somaRendaBruta',
                        tipo: 'combo_unico',
                        escondeLista: 'S',
                        opcao: {
                            lista: [
                                {
                                    id: 1, 
                                    nome: 'Menos de R$ 415',
                                },
                                {
                                    id: 2, 
                                    nome: 'De R$ 415 a R$ 830',
                                },
                                {
                                    id: 3, 
                                    nome: 'De R$ 831 a R$ 1.660',
                                },
                                {
                                    id: 4, 
                                    nome: 'De R$ 1.661 a R$ 2.490',
                                },
                                {
                                    id: 5, 
                                    nome: 'De R$ 2.491 a R$ 4.150',
                                },
                                {
                                    id: 6, 
                                    nome: 'De R$ 4.151 a R$ 6.225',
                                },
                                {
                                    id: 7, 
                                    nome: 'De R$ 6.226 a R$ 9.130',
                                },
                                {
                                    id: 8, 
                                    nome: 'Acima de R$ 9.130',
                                },
                            ],
                            codigo: 'id',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Da renda bruta da família (de trabalho, aposentadoria, pensão, benefício, bolsa etc.) qual a porcentagem que vem de cada uma das atividades abaixo',
                        codigo: 'distribuicaoRenda',
                        tipo: 'nome',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Produção Animal %',
                        codigo: 'percentualProducaoAnimal',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Produção Vegetal %',
                        codigo: 'percentualProducaoVegetal',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Extrativismo %',
                        codigo: 'percentualExtrativismo',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Turismo e Artesanato %',
                        codigo: 'percentualTurismoArtesanato',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Benefícios Sociais %',
                        codigo: 'percentualBeneficioSocial',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Salários %',
                        codigo: 'percentualSalario',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Outros %',
                        codigo: 'percentualOutros',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Total Patrimônio R$',
                        codigo: 'totalPatrimonio',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },
                    {
                        nome: 'Total Dívida R$',
                        codigo: 'totalDivida',
                        tipo: 'numero',
                        escondeLista: 'S',
                    },

                ],
        };

*/