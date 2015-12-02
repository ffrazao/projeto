/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.grupoSocialList)) {
            $scope.cadastro.registro.grupoSocialList = [];
        }
        $scope.pessoaGrupoSocialNvg = new FrzNavegadorParams($scope.cadastro.registro.grupoSocialList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        var gs = UtilSrv.indiceDePorCampo(conteudo.grupoSocialList, conteudo.grupoSocial.id, 'id');
        for (var j in $scope.cadastro.registro.grupoSocialList) {
            if (angular.equals($scope.cadastro.registro.grupoSocialList[j].grupoSocial.nome, gs.nome)) {
                if ($scope.cadastro.registro.grupoSocialList[j].cadastroAcao === 'E') {
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
        item['grupoSocialList'] = $scope.grupoSocialList;
        mensagemSrv.confirmacao(true, 'pessoa-grupoSocial-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            var gs = UtilSrv.indiceDePorCampo(conteudo.grupoSocialList, conteudo.grupoSocial.id, 'id');
            delete gs['@jsonId'];
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.grupoSocial = angular.copy(gs);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.cadastro.registro.grupoSocialList) {
                    init();
                }
                $scope.cadastro.registro.grupoSocialList.push({grupoSocial: gs});
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    $scope.grupoSocialList = [];
    UtilSrv.dominioLista($scope.grupoSocialList, {ent:['GrupoSocial']});

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaGrupoSocialNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {grupoSocial: {nome: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaGrupoSocialNvg.selecao.tipo === 'U' && $scope.pessoaGrupoSocialNvg.selecao.item) {
            item = angular.copy($scope.pessoaGrupoSocialNvg.selecao.item);
            editarItem($scope.pessoaGrupoSocialNvg.selecao.item, item);
        } else if ($scope.pessoaGrupoSocialNvg.selecao.items && $scope.pessoaGrupoSocialNvg.selecao.items.length) {
            for (i in $scope.pessoaGrupoSocialNvg.selecao.items) {
                for (j in $scope.cadastro.registro.grupoSocialList) {
                    if (angular.equals($scope.pessoaGrupoSocialNvg.selecao.items[i], $scope.cadastro.registro.grupoSocialList[j])) {
                        item = angular.copy($scope.cadastro.registro.grupoSocialList[j]);
                        editarItem($scope.cadastro.registro.grupoSocialList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaGrupoSocialNvg.selecao.tipo === 'U' && $scope.pessoaGrupoSocialNvg.selecao.item) {
                for (j = $scope.cadastro.registro.grupoSocialList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.grupoSocialList[j].grupoSocial.nome, $scope.pessoaGrupoSocialNvg.selecao.item.grupoSocial.nome)) {
                        //$scope.cadastro.registro.grupoSocialList.splice(j, 1);
                        $scope.cadastro.registro.grupoSocialList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaGrupoSocialNvg.selecao.item = null;
                $scope.pessoaGrupoSocialNvg.selecao.selecionado = false;
            } else if ($scope.pessoaGrupoSocialNvg.selecao.items && $scope.pessoaGrupoSocialNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.grupoSocialList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaGrupoSocialNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.grupoSocialList[j].grupoSocial.nome, $scope.pessoaGrupoSocialNvg.selecao.items[i].grupoSocial.nome)) {
                            //$scope.cadastro.registro.grupoSocialList.splice(j, 1);
                            $scope.cadastro.registro.grupoSocialList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaGrupoSocialNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaGrupoSocialNvg.selecao.items.splice(i, 1);
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

})('pessoa', 'PessoaGrupoSocialCtrl', 'GrupoSocial vinculado à pessoa');