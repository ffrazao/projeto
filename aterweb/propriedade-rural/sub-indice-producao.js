
(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'BemClassificacaoSrv', 'IndiceProducaoSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, BemClassificacaoSrv, IndiceProducaoSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.indiceProducaoList)) {
            $scope.cadastro.registro.indiceProducaoList = [];
        }
        $scope.indiceProducaoNvg = new FrzNavegadorParams($scope.cadastro.registro.indiceProducaoList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.indiceProducaoNvg.mudarEstado('ESPECIAL');
        $scope.indiceProducaoNvg.estados()['ESPECIAL'].botoes.push('filtro');
        $scope.indiceProducaoNvg.botao('filtro').exibir = function() {return true;};
    };

    $scope.incluir = function() {
        var item = {};
        mensagemSrv.confirmacao(true, "indice-producao/form.html", null, item, null, null).then(function (conteudo) {

        });
    };
    $scope.editar = function() {
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.indiceProducaoNvg.selecao.tipo === 'U' && $scope.indiceProducaoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.indiceProducaoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.indiceProducaoList[j], $scope.indiceProducaoNvg.selecao.item)) {
                        $scope.marcarParaExclusao($scope, 'producaoFormaList', j);
                        //$scope.cadastro.registro.indiceProducaoList.splice(j, 1);
                        //$scope.cadastro.registro.indiceProducaoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.indiceProducaoNvg.selecao.item = null;
                $scope.indiceProducaoNvg.selecao.selecionado = false;
            } else if ($scope.indiceProducaoNvg.selecao.items && $scope.indiceProducaoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.indiceProducaoList.length-1; j >= 0; j--) {
                    for (i in $scope.indiceProducaoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.indiceProducaoList[j], $scope.indiceProducaoNvg.selecao.items[i])) {
                            $scope.marcarParaExclusao($scope, 'producaoFormaList', j);
                            //$scope.cadastro.registro.indiceProducaoList.splice(j, 1);
                            //$scope.cadastro.registro.indiceProducaoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.indiceProducaoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.indiceProducaoNvg.selecao.items.splice(i, 1);
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
    $scope.filtrar = function() {
        var item = {
            cadastro: {
                registro: {}, 
                filtro: {}, 
                lista: [], 
                original: {}, 
                apoio: [],
            }
        };

        item.cadastro.apoio.ano = new Date().getFullYear();
        item.cadastro.filtro.ano = item.cadastro.apoio.ano;
        for (var ano = item.cadastro.filtro.ano + 1; ano > item.cadastro.filtro.ano - 20; ano--) {
            if (!item.cadastro.apoio.anoList) {
                item.cadastro.apoio.anoList = [];
            }
            item.cadastro.apoio.anoList.push(ano);
        }
        var identificaPai = function(lista, pai) {
            for (var i in lista) {
                lista[i][lista[i].length] = pai;
                if (lista[i][3] && lista[i][3].length) {
                    identificaPai(lista[i][3], lista[i]);
                }
            }
        };
        BemClassificacaoSrv.filtrar({}).success(function(resposta) {
            item.cadastro.apoio.bemClassificacaoList = resposta.resultado;
            identificaPai(item.cadastro.apoio.bemClassificacaoList, null);
            //console.log(scp.cadastro.apoio.bemClassificacaoList);
        });

        item.toggleChildren = function (scope) {
            scope.toggle();
        };
        item.visivel = function (filtro, no, folha) {
            if (!folha) {
                return true;
            }
            return !(filtro && 
                filtro.length > 0 && 
                no.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1);
        };
        item.getTagBem = function($query) {
            var carregarClassificacao = function(a, r) {
                if (r) {
                    a.push(r.nome);
                    if (r.bemClassificacao) {
                        carregarClassificacao(a, r.bemClassificacao);
                    }
                }
            };
            var montarClassificacao = function(a) {
                var result = null;
                for (var i = a.length -1; i>=0; i--) {
                    if (result != null) {
                        result += '/';
                    } else {
                        result = '';
                    }
                    result += a[i];
                }
                return result;
            };
            return IndiceProducaoSrv.tagBem($query).then(function(response) { 
                var retorno = {data: []};
                var classificacao;
                for (var i in response.data.resultado) {
                    classificacao = [];
                    carregarClassificacao(classificacao, response.data.resultado[i][2]);
                    retorno.data.push({id: response.data.resultado[i][0], nome: response.data.resultado[i][1], classificacao: montarClassificacao(classificacao)});
                }
                return retorno;
            });
        };

        mensagemSrv.confirmacao(true, "propriedade-rural/sub-indice-producao-filtro.html", null, item, null, null).then(function (conteudo) {
            var captarBemClassificacaoList = function(lista, resultado) {
                if (lista) {
                    for (var i in lista) {
                        if (lista[i][4] === true) {
                            resultado.push({id: lista[i][0]});
                        } else {
                            captarBemClassificacaoList(lista[i][3], resultado);
                        }
                    }
                }
            };
            conteudo.cadastro.filtro.bemClassificacaoList = [];
            captarBemClassificacaoList(conteudo.cadastro.apoio.bemClassificacaoList, conteudo.cadastro.filtro.bemClassificacaoList);

            var captarFormaProducaoValorList = function(lista, resultado) {
                if (lista) {
                    var i, j;
                    for (i in lista) {
                        captarFormaProducaoValorList(lista[i][3], resultado);
                        if (lista[i][2]) {
                            for (j in lista[i][2]) {
                                if (lista[i][2][j][3] && lista[i][2][j][3] !== null) {
                                    resultado.push({id : lista[i][2][j][3]});
                                    lista[i][2][j][3] = null;
                                }
                            }
                        }
                    }
                }
            };
            conteudo.cadastro.filtro.formaProducaoValorList = [];
            captarFormaProducaoValorList(conteudo.cadastro.apoio.bemClassificacaoList, conteudo.cadastro.filtro.formaProducaoValorList);
            IndiceProducaoSrv.filtrar(conteudo.cadastro.filtro).success(function(resposta) {
                if (resposta.mensagem === 'OK') {
                    $scope.cadastro.registro.indiceProducaoList = resposta.resultado;
                    init();
                }
            }).error(function(erro){
                toastr.error(erro, 'Erro ao filtrar');
            });
        });

    };
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

    // inicio dos watches

    // fim dos watches

} // fim função
]);

})('propriedadeRural', 'PropriedadeRuralIndiceProducaoCtrl', 'Medição do Índice de Produção da Propriedade');