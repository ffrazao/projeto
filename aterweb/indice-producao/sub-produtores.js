/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv', 
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, IndiceProducaoSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.navegador.selecao.item)) {
            $scope.navegador.selecao.item = {};
        }
        if (!angular.isObject($scope.navegador.selecao.item[7])) {
            $scope.navegador.selecao.item[7] = [];
        }
        $scope.produtoresNvg = new FrzNavegadorParams($scope.navegador.selecao.item[7], 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio

    $scope.modalSelecinarPropriedadeRural = function (size) {
        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'pessoa/pessoa-modal.html',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function() {
                    var cadastro = $scope.cadastroBase();
                    cadastro.filtro.unidadeOrganizacionalList = [$scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_ID]];
                    cadastro.apoio.unidadeOrganizacionalSomenteLeitura = true;
                    return cadastro;
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            var reg = null, pessoa = null;
            $scope.cadastro.apoio.publicoAlvoList = [];
            if (resultado.selecao.tipo === 'U') {
                $scope.cadastro.apoio.publicoAlvoList.push({id: resultado.selecao.item[10]});
            } else {
                for (var i in resultado.selecao.items) {
                    $scope.cadastro.apoio.publicoAlvoList.push({id: resultado.selecao.items[i][10]});
                }
            }
            if (!$scope.cadastro.apoio.publicoAlvoList.length) {
                toastr.error('Nenhum registro selecionado');
            } else {
                IndiceProducaoSrv.filtrarPropriedadeRuralPorPublicoAlvo(
                    {  
                        empresaList: resultado.cadastro.filtro.empresaList,
                        unidadeOrganizacionalList: resultado.cadastro.filtro.unidadeOrganizacionalList,
                        comunidadeList: resultado.cadastro.filtro.comunidadeList,
                        publicoAlvoList: $scope.cadastro.apoio.publicoAlvoList,
                    }).success(function(resposta) {
                    if (resposta.mensagem === 'OK') {
                        // ano: $scope.navegador.selecao.item[$scope.PRODUCAO_ANO],
                        // bem: {id: $scope.navegador.selecao.item[$scope.PRODUCAO_BEM_ID]},
                        if (resposta.resultado && resposta.resultado.length) {
                            var i, j, k;
                            // percorrer todas as propriedades
                            for (i in resposta.resultado) {
                                // percorrer o publico alvo
                                for (j in $scope.cadastro.apoio.publicoAlvoList) {
                                    for (k in resposta.resultado[i][6]) {
                                        if (resposta.resultado[i][6][k][1] === $scope.cadastro.apoio.publicoAlvoList[j].id) {
                                            editarItem($scope.navegador.selecao.item[7], resposta.resultado[i]);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }).error(function(erro){
                    toastr.error(erro, 'Erro ao selecionar propriedades');
                });
            }
        }, function () {
            // processar o retorno negativo da modal
            
        });
    };

    var jaCadastrado = function(conteudo, modalInstance) {
        var acao = null;
        if (!conteudo.id) {
            acao = IndiceProducaoSrv.incluir(conteudo);
        } else {
            acao = IndiceProducaoSrv.editar(conteudo);
        }
        acao.success(function(resposta) {
            if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                //console.log(resposta, conteudo);
                modalInstance.close(resposta.resultado);
            } else {
                toastr.error(resposta.mensagem, 'Erro ao salvar');
            }
        }).error(function(erro){
            toastr.error(erro, 'Erro ao salvar');
        });
        return false;
    };
    var editarItem = function (destino, item) {
        if (item.id) {
            IndiceProducaoSrv.visualizar(item.id).success(function(resposta) {
                if (resposta.mensagem && resposta.mensagem === 'OK') {
                    exibirItem(destino, resposta.resultado);
                } else {
                    toastr.error(resposta.mensagem, 'Erro ao editar');
                }
            }).error(function(erro){
                toastr.error(erro, 'Erro ao editar');
            });
        } else {
            exibirItem(destino, item);
        }
    };
    var exibirItem = function (destino, item) {
        //console.log($scope.navegador.selecao.item);
        var form = 

        '<div class="modal-body">' +
        '    <div class="container-fluid">' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Ano</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            $scope.navegador.selecao.item[1] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Local</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            $scope.navegador.selecao.item[2] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Classificação</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            $scope.navegador.selecao.item[3] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Bem de Produção</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            $scope.navegador.selecao.item[4] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Produtor</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
        '                   <a ng-click="modalVerPessoa(' + item.publicoAlvo.pessoa.id + ')">' +
                                item.publicoAlvo.pessoa.nome +
        '                   </a>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Propriedade</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
        '                   <a ng-click="modalVerPropriedadeRural(' + item.propriedadeRural.id + ')">' +
                                item.propriedadeRural.nome +
        '                   </a>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-12">' +
        '                <label class="form-label">Forma de Produção</label>' +
        '                <ng-include src="\'indice-producao/sub-produtores-producao.html\'" ng-controller="ProdutoresProducaoCtrl"></ng-include>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>';

        item.principal = $scope.navegador.selecao.item;
        item.formula = $scope.formula;
        if (!item.producaoFormaList) {
            item.producaoFormaList = [];
        }

        mensagemSrv.confirmacao(false, form, 'Produção do Produtor', item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal

            IndiceProducaoSrv.filtrar({id: conteudo}).success(function(resposta) {
                for (var i in $scope.navegador.selecao.item[7]) {
                    if ($scope.navegador.selecao.item[7][i][3] === resposta.resultado[0][3]) {
                        angular.copy(resposta.resultado[0], $scope.navegador.selecao.item[7][i]);
                        return;
                    }
                }
                $scope.navegador.selecao.item[7].push(resposta.resultado[0]);
            }).error(function(erro){
                toastr.error(erro, 'Erro ao salvar');
            });

        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    $scope.UtilSrv = UtilSrv;

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.produtoresNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        $scope.modalSelecinarPropriedadeRural();
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.produtoresNvg.selecao.tipo === 'U' && $scope.produtoresNvg.selecao.item) {
            item = angular.copy($scope.produtoresNvg.selecao.item);
            editarItem($scope.produtoresNvg.selecao.item, {id: item[3]});
        } else if ($scope.produtoresNvg.selecao.items && $scope.produtoresNvg.selecao.items.length) {
            for (i in $scope.produtoresNvg.selecao.items) {
                for (j in $scope.navegador.selecao.item[7]) {
                    if (angular.equals($scope.produtoresNvg.selecao.items[i], $scope.navegador.selecao.item[7][j])) {
                        item = angular.copy($scope.navegador.selecao.item[7][j]);
                        editarItem($scope.navegador.selecao.item[7][j], {id: item[3]});
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.produtoresNvg.selecao.tipo === 'U' && $scope.produtoresNvg.selecao.item) {
                for (j = $scope.navegador.selecao.item[7].length -1; j >= 0; j--) {
                    if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, $scope.produtoresNvg.selecao.item.email.endereco)) {
                        //$scope.navegador.selecao.item[7].splice(j, 1);
                        $scope.navegador.selecao.item[7][j].cadastroAcao = 'E';
                    }
                }
                $scope.produtoresNvg.selecao.item = null;
                $scope.produtoresNvg.selecao.selecionado = false;
            } else if ($scope.produtoresNvg.selecao.items && $scope.produtoresNvg.selecao.items.length) {
                for (j = $scope.navegador.selecao.item[7].length-1; j >= 0; j--) {
                    for (i in $scope.produtoresNvg.selecao.items) {
                        if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, $scope.produtoresNvg.selecao.items[i].email.endereco)) {
                            //$scope.navegador.selecao.item[7].splice(j, 1);
                            $scope.navegador.selecao.item[7][j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.produtoresNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.produtoresNvg.selecao.items.splice(i, 1);
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

    // inicio dos watches
    $scope.$watch('navegador.selecao.item', function() {
        $scope.produtoresNvg.setDados($scope.navegador.selecao.item && $scope.navegador.selecao.item[7] ? $scope.navegador.selecao.item[7] : []);
    });
    // fim dos watches

} // fim função
]);

})('indiceProducao', 'ProdutoresCtrl', 'Produtores dos bens');