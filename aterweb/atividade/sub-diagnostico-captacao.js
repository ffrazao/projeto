/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    $scope.cadastro.apoio.tecnicoUnidadeList = [];

    UtilSrv.dominioLista($scope.cadastro.apoio.tecnicoUnidadeList, {ent:['Usuario']}, function(resultado) {
            if (!$scope.cadastro.apoio.tecnicoUnidadeList) {
                $scope.cadastro.apoio.tecnicoUnidadeList = [];
            }
            $scope.cadastro.apoio.tecnicoUnidadeList.length = 0;
            for (var i in resultado) {
                $scope.cadastro.apoio.tecnicoUnidadeList.push({username: resultado[i]['username'], nome: resultado[i]['pessoa']['nome'],});
            }
        });

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
                        tipo: 'combo_unico_objeto',
                        opcao: {
                            lista: $scope.cadastro.apoio.tecnicoUnidadeList,
                            codigo: 'username',
                            descricao: 'nome',
                        },
                    },
                    {
                        nome: 'Vinculação (Entidade/Organização)',
                        codigo: 'vinculacao',
                        tipo: 'combo_unico_objeto',
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
                        tipo: 'combo_unico_objeto',
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
                        tipo: 'combo_unico_objeto',
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

    $scope.cadastro.registro.diagnosticoList = [{"data":"01/03/2010 14:32","nome":"asdf","versao":"23"}];

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.emailList)) {
            $scope.cadastro.registro.emailList = [];
        }
        $scope.pessoaEmailNvg = new FrzNavegadorParams($scope.cadastro.registro.emailList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.emailList) {
            if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, conteudo.email.endereco)) {
                if ($scope.cadastro.registro.emailList[j].cadastroAcao === 'E') {
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
        mensagemSrv.confirmacao(true, 'pessoa-email-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.email.endereco = angular.copy(conteudo.email.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                $scope.cadastro.registro.emailList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaEmailNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {email: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaEmailNvg.selecao.tipo === 'U' && $scope.pessoaEmailNvg.selecao.item) {
            item = angular.copy($scope.pessoaEmailNvg.selecao.item);
            editarItem($scope.pessoaEmailNvg.selecao.item, item);
        } else if ($scope.pessoaEmailNvg.selecao.items && $scope.pessoaEmailNvg.selecao.items.length) {
            for (i in $scope.pessoaEmailNvg.selecao.items) {
                for (j in $scope.cadastro.registro.emailList) {
                    if (angular.equals($scope.pessoaEmailNvg.selecao.items[i], $scope.cadastro.registro.emailList[j])) {
                        item = angular.copy($scope.cadastro.registro.emailList[j]);
                        editarItem($scope.cadastro.registro.emailList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaEmailNvg.selecao.tipo === 'U' && $scope.pessoaEmailNvg.selecao.item) {
                for (j = $scope.cadastro.registro.emailList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, $scope.pessoaEmailNvg.selecao.item.email.endereco)) {
                        //$scope.cadastro.registro.emailList.splice(j, 1);
                        $scope.cadastro.registro.emailList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaEmailNvg.selecao.item = null;
                $scope.pessoaEmailNvg.selecao.selecionado = false;
            } else if ($scope.pessoaEmailNvg.selecao.items && $scope.pessoaEmailNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.emailList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaEmailNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, $scope.pessoaEmailNvg.selecao.items[i].email.endereco)) {
                            //$scope.cadastro.registro.emailList.splice(j, 1);
                            $scope.cadastro.registro.emailList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaEmailNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaEmailNvg.selecao.items.splice(i, 1);
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