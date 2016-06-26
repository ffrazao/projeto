/* global StringMask:false, removerCampo */

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
        if (!$scope.pessoaGrupoSocialNvg) {
            $scope.pessoaGrupoSocialNvg = new FrzNavegadorParams($scope.cadastro.registro.grupoSocialList, 4);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        var i, id, grupo;
        for (i in $scope.cadastro.registro.grupoSocialList) {
            id = $scope.cadastro.registro.grupoSocialList[i].id;
            grupo = $scope.cadastro.registro.grupoSocialList[i].grupoSocial;
            if (!angular.equals(id, conteudo.id) && angular.equals(grupo.id, conteudo.grupoSocial.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    var editarItem = function (destino, item) {
        item['grupoSocialList'] = $scope.grupoSocialList;
        mensagemSrv.confirmacao(true, 'pessoa-grupoSocial-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            init();
            // processar o retorno positivo da modal
            conteudo = $scope.editarElemento(conteudo);
            if (destino) {
                destino.grupoSocial = angular.copy(conteudo.grupoSocial);
            } else {
                $scope.cadastro.registro.grupoSocialList.push(conteudo);
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
        init();
        var item = {grupoSocial: {nome: null}};
        item = $scope.criarElemento($scope.cadastro.registro, 'grupoSocialList', item);
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var j, i;
        if ($scope.pessoaGrupoSocialNvg.selecao.tipo === 'U' && $scope.pessoaGrupoSocialNvg.selecao.item) {
            item = angular.copy($scope.pessoaGrupoSocialNvg.selecao.item);
            editarItem($scope.pessoaGrupoSocialNvg.selecao.item, item);
        } else if ($scope.pessoaGrupoSocialNvg.selecao.items && $scope.pessoaGrupoSocialNvg.selecao.items.length) {
            for (i in $scope.pessoaGrupoSocialNvg.selecao.items) {
                for (j in $scope.cadastro.registro.grupoSocialList) {
                    if (angular.equals($scope.pessoaGrupoSocialNvg.selecao.items[i].id, $scope.cadastro.registro.grupoSocialList[j].id)) {
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
            removerCampo($scope.cadastro.registro.grupoSocialList, ['@jsonId']);
            if ($scope.pessoaGrupoSocialNvg.selecao.tipo === 'U' && $scope.pessoaGrupoSocialNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'grupoSocialList', $scope.pessoaGrupoSocialNvg.selecao.item);
            } else if ($scope.pessoaGrupoSocialNvg.selecao.items && $scope.pessoaGrupoSocialNvg.selecao.items.length) {
                for (i in $scope.pessoaGrupoSocialNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'grupoSocialList', $scope.pessoaGrupoSocialNvg.selecao.items[i]);
                }
            }
            $scope.pessoaGrupoSocialNvg.selecao.item = null;
            $scope.pessoaGrupoSocialNvg.selecao.items = [];
            $scope.pessoaGrupoSocialNvg.selecao.selecionado = false;
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