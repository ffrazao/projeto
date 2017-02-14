/* global criarEstadosPadrao, isUndefOrNull, removerCampo, StringMask:false, converterStringParaData */ /* jslint evil: true */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'FormularioSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, PropriedadeRuralSrv, FormularioSrv) {
    'ngInject';
    
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
            $scope.cadastro.apoio.tecnicoUnidadeList.push({username: resultado[i]['username'], nome: resultado[i]['propriedadeRural']['nome'],});
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
                nome: 'Usuário Inclusão',
                codigo: 'inclusaoUsuario',
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
                    //lista[item] = null;
                },
            },
            {
                nome: 'Usuário Alteração',
                codigo: 'alteracaoUsuario',
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
                    //lista[item] = null;
                },
            },
            {
                nome: 'Formulário',
                codigo: 'valor',
                tipo: 'objeto',
            },
        ],
        funcaoIncluirAntes: function(form, dd) {
            dd.formularioVersao = {id: $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item[9].id};
            dd.dataColeta = $scope.hoje();
            dd.finalizada = 'N';
            //dd.inclusaoUsuario = $scope.token;
            //dd.alteracaoUsuario = $scope.token;

            var id = $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item[9].versao;

            var f = this.opcao[5];
            if (!id || !versao) {
                toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                return;
            }
            FormularioSrv.visualizar(id).success(function (resposta) {
                if (resposta.mensagem === 'OK') {
                    var formulario = FormularioSrv.montar($scope, resposta.resultado, versao);
                    f.opcao = formulario.opcao;
                }
            });
        },
        funcaoEditarAntes: function(form, dd) {
            dd.formularioVersao = {id: $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item[9].id};

            if (dd.valorString && !dd.valor) {
                var x = null;
                eval('x = ' + dd.valorString);
                // converter string para data
                x = converterStringParaData(x);
                //console.log(x);
                dd.valor = x;
            }

            var id = $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item[9].versao;
            var f = this.opcao[5];
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
        funcaoSalvarDepois: function(form, dd, acao) {
            dd.propriedadeRural = { id : $scope.cadastro.registro.id };
            removerCampo(dd, ['@jsonId']);
            FormularioSrv.coletar(dd).success(function(resposta) {
                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                } else {
                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao salvar diagnóstico');
                }
            }).error(function(erro) {
                toastr.error(erro, 'Erro ao salvar diagnóstico');
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
        $scope.cadastro.apoio.filtroFormulario.destino = ['PR'];
        $scope.cadastro.apoio.filtroFormulario.propriedadeRural = {
            'id': $scope.cadastro.registro['id'],
        };

        $scope.cadastro.apoio.filtroFormulario.subformulario = ['N'];
        PropriedadeRuralSrv.formularioFiltrarComColeta($scope.cadastro.apoio.filtroFormulario).success(function (resposta) {
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
                $scope.propriedadeRuralDiagnosticoCaptacaoNvg.setDados($scope.cadastro.registro.diagnosticoList);
            }
        });

    });

    //$scope.cadastro.registro.diagnosticoList = [{"data":"01/03/2010 14:32","nome":"asdf","versao":"23"}];

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.diagnosticoList)) {
            $scope.cadastro.registro.diagnosticoList = [];
        }
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg = new FrzNavegadorParams($scope.cadastro.registro.diagnosticoList, 4);
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
        mensagemSrv.confirmacao(true, 'propriedade-rural-diagnostico-captacao-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
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
                    $scope.propriedadeRuralDiagnosticoCaptacaoNvg.setDados($scope.cadastro.registro.diagnosticoList);
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
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg.mudarEstado('ESPECIAL');
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg.estados()['ESPECIAL'].botoes.push('filtro');
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg.botao('filtro').exibir = function() {return true;};
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg.botao('inclusao').exibir = function() {return false;};
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg.botao('edicao').exibir = function() {return false;};
        $scope.propriedadeRuralDiagnosticoCaptacaoNvg.botao('exclusao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        var item = {diagnosticoCaptacao: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.tipo === 'U' && $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item) {
            item = angular.copy($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item);
            editarItem($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item, item);
        } else if ($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items && $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items.length) {
            for (i in $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.diagnosticoList) {
                    if (angular.equals($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items[i], $scope.cadastro.registro.diagnosticoList[j])) {
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
            if ($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.tipo === 'U' && $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.diagnosticoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.diagnosticoList[j].diagnosticoCaptacao.endereco, $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item.diagnosticoCaptacao.endereco)) {
                        //$scope.cadastro.registro.diagnosticoList.splice(j, 1);
                        $scope.cadastro.registro.diagnosticoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.item = null;
                $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.selecionado = false;
            } else if ($scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items && $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.diagnosticoList.length-1; j >= 0; j--) {
                    for (i in $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.diagnosticoList[j].diagnosticoCaptacao.endereco, $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items[i].diagnosticoCaptacao.endereco)) {
                            //$scope.cadastro.registro.diagnosticoList.splice(j, 1);
                            $scope.cadastro.registro.diagnosticoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.propriedadeRuralDiagnosticoCaptacaoNvg.selecao.items.splice(i, 1);
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

})('propriedadeRural', 'PropriedadeRuralDiagnosticoCaptacaoCtrl', 'Captação de diagnósticos da propriedade rural');