
(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'BemClassificacaoSrv', 'IndiceProducaoSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, BemClassificacaoSrv, IndiceProducaoSrv, $log) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.publicoAlvo.indiceProducaoList)) {
            $scope.cadastro.registro.publicoAlvo.indiceProducaoList = [];
        }
        $scope.indiceProducaoNvg = new FrzNavegadorParams($scope.cadastro.registro.publicoAlvo.indiceProducaoList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.indiceProducaoNvg.mudarEstado('ESPECIAL');
        $scope.indiceProducaoNvg.estados()['ESPECIAL'].botoes.push('filtro');
        $scope.indiceProducaoNvg.botao('filtro').exibir = function() {return true;};
        $scope.indiceProducaoNvg.botao('primeiro').exibir = function() {return false;};
        $scope.indiceProducaoNvg.botao('anterior').exibir = function() {return false;};
        $scope.indiceProducaoNvg.botao('tamanhoPagina').exibir = function() {return false;};
        $scope.indiceProducaoNvg.botao('proximo').exibir = function() {return false;};
        $scope.indiceProducaoNvg.botao('ultimo').exibir = function() {return false;};
    };
    var pegaComposicaoId = function (formaProducao) {
        if (formaProducao === null) {
            return null;
        }
        var result = [];
        for (var i in formaProducao.producaoFormaComposicaoList) {
            result.push(formaProducao.producaoFormaComposicaoList[i].formaProducaoValor.id);
        }
        // ordenar
        result.sort(function (a, b) {
            return a - b;
        });
        return result;
    };
    var jaCadastrado = function(conteudo) {
        var composicao = pegaComposicaoId(conteudo);
        var j, igual;
        for (j in $scope.cadastro.registro.producaoFormaList) {
            igual = angular.equals(composicao, pegaComposicaoId($scope.cadastro.registro.producaoFormaList[j]));
            if (igual) {
                if (conteudo.id !== $scope.cadastro.registro.producaoFormaList[j].id) {
                    toastr.error('Registro já cadastrado');
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    };
    var editarItem = function (destino, item, selecaoId) {

        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            template: '<ng-include src=\"\'indice-producao/indice-producao-propriedade-form-modal.html\'\"></ng-include>',
            controller: 'IndiceProducaoCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function () {
                    var cad = $scope.cadastroBase();
                    cad.apoio.producaoUnidadeOrganizacional = false;
                    cad.apoio.estadoInicial = 'form';
                    cad.registro.id = destino ? destino[0] : null;
                    cad.registro.publicoAlvo = {'id': $scope.cadastro.registro.publicoAlvo.id, 'pessoa': {'nome': $scope.cadastro.registro.nome, 'pessoaTipo': $scope.cadastro.registro.pessoaTipo}};
                    $scope.preparaClassePessoa(cad.registro.publicoAlvo.pessoa);
                    cad.apoio.porProdutor = true;

                    return cad;
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            var pessoa = null;
            if (resultado.selecao.tipo === 'U') {
                pessoa = {
                    id: resultado.selecao.item[0], 
                    nome: resultado.selecao.item[1], 
                    pessoaTipo: resultado.selecao.item[3],
                };
                $scope.preparaClassePessoa(pessoa);
                $scope.cadastro.registro.publicoAlvo = 
                    {
                        id: resultado.selecao.item[10], 
                        pessoa: pessoa,
                    };
            } else {
                pessoa = {
                    id: resultado.selecao.items[0][0], 
                    nome: resultado.selecao.items[0][1], 
                    pessoaTipo: resultado.selecao.items[0][3],
                };
                $scope.preparaClassePessoa(pessoa);
                $scope.cadastro.registro.publicoAlvo = 
                    {
                        id: resultado.selecao.items[0][10], 
                        pessoa: pessoa,
                    };
            }
        }, function () {
            // processar o retorno negativo da modal
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.incluir = function() {
        editarItem();
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.indiceProducaoNvg.selecao.tipo === 'U' && $scope.indiceProducaoNvg.selecao.item) {
            item = angular.copy($scope.indiceProducaoNvg.selecao.item);
            editarItem($scope.indiceProducaoNvg.selecao.item, item);
        } else if ($scope.indiceProducaoNvg.selecao.items && $scope.indiceProducaoNvg.selecao.items.length) {
            for (i in $scope.indiceProducaoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.producaoFormaList) {
                    if (angular.equals($scope.indiceProducaoNvg.selecao.items[i], $scope.cadastro.registro.producaoFormaList[j])) {
                        item = angular.copy($scope.cadastro.registro.producaoFormaList[j]);
                        editarItem($scope.cadastro.registro.producaoFormaList[j], item, i);
                        break;
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.indiceProducaoNvg.selecao.tipo === 'U' && $scope.indiceProducaoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.publicoAlvo.indiceProducaoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.publicoAlvo.indiceProducaoList[j], $scope.indiceProducaoNvg.selecao.item)) {
                        $scope.marcarParaExclusao($scope, 'producaoFormaList', j);
                        //$scope.cadastro.registro.publicoAlvo.indiceProducaoList.splice(j, 1);
                        //$scope.cadastro.registro.publicoAlvo.indiceProducaoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.indiceProducaoNvg.selecao.item = null;
                $scope.indiceProducaoNvg.selecao.selecionado = false;
            } else if ($scope.indiceProducaoNvg.selecao.items && $scope.indiceProducaoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.publicoAlvo.indiceProducaoList.length-1; j >= 0; j--) {
                    for (i in $scope.indiceProducaoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.publicoAlvo.indiceProducaoList[j], $scope.indiceProducaoNvg.selecao.items[i])) {
                            $scope.marcarParaExclusao($scope, 'producaoFormaList', j);
                            //$scope.cadastro.registro.publicoAlvo.indiceProducaoList.splice(j, 1);
                            //$scope.cadastro.registro.publicoAlvo.indiceProducaoList[j].cadastroAcao = 'E';
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

        mensagemSrv.confirmacao(true, "pessoa/sub-publico-alvo-indice-producao-filtro.html", null, item, null, null).then(function (conteudo) {
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

            conteudo.cadastro.filtro.publicoAlvo = {id: $scope.cadastro.registro.publicoAlvo.id};
            //conteudo.cadastro.apoio.unidadeOrganizacional = $scope.cadastro.registro.comunidade.unidadeOrganizacional;


            IndiceProducaoSrv.filtrarProducaoPorPublicoAlvo(conteudo.cadastro.filtro).success(function(resposta) {
                if (resposta.mensagem === 'OK') {
                    $scope.cadastro.registro.publicoAlvo.indiceProducaoList = resposta.resultado;
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
    var idx = [];
    idx[0] = 0;
    $scope.PRODUCAO_ID = idx[0]++;
    $scope.PRODUCAO_ANO = idx[0]++;
    $scope.PRODUCAO_BEM_ID = idx[0]++;
    $scope.PRODUCAO_BEM_NOME = idx[0]++;
    $scope.PRODUCAO_BEM_CLASSIFICACAO = idx[0]++;
    $scope.PRODUCAO_UNIDADE_MEDIDA = idx[0]++;
    $scope.PRODUCAO_FORMULA = idx[0]++;
    $scope.PRODUCAO_NOME_ITEM_A = idx[0]++;
    $scope.PRODUCAO_NOME_ITEM_B = idx[0]++;
    $scope.PRODUCAO_NOME_ITEM_C = idx[0]++;
    $scope.PRODUCAO_UNID_ORG_ID = idx[0]++;
    $scope.PRODUCAO_UNID_ORG_NOME = idx[0]++;
    $scope.PRODUCAO_UNID_ORG_SIGLA = idx[0]++;
    $scope.PRODUCAO_PUBLICO_ALVO_ID = idx[0]++;
    $scope.PRODUCAO_PUBLICO_ALVO_PESSOA_ID = idx[0]++;
    $scope.PRODUCAO_PUBLICO_ALVO_NOME = idx[0]++;
    $scope.PRODUCAO_PROPRIEDADE_RURAL_ID = idx[0]++;
    $scope.PRODUCAO_PROPRIEDADE_RURAL_NOME = idx[0]++;
    $scope.PRODUCAO_COMUNIDADE_ID = idx[0]++;
    $scope.PRODUCAO_COMUNIDADE_NOME = idx[0]++;
    $scope.PRODUCAO_FORMA_LIST = idx[0]++;
        idx[1] = 0;
        $scope.FORMA_COMPOSICAO_LIST = idx[1]++;
            idx[2] = 0;
            $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_ID = idx[2]++;
            $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME = idx[2]++;
            $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME = idx[2]++;
            $scope.COMPOSICAO_ORDEM = idx[2]++;
        $scope.FORMA_VALOR_ITEM_A = idx[1]++;
        $scope.FORMA_VALOR_ITEM_B = idx[1]++;
        $scope.FORMA_VALOR_ITEM_C = idx[1]++;
        $scope.FORMA_VOLUME = idx[1]++;
        $scope.FORMA_VLR_UNIT = idx[1]++;
        $scope.FORMA_VLR_TOTAL = idx[1]++;
        $scope.FORMA_QTD_PRODUTORES = idx[1]++;
        $scope.FORMA_DATA_CONFIRMACAO = idx[1]++;
        $scope.FORMA_INCLUSAO_NOME = idx[1]++;
        $scope.FORMA_INCLUSAO_DATA = idx[1]++;
        $scope.FORMA_ALTERACAO_NOME = idx[1]++;
        $scope.FORMA_ALTERACAO_DATA = idx[1]++;
        $scope.FORMA_NOME_CALCULO = idx[1]++;

    $scope.PRODUCAO_INCLUSAO_NOME = idx[0]++;
    $scope.PRODUCAO_INCLUSAO_DATA = idx[0]++;
    $scope.PRODUCAO_ALTERACAO_NOME = idx[0]++;
    $scope.PRODUCAO_ALTERACAO_DATA = idx[0]++;
    $scope.PRODUCAO_PRODUTOR_LIST = idx[0]++;


} // fim função
]);

})('pessoa', 'PublicoAlvoIndiceProducaoCtrl', 'Indice de Producao vinculado ao publico alvo');