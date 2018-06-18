/* global StringMask:false, converterStringParaData, dataToInputData */ /* jslint evil: true */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'PessoaSrv', 'FormularioSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, PessoaSrv, FormularioSrv) {
    'ngInject';

    $scope.cadastro.apoio.tecnicoUnidadeList = [
        {username: 'a', nome: 'Jose',},
        {username: 'b', nome: 'Maria',},
    ]; 

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
                escondeLista: 'S',
            },
            
        ],
        funcaoIncluirAntes: function(form, dd) {

            var i, coleta ={};
            var ultimaColeta = { id: null, dataColeta: "01/01/1800"} ; 
            for (i in $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].coletaList ){
                coleta = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].coletaList[i];
                if( coleta.finalizada === 'N' ){
                    toastr.warning('Há coleta não finalizada! Finalize primeiro essa coleta!', 'Não pode crirar nova coleta.');
                    return false;
                }
                if( dataToInputData(ultimaColeta.dataColeta) < dataToInputData(coleta.dataColeta) ){
                    ultimaColeta =  angular.copy(coleta);
                }
            }

            var id = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].versao;
            if (!id || !versao) {
                toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                return { valida : false, reg : {} };
            }

            dd = angular.copy(ultimaColeta);
            dd.dataColeta = $scope.hoje();
            dd.finalizada = 'N';
            dd.inclusaoUsuario = $scope.token;
            dd.alteracaoUsuario = $scope.token;

            if( ultimaColeta.id === null ){                
                dd.formularioVersao = {id: $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].id};
            } else { 
                dd.id = null;
            }

            var f = this.opcao[5];
            FormularioSrv.visualizar(id).success(function (resposta) {
                if (resposta.mensagem === 'OK') {
                    var formulario = FormularioSrv.montar($scope, resposta.resultado.formulario, versao);
                    f.opcao = formulario.opcao;
                }
            });
            return { valida : true, reg : dd };

        },
        funcaoEditarAntes: function(form, dd) {
                
            dd.formularioVersao = {id: $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].id};

            if (dd.valorString && !dd.valor) {
                var x = null;
                eval('x = ' + dd.valorString);
                // converter string para data
                x = converterStringParaData(x);
                //console.log(x);
                dd.valor = x;
            }

            var id = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].versao;
            var f = this.opcao[5];
            if (!id || !versao) {
                toastr.warning('Não foi possível identificar o formulário', 'Identificar formulário');
                return;
            }
            FormularioSrv.visualizar(id).success(function (resposta) {
                if (resposta.mensagem === 'OK') {
                    var formulario = FormularioSrv.montar($scope, resposta.resultado.formulario, versao);
                    f.opcao = formulario.opcao;
                }
            });
            if( dd.finalizada !== "N" ){
                toastr.warning('Coleta já finalizada, não é permitido alterar. Só é possível visualizar!', 'Erro ao Editar');
                return false;
            }
        },
        funcaoExcluirAntes: function(form, dd) {
            if( dd.finalizada === "S" ){
                toastr.warning('Coleta já finalizada, não é permitido alterar. Faça uma nova outra coleta!', 'Erro ao Escluir');
                return false;
            } else {
                return true;
            }
        },
        funcaoExibirAntes: function(form, dd) {
            var id = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[0];
            var versao = $scope.pessoaDiagnosticoCaptacaoNvg.selecao.item[9].versao;
            var f = this.opcao[5];
            if (!id || !versao) {
                toastr.warning('Não foi possível identificar o formulário', 'Identificar formulário');
                return;
            }
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
        if (!$scope.pessoaDiagnosticoCaptacaoNvg) {
            $scope.pessoaDiagnosticoCaptacaoNvg = new FrzNavegadorParams($scope.cadastro.registro.diagnosticoList, 4);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.diagnosticoList) {
            if (angular.equals($scope.cadastro.registro.diagnosticoList[j].diagnosticoCaptacao.endereco, conteudo.diagnosticoCaptacao.endereco)) {
                if ($scope.cadastro.registro.diagnosticoList[j].cadastroAcao === 'E') {
                    return true;
                } else {
                    toastr.warning('Registro já cadastrado');
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


    //inicio dos watches
        $scope.verObjeto = function(form, dd) {
            console.log("intercepta");
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

