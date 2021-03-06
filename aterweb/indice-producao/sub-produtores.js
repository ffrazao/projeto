/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv', '$rootScope',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, IndiceProducaoSrv, $rootScope) {
    'ngInject';

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
        if (!item.producaoList) {
            item.producaoList = [];
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
                        // bemClassificado: {id: $scope.navegador.selecao.item[$scope.PRODUCAO_BEM_ID]},
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

    $scope.UtilSrv = UtilSrv;

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.produtoresNvg.mudarEstado('ESPECIAL');

        $scope.produtoresNvg.botao('acao', 'acao')['subFuncoes'] = [
        {
            nome: 'Inserir seleção de produtores',
            descricao: 'Permite cadastrar diversos produtores de uma vez numa produção',
            acao: function() {
                $scope.modalSelecinarPropriedadeRural();
            },
            exibir: function() {
                return false;
            },
        },];

    };
    $scope.incluir = function() {
        $rootScope.incluir($scope, {id: $scope.navegador.selecao.item[$scope.PRODUCAO_ID]});
    };
    $scope.incluirDepois = function(registro) {
        $scope.cadastro.apoio.bemClassificacao = $scope.encontraBemClassificacao(registro.bemClassificado.bemClassificacao.id);
        $scope.cadastro.apoio.producaoUnidadeOrganizacional = false;
        $scope.cadastro.apoio.unidadeOrganizacional = {id: $scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_ID], nome: $scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_NOME], sigla: $scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_SIGLA]};
        $scope.cadastro.apoio.porProdutor = true;
    };
    $scope.editar = function() {

        var id = $scope.produtoresNvg.selecao.item[0];

        $scope.servico.visualizar(id).success(function(resposta) {
            if (resposta.mensagem && resposta.mensagem === 'OK') {
                $scope.navegador.mudarEstado('VISUALIZANDO');
                $scope.crudVaiPara($scope, $scope.stt, 'form',  id);
                // ajustar o registro
                $scope.cadastro.apoio.bemClassificacao = $scope.encontraBemClassificacao(resposta.resultado.bemClassificado.bemClassificacao.id);
                $scope.cadastro.apoio.producaoUnidadeOrganizacional = false;
                $scope.cadastro.apoio.unidadeOrganizacional = {id: $scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_ID], nome: $scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_NOME], sigla: $scope.navegador.selecao.item[$scope.PRODUCAO_UNID_ORG_SIGLA]};
                $scope.cadastro.apoio.porProdutor = true;
                
                $scope.cadastro.original = angular.copy(resposta.resultado);
                $scope.cadastro.registro = angular.copy($scope.cadastro.original);
                $scope.crudVerRegistro($scope);
            } else {
                toastr.error(resposta.mensagem, 'Erro ao visualizar');
            }
        }).error(function(erro){
            toastr.error(erro, 'Erro ao visualizar');
        });
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i;
            if ($scope.produtoresNvg.selecao.tipo === 'U' && $scope.produtoresNvg.selecao.item) {
                $scope.servico.excluir({
                    id: $scope.produtoresNvg.selecao.item[0]
                }).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        $scope.produtoresNvg.dados.splice(UtilSrv.indiceDe($scope.produtoresNvg.dados, $scope.produtoresNvg.selecao.item), 1);
                        $scope.produtoresNvg.selecao.item = null;
                        $scope.produtoresNvg.selecao.selecionado = false;
                        toastr.info('Operação realizada!', 'Informação');
                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao excluir');
                    }
                });
            } else if ($scope.produtoresNvg.selecao.items && $scope.produtoresNvg.selecao.items.length) {
/*                for (i = $scope.produtoresNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.servico.excluir({
                        id: $scope.produtoresNvg.selecao.items[i][0]
                    }).success(function(resposta) {
                        if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                            $scope.produtoresNvg.dados.splice(UtilSrv.indiceDe($scope.produtoresNvg.dados, $scope.produtoresNvg.selecao.items[i][0]), 1);
                        } else {
                            toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao excluir');
                        }
                    });
                }
                toastr.info('Operação realizada!', 'Informação');*/
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