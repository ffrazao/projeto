/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv', 'PropriedadeRuralSrv',
        'TokenStorage',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, IndiceProducaoSrv,  PropriedadeRuralSrv,
            TokenStorage) {

            // inicializacao
            $scope.crudInit($scope, $state, modalCadastro, pNmFormulario, IndiceProducaoSrv);

            $scope.produtoresNvg = new FrzNavegadorParams([]);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, modalCadastro ? modalCadastro.apoio.estadoInicial : 'filtro', modalCadastro, pNmFormulario);
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
                        nome: 'Exibir/Esconder Detalhes',
                        descricao: 'Exibe ou esconde os detalhes da produção',
                        acao: function() {
                            if (!$scope.cadastro.apoio.escondeDetalhe) {
                                $scope.cadastro.apoio.escondeDetalhe = false;
                            }
                            $scope.cadastro.apoio.escondeDetalhe = !$scope.cadastro.apoio.escondeDetalhe;
                        },
                        exibir: function() {
                            var estado = $scope.navegador.estadoAtual();
                            return estado === 'LISTANDO';
                        },
                    },
                ];
                $rootScope.abrir(scp);
            };
            $scope.incluirDepois = function (objeto) {
                var t = TokenStorage.token();
                if (t && t.lotacaoAtual) {
                    objeto.unidadeOrganizacional = t.lotacaoAtual;
                }
                if (isUndefOrNull($scope.cadastro.apoio.producaoUnidadeOrganizacional)) {
                    $scope.cadastro.apoio.producaoUnidadeOrganizacional = true;
                }
                delete $scope.cadastro.apoio.unidadeOrganizacional;
                delete $scope.cadastro.apoio.porProdutor;
            };
            $scope.confirmarIncluir = function(scp) {
                if (!scp.confirmar(scp)) {
                    return;
                }
                var reg = angular.copy(scp.cadastro.registro);
                removerCampo(reg, ['@jsonId', 'formula', 'bemClassificacao']);

                // preparar composicao da forma de producao
                scp.servico.incluir(reg).success(function (resposta) {
                    if (resposta.mensagem && resposta.mensagem === 'OK') {
                        scp.navegador.voltar(scp);
                        scp.navegador.mudarEstado('VISUALIZANDO');
                        scp.crudVaiPara(scp, scp.stt, 'form');
                        scp.navegador.submetido = false;
                        scp.navegador.dados.push(scp.cadastro.registro);
                        if (scp.navegador.selecao.tipo === 'U') {
                            scp.navegador.selecao.item = scp.cadastro.registro;
                        } else {
                            scp.navegador.folhaAtual = scp.navegador.selecao.items.length;
                            scp.navegador.selecao.items.push(scp.cadastro.registro);
                        }
                        scp.navegador.refresh();

                        toastr.info('Operação realizada!', 'Informação');
                            
                    } else {
                        toastr.error(resposta.mensagem, 'Erro ao incluir');
                    }
                }).error(function (erro) {
                    toastr.error(erro, 'Erro ao incluir');
                });
            };
            $scope.encontraBemClassificacao = function (id, lista) {
                if (!lista) {
                    lista = $scope.cadastro.apoio.bemClassificacaoList;
                }
                var result;
                for (var i in lista) {
                    if (id === lista[i][0]) {
                        result = lista[i];
                    } else if (lista[i][3]) {
                        result = $scope.encontraBemClassificacao(id, lista[i][3]);
                    }
                    if (result) {
                        break;
                    }
                }
                return result;
            };
            $scope.visualizarDepois = function (registro) {
                $scope.cadastro.apoio.bemClassificacao = $scope.encontraBemClassificacao(registro.bem.bemClassificacao.id);
                $scope.cadastro.apoio.unidadeOrganizacional = angular.copy(registro.unidadeOrganizacional);
            };
            $scope.cadastro.apoio.producaoForma = {composicao: []};

            $scope.confirmarFiltrarAntes = function(filtro) {
                filtro.empresaList = [];
                filtro.unidadeOrganizacionalList = [];
                filtro.comunidadeList = [];
                var i, j, k;
                for (i in $scope.cadastro.apoio.localList) {
                    // filtrar as empressas
                    if ($scope.cadastro.apoio.localList[i].selecionado) {
                        filtro.empresaList.push({id: $scope.cadastro.apoio.localList[i].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica'});
                    } else {
                        for (j in $scope.cadastro.apoio.localList[i].unidadeList) {
                            // filtrar as unidades organizacionais
                            if ($scope.cadastro.apoio.localList[i].unidadeList[j].selecionado) {
                                filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id});
                            } else {
                                for (k in $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList) {
                                    // filtrar as unidades organizacionais
                                    if ($scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].selecionado) {
                                        filtro.comunidadeList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].id});
                                    }
                                }
                            }
                        }
                    }
                }                
                if ($scope.cadastro.apoio.unidadeOrganizacionalSomenteLeitura && !$scope.cadastro.filtro.unidadeOrganizacionalList.length && !$scope.cadastro.filtro.comunidadeList.length) {
                    toastr.error('Informe pelo menos uma comunidade', 'Erro ao filtrar');
                    throw 'Informe pelo menos uma comunidade';
                }

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
                filtro.bemClassificacaoList = [];
                captarBemClassificacaoList($scope.cadastro.apoio.bemClassificacaoList, filtro.bemClassificacaoList);

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
                filtro.formaProducaoValorList = [];
                captarFormaProducaoValorList($scope.cadastro.apoio.bemClassificacaoList, filtro.formaProducaoValorList);
            };


            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais

            // nomes dos campos para listagem
            $scope.PRODUCAO_ID = 0;
            $scope.PRODUCAO_ANO = 1;
            $scope.PRODUCAO_BEM_ID = 2;
            $scope.PRODUCAO_BEM_NOME = 3;
            $scope.PRODUCAO_BEM_CLASSIFICACAO = 4;
            $scope.PRODUCAO_UNIDADE_MEDIDA = 5;
            $scope.PRODUCAO_FORMULA = 6;
            $scope.PRODUCAO_NOME_ITEM_A = 7;
            $scope.PRODUCAO_NOME_ITEM_B = 8;
            $scope.PRODUCAO_NOME_ITEM_C = 9;
            $scope.PRODUCAO_UNID_ORG_ID = 10;
            $scope.PRODUCAO_UNID_ORG_NOME = 11;
            $scope.PRODUCAO_UNID_ORG_SIGLA = 12;
            $scope.PRODUCAO_PUBLICO_ALVO_ID = 13;
            $scope.PRODUCAO_PUBLICO_ALVO_NOME = 14;
            $scope.PRODUCAO_PROPRIEDADE_RURAL_ID = 15;
            $scope.PRODUCAO_PROPRIEDADE_RURAL_NOME = 16;
            $scope.PRODUCAO_COMUNIDADE_ID = 17;
            $scope.PRODUCAO_COMUNIDADE_NOME = 18;
            $scope.PRODUCAO_FORMA_LIST = 19;
                $scope.FORMA_COMPOSICAO_LIST = 0;
                    $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_ID = 0;
                    $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME = 1;
                    $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME = 2;
                    $scope.COMPOSICAO_ORDEM = 3;
                $scope.FORMA_VALOR_ITEM_A = 1;
                $scope.FORMA_VALOR_ITEM_B = 2;
                $scope.FORMA_VALOR_ITEM_C = 3;
                $scope.FORMA_VOLUME = 4;
                $scope.FORMA_VLR_UNIT = 5;
                $scope.FORMA_VLR_TOTAL = 6;
                $scope.FORMA_QTD_PRODUTORES = 7;
                $scope.FORMA_DATA_CONFIRMACAO = 8;
                $scope.FORMA_INCLUSAO_NOME = 9;
                $scope.FORMA_INCLUSAO_DATA = 10;
                $scope.FORMA_ALTERACAO_NOME = 11;
                $scope.FORMA_ALTERACAO_DATA = 12;
                $scope.FORMA_NOME_CALCULO = 13;

            $scope.PRODUCAO_INCLUSAO_NOME = 20;
            $scope.PRODUCAO_INCLUSAO_DATA = 21;
            $scope.PRODUCAO_ALTERACAO_NOME = 22;
            $scope.PRODUCAO_ALTERACAO_DATA = 23;
            $scope.PRODUCAO_PRODUTOR_LIST = 24;


            $scope.resultado = function(item, array, campo) {
                if (!item) {
                    return null;
                }
                switch (UtilSrv.indiceDePorCampo($scope.cadastro.apoio.quantitativoList, item, 'nome').resultado) {
                    case 'S': 
                        return $scope.soma(array, campo);
                    case 'M': 
                        return $scope.media(array, campo);
                    case 'N': 
                        return null;
                }
            };
            $scope.soma = function(array, campo) {
                if (!array || !campo) {
                    return null;
                }
                var result = 0;
                for (var i in array) {
                    result += array[i][campo];
                }
                return result;
            };
            $scope.media = function(array, campo) {
                return $scope.soma(array, campo) / (array && array.length ? array.length : 1);
            };
            $scope.formula = function(formula, itemA, itemB, itemC, array, indice) {
                if (!formula) {
                    return null;
                }
                formula = formula.replace(new RegExp('A', 'g'), itemA);
                formula = formula.replace(new RegExp('B', 'g'), itemB);
                formula = formula.replace(new RegExp('C', 'g'), itemC);
                var result = 0;
                eval('result = ' + formula);
                if (!result || isNaN(result)) {
                    result = 0;
                }
                if (array && indice) {
                    array[indice] = result;
                }
                return result;
            };
            $scope.toggleChildren = function (scope) {
                scope.toggle();
            };
            $scope.selecionou = function (item, selecao) {
                item.selecionado = selecao.selected;
            };

            $scope.visible = function (item) {
                return !($scope.cadastro.apoio.localFiltro && 
                    $scope.cadastro.apoio.localFiltro.length > 0 && 
                    item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.visivel = function (filtro, no, folha) {
                if (!folha) {
                    return true;
                }
                return !(filtro && 
                    filtro.length > 0 && 
                    no.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.getTagBem = function($query) {
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
            
            $scope.UtilSrv = UtilSrv;

            $scope.modalSelecinarPublicoAlvo = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            return $scope.cadastroBase();
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

            $scope.modalVerPublicoAlvo = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-form-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            var cadastro = {registro: angular.copy($scope.cadastro.registro.publicoAlvo.pessoa), filtro: {}, lista: [], original: {}, apoio: [],};
                            return cadastro;
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (cadastroModificado) {
                    // processar o retorno positivo da modal

                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalSelecinarPropriedadeRural = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            return $scope.cadastroBase();
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (resultado) {
                    // processar o retorno positivo da modal
                    if (resultado.selecao.tipo === 'U') {
                        $scope.cadastro.registro.propriedadeRural = 
                            {
                                id: resultado.selecao.item[0],
                                nome: resultado.selecao.item[1],
                            };
                    } else {
                        $scope.cadastro.registro.propriedadeRural = 
                            {
                                id: resultado.selecao.items[0][0],
                                nome: resultado.selecao.items[0][1],
                            };
                    }
                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalVerPropriedadeRural = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-form-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            var cadastro = {registro: angular.copy($scope.cadastro.registro.propriedadeRural), filtro: {}, lista: [], original: {}, apoio: [],};
                            return cadastro;
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (cadastroModificado) {
                    // processar o retorno positivo da modal

                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
            // fim ações especiais

            // inicio trabalho tab
            // fim trabalho tab

            // inicio dos watches
            var pai = function(array, item) {
                if (!array || !item) {
                    return;
                }
                array.push(item);
                if (item[9]) {
                    pai(array, item[9]);
                }
            };
            $scope.$watch('cadastro.apoio.bemClassificacao', function(novo) {
                //console.log($scope.cadastro.apoio.bemClassificacao);
                if (!novo) {
                    return;
                }
                UtilSrv.dominio({ent: [
                        'Bem',
                    ], 
                    npk: 'bemClassificacao.id', 
                    vpk: novo[0]}).success(function(resposta) {
                    if (resposta && resposta.mensagem === "OK") {
                        $scope.cadastro.apoio.bemList = resposta.resultado[0];
                    }
                });
                $scope.cadastro.apoio.producaoForma.bemClassificacao = '';
                $scope.cadastro.apoio.producaoForma.composicao = [];
                var array = [];
                pai(array, novo);
                if (array.length) {
                    array.reverse();
                    var unidadeMedida, itemANome, itemBNome, itemCNome, formula;
                    for (var i in array) {
                        //console.log(array[i][2]);
                        if (i > 0) {
                            $scope.cadastro.apoio.producaoForma.bemClassificacao += '/';
                        }
                        $scope.cadastro.apoio.producaoForma.bemClassificacao += array[i][1];
                        if (array[i][2]) {
                            for (var j in array[i][2]) {
                                $scope.cadastro.apoio.producaoForma.composicao.push(array[i][2][j]);
                            }
                        }
                        if (!unidadeMedida && array[i][4]) {
                            unidadeMedida = array[i][4];
                        }
                        if (!itemANome && array[i][5]) {
                            itemANome = array[i][5];
                        }
                        if (!itemBNome && array[i][6]) {
                            itemBNome = array[i][6];
                        }
                        if (!itemCNome && array[i][7]) {
                            itemCNome = array[i][7];
                        }
                        if (!formula && array[i][8]) {
                            formula = array[i][8];
                        }
                    }
                    // cadastrar a composicao
                    if (itemANome) {
                        $scope.cadastro.apoio.producaoForma.itemANome = itemANome;
                    }
                    if (itemBNome) {
                        $scope.cadastro.apoio.producaoForma.itemBNome = itemBNome;
                    }
                    if (itemCNome) {
                        $scope.cadastro.apoio.producaoForma.itemCNome = itemCNome;
                    }
                    $scope.cadastro.apoio.producaoForma.formula = formula;
                    $scope.cadastro.apoio.producaoForma.unidadeMedida = unidadeMedida;
                }
            });

            $scope.$watch('cadastro.registro.publicoAlvo.id', function(novo) {
                if (!$scope.cadastro.apoio.unidadeOrganizacional) {
                    return;
                }
                PropriedadeRuralSrv.filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade({
                    unidadeOrganizacionalList: [{id: $scope.cadastro.apoio.unidadeOrganizacional.id}],
                    publicoAlvoList: [{id: $scope.cadastro.registro.publicoAlvo.id}],
                }).success(function (resposta) {
                    if (resposta && resposta.mensagem === "OK") {
                        $scope.cadastro.apoio.propriedadeRuralList = [];
                        for (var i in resposta.resultado) {
                            $scope.cadastro.apoio.propriedadeRuralList.push({id: resposta.resultado[i][0], nome: resposta.resultado[i][1]});
                        }                        
                        if ($scope.cadastro.apoio.propriedadeRuralList.length === 1) {
                            $scope.cadastro.registro.propriedadeRural = $scope.cadastro.apoio.propriedadeRuralList[0];
                        }
                    }
                });
            });

            $scope.$watch('cadastro.registro.propriedadeRural.id', function(novo) {
                if (!$scope.cadastro.apoio.unidadeOrganizacional) {
                    return;
                }
                PropriedadeRuralSrv.filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade({
                    unidadeOrganizacionalList: [{id: $scope.cadastro.apoio.unidadeOrganizacional.id}],
                    propriedadeRuralList: [{id: $scope.cadastro.registro.propriedadeRural.id}],
                }).success(function (resposta) {
                    if (resposta && resposta.mensagem === "OK") {
                        $scope.cadastro.apoio.publicoAlvoList = [];
                        for (var i in resposta.resultado) {
                            var pessoa = {id: resposta.resultado[i][0], pessoa: {nome: resposta.resultado[i][1], pessoaTipo: resposta.resultado[i][2]}};
                            $scope.preparaClassePessoa(pessoa);
                            $scope.cadastro.apoio.publicoAlvoList.push(pessoa);
                        }                        
                        if ($scope.cadastro.apoio.publicoAlvoList.length === 1) {
                            $scope.cadastro.registro.publicoAlvo = $scope.cadastro.apoio.publicoAlvoList[0];
                        }
                    }
                });
            });

            $scope.$watch('cadastro.apoio.unidadeOrganizacional.id', function(novo) {
                //console.log($scope.cadastro.apoio.unidadeOrganizacional);
                if (novo) {
                    UtilSrv.dominio({ent: [
                           'UnidadeOrganizacionalComunidade',
                        ], 
                        npk: 'unidadeOrganizacional.id', 
                        vpk: novo, 
                        fetchs: 'comunidade'}).success(function(resposta) {
                        if (resposta && resposta.mensagem === "OK") {
                            if (!$scope.cadastro.apoio.comunidadeList) {
                                $scope.cadastro.apoio.comunidadeList = [];
                            } else {
                                $scope.cadastro.apoio.comunidadeList.length = 0;
                            }
                            for (var i in resposta.resultado[0]) {
                                $scope.cadastro.apoio.comunidadeList.push(resposta.resultado[0][i].comunidade);
                            }
                            removerCampo($scope.cadastro.apoio.comunidadeList, ['unidadeOrganizacional']);
                        }
                    });
                }
            });
            // fim dos watches

            // quebra galho para funcionar a inclusao externa
            if (modalCadastro) {
                $rootScope.abrir($scope);
                $scope.modalCadastro = true;
            }
        }
    ]);
})('indiceProducao', 'IndiceProducaoCtrl', 'Cadastro de Índices de Produção', 'indice-producao');