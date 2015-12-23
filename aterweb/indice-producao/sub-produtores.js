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
                    cadastro.filtro.comunidade = {id: $scope.navegador.selecao.item[12]};
                    cadastro.apoio.comunidadeSomenteLeitura = true;
                    return cadastro;
                }
            }
        });
        // processar retorno da modal
        var criarPessoa = function (id, nome, tipo) {
            var pessoa = {
                id: id, 
                nome: nome,
                pessoaTipo: tipo,
            };
            $scope.preparaClassePessoa(pessoa);
            var reg = {pessoa: pessoa};
            return reg;
        };
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            var reg = null, pessoa = null;
            $scope.cadastro.apoio.publicoAlvoList = [];
            if (resultado.tipo === 'U') {
                $scope.cadastro.apoio.publicoAlvoList.push(criarPessoa(resultado.item[0], resultado.item[1], resultado.item[3]));
            } else {
                for (var i in resultado.items) {
                    $scope.cadastro.apoio.publicoAlvoList.push(criarPessoa(resultado.item[i][0], resultado.item[i][1], resultado.item[i][3]));
                }
            }
            if (!$scope.cadastro.apoio.publicoAlvoList.length) {
                toastr.error('Nenhum registro selecionado');
            } else {
                IndiceProducaoSrv.filtrarPorPublicoAlvoComunidade(
                    {
                        comunidade: {id: $scope.navegador.selecao.item[12]},
                        publicoAlvoList: $scope.cadastro.apoio.publicoAlvoList,
                    }).success(function(resposta) {
                    if (resposta.mensagem === 'OK') {
                        if (!resposta.resultado || !resposta.resultado.length) {
                            console.log(resposta.resultado);
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

    $scope.modalVerPropriedadeRural = function (id) {
        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            template: '<ng-include src=\"\'pessoa/pessoa-form-modal.html\'\"></ng-include>',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function () {
                    var cadastro = {registro: {id: id}, filtro: {}, lista: [], original: {}, apoio: [],};
                    return cadastro;
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (cadastroModificado) {
            // processar o retorno positivo da modal

        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };


    var jaCadastrado = function(conteudo) {
        for (var j in $scope.navegador.selecao.item[7]) {
            if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, conteudo.email.endereco)) {
                if ($scope.navegador.selecao.item[7][j].cadastroAcao === 'E') {
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
                            '' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Propriedade</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            '' +
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

        mensagemSrv.confirmacao(false, form, 'Produção', item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.email.endereco = angular.copy(conteudo.email.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.navegador.selecao.item[7]) {
                    $scope.navegador.selecao.item[7] = [];
                    $scope.produtoresNvg.setDados($scope.navegador.selecao.item[7]);
                }
                $scope.navegador.selecao.item[7].push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.produtoresNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        //var item = {email: {endereco: null}};
        //editarItem(null, item);
        $scope.modalSelecinarPropriedadeRural();
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.produtoresNvg.selecao.tipo === 'U' && $scope.produtoresNvg.selecao.item) {
            item = angular.copy($scope.produtoresNvg.selecao.item);
            editarItem($scope.produtoresNvg.selecao.item, item);
        } else if ($scope.produtoresNvg.selecao.items && $scope.produtoresNvg.selecao.items.length) {
            for (i in $scope.produtoresNvg.selecao.items) {
                for (j in $scope.navegador.selecao.item[7]) {
                    if (angular.equals($scope.produtoresNvg.selecao.items[i], $scope.navegador.selecao.item[7][j])) {
                        item = angular.copy($scope.navegador.selecao.item[7][j]);
                        editarItem($scope.navegador.selecao.item[7][j], item);
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