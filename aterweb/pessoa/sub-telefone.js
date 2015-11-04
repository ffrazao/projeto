(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.telefoneList)) {
            $scope.cadastro.registro.telefoneList = [];
        }
        $scope.pessoaTelefoneNvg = new FrzNavegadorParams($scope.cadastro.registro.telefoneList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    // $scope.seleciona = function(pessoaTelefoneNvg, item) { };
    // $scope.mataClick = function(pessoaTelefoneNvg, event, item){ };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaTelefoneNvg.mudarEstado('ESPECIAL'); };
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
    $scope.excluir = function() {};
    $scope.filtrar = function() {};
    $scope.folhearAnterior = function() {};
    $scope.folhearPrimeiro = function() {};
    $scope.folhearProximo = function() {};
    $scope.folhearUltimo = function() {};
    $scope.editar = function() {
        if ($scope.pessoaTelefoneNvg.selecao.tipo === 'U' && $scope.pessoaTelefoneNvg.selecao.item) {
            var item = angular.copy($scope.pessoaTelefoneNvg.selecao.item.telefone);
            mensagemSrv.confirmacao(true, 'pessoa-telefone-frm.html', null, item, item.tamanho ).then(function (conteudo) {
                // processar o retorno positivo da modal
                conteudo.numero = formataTelefone(conteudo.numero);
                $scope.pessoaTelefoneNvg.selecao.item.telefone = angular.copy(conteudo);
            }, function () {
                // processar o retorno negativo da modal
                //$log.info('Modal dismissed at: ' + new Date());
            });
        }
    };
    $scope.incluir = function() {
        var item = {};
        mensagemSrv.confirmacao(true, 'pessoa-telefone-frm.html', null, item, item.tamanho ).then(function (conteudo) {
            // processar o retorno positivo da modal
            $scope.cadastro.registro.telefoneList.push({telefone: {'numero': formataTelefone(conteudo.numero)}});
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
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

    var formataTelefone = function(numero) {
        if (!numero) {
            return null;
        }
        var phoneMask8D = new StringMask('(00) 0000-0000'),
            phoneMask9D = new StringMask('(00) 00000-0000');
        var result = numero.toString().replace(/[^0-9]/g, '').slice(0, 11);
        if (result.length < 11){
            result = phoneMask8D.apply(result) || '';
        } else{
            result = phoneMask9D.apply(result);
        }
        return result;
    }

} // fim função
]);

})('pessoa', 'PessoaTelefoneCtrl', 'Telefone vinculado à pessoa');