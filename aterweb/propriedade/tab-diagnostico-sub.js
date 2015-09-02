(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';


angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.diagnostico)) {
            $scope.cadastro.registro.diagnostico = [];
        }
        $scope.DiagnosticoNvg = new FrzNavegadorParams($scope.cadastro.registro.diagnostico, 8);
    };

    if (!$modalInstance) {
        init();
    }

        if ($modalInstance === null) {
        // se objeto modal esta vazio abrir de forma normal
        $scope.modalEstado = null;
//        $scope.navegador.dados[0].vinculado = $scope.cadastro.registro.solo[3];
 //       $scope.DiagnosticoNvg.setDados($scope.navegador.dados[0].vinculado);
        // até aki
    } else {
        // recuperar o item
        $scope.modalEstado = 'filtro';
        // atualizar o cadastro
//        $scope.cadastro = angular.copy(modalCadastro);
    }
    // fim: atividades do Modal

    // inicio rotinas de apoio
    var vaiPara = function (estado) {
        if ($scope.modalEstado) {
            $scope.modalEstado = estado;
        } else {
//            $state.go('^.' + estado);
        }
    };
    var meuEstado = function (estado) {
        if ($scope.modalEstado) {
            return $scope.modalEstado === estado;
        } else {
  //          return $state.is('^.' + estado);
        }
    };
    var verRegistro = function() {
        if ($scope.DiagnosticoNvg.selecao.tipo === 'U') {
            $scope.cadastro.original = $scope.DiagnosticoNvg.selecao.item;
        } else {
            $scope.cadastro.original = $scope.DiagnosticoNvg.selecao.items[$scope.DiagnosticoNvg.folhaAtual];
        }
        $scope.cadastro.registro = angular.copy($scope.cadastro.original);
        $scope.DiagnosticoNvg.refresh();
    };
    $scope.seleciona = function(item) {
        if (!angular.isObject(item)) {
            return;
        }
        // apoio a selecao de linhas na listagem
        if ($scope.DiagnosticoNvg.selecao.tipo === 'U') {
            $scope.DiagnosticoNvg.selecao.item = item;
        } else {
            var its = $scope.DiagnosticoNvg.selecao.items;
            for (var i in its) {
                if (angular.equals(its[i], item)) {
                    its.splice(i, 1);
                    return;
                }
            }
            $scope.DiagnosticoNvg.selecao.items.push(item);
        }
    };
    $scope.mataClick = function(event, item) {
        event.stopPropagation();
        $scope.seleciona(item);
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        // ajustar o menu das acoes especiais
        $scope.DiagnosticoNvg.mudarEstado('ESPECIAL');
        $scope.DiagnosticoNvg.botao('edicao').visivel = false;
    };
    $scope.agir = function() {};
    $scope.ajudar = function() {};
    $scope.alterarTamanhoPagina = function() {};
    $scope.cancelar = function() {
        $scope.voltar();
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.cancelarEditar = function() {
        $scope.cancelar();
    };
    $scope.cancelarExcluir = function() {
        $scope.cancelar();
    };
    $scope.cancelarFiltrar = function() {
        $scope.cancelar();
    };
    $scope.cancelarIncluir = function() {
        $scope.cancelar();
    };
    $scope.confirmar = function() {
        $scope.DiagnosticoNvg.submitido = true;
        if ($scope.frm.formulario.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }
        $scope.DiagnosticoNvg.voltar();
        $scope.DiagnosticoNvg.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        $scope.DiagnosticoNvg.submitido = false;
        return true;
    };
    $scope.confirmarEditar = function() {
        if (!$scope.confirmar()) {
            return;
        }
        angular.copy($scope.cadastro.registro, $scope.cadastro.original);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.confirmarExcluir = function() {
        if (meuEstado('form')) {
            if ($scope.DiagnosticoNvg.selecao.tipo === 'U') {
                $scope.DiagnosticoNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoNvg.dados, $scope.DiagnosticoNvg.selecao.item), 1);
                $scope.DiagnosticoNvg.selecao.item = null;
                $scope.DiagnosticoNvg.mudarEstado('LISTANDO');
                vaiPara('lista');
            } else {
                var reg = $scope.DiagnosticoNvg.selecao.items[$scope.DiagnosticoNvg.folhaAtual];
                $scope.DiagnosticoNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoNvg.dados, reg), 1);
                $scope.DiagnosticoNvg.selecao.items.splice(utilSrv.indiceDe($scope.DiagnosticoNvg.selecao.items, reg), 1);
                if (!$scope.DiagnosticoNvg.selecao.items.length) {
                    $scope.DiagnosticoNvg.mudarEstado('LISTANDO');
                    vaiPara('lista');
                } else {
                    if ($scope.DiagnosticoNvg.folhaAtual >= $scope.DiagnosticoNvg.selecao.items.length) {
                        $scope.DiagnosticoNvg.folhaAtual = $scope.DiagnosticoNvg.selecao.items.length -1;
                    }
                    verRegistro();
                    $scope.voltar();
                }
            }
        } else if (meuEstado('lista')) {
            if ($scope.DiagnosticoNvg.selecao.tipo === 'U') {
                $scope.DiagnosticoNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoNvg.dados, $scope.DiagnosticoNvg.selecao.item), 1);
                $scope.DiagnosticoNvg.selecao.item = null;
            } else {
                for (var item = $scope.DiagnosticoNvg.selecao.items.length; item--;) {
                    $scope.DiagnosticoNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoNvg.dados, $scope.DiagnosticoNvg.selecao.items[item]), 1);
                }
                $scope.DiagnosticoNvg.selecao.items = [];
            }
            $scope.voltar();
        }
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.confirmarFiltrar = function() {
        $scope.DiagnosticoNvg.mudarEstado('LISTANDO');
        vaiPara('lista');
        $scope.DiagnosticoNvg.setDados($scope.cadastro.lista);
    };
    $scope.confirmarIncluir = function() {
        if (!$scope.confirmar()) {
            return;
        }
        $scope.DiagnosticoNvg.dados.push($scope.cadastro.registro);
        if ($scope.DiagnosticoNvg.selecao.tipo === 'U') {
            $scope.DiagnosticoNvg.selecao.item = $scope.cadastro.registro;
        } else {
            $scope.DiagnosticoNvg.folhaAtual = $scope.DiagnosticoNvg.selecao.items.length;
            $scope.DiagnosticoNvg.selecao.items.push($scope.cadastro.registro);
        }
        $scope.DiagnosticoNvg.refresh();

        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.editar = function() {
        $scope.DiagnosticoNvg.mudarEstado('EDITANDO');
        vaiPara('form');
        verRegistro();
    };
    $scope.excluir = function() {
        $scope.pegarConfirmacao('Confirme a exclusao!').result.then(function () {
            $scope.exibirAlerta('Ok, Em breve estes arquivos serao apagados');
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    $scope.filtrar = function() {
        $scope.DiagnosticoNvg.mudarEstado('FILTRANDO');
        vaiPara('filtro');
    };
    $scope.folhearAnterior = function() {
        verRegistro();
    };
    $scope.folhearPrimeiro = function() {
        verRegistro();
    };
    $scope.folhearProximo = function() {
        verRegistro();
    };
    $scope.folhearUltimo = function() {
        verRegistro();
    };
    $scope.incluir = function() {
        var tmp = $scope.DiagnosticoNvg.selecao.item;
        var item = { arquivo: 'tab-diagnostico-sub-modal.html', descricao: 'Diagnóstico', tamanho :800,
                     dados: tmp
                    };
        $scope.abreModal(item);
    };


    $scope.informacao = function() {};
    $scope.limpar = function() {
        var e = $scope.DiagnosticoNvg.estadoAtual();
        if ('FILTRANDO' === e) {
            $scope.cadastro.filtro = {};
        } else {
            $scope.cadastro.registro = {};
        }
    };
    $scope.paginarAnterior = function() {};
    $scope.paginarPrimeiro = function() {};
    $scope.paginarProximo = function() {};
    $scope.paginarUltimo = function() {};
    $scope.restaurar = function() {
        angular.copy($scope.cadastro.original, $scope.cadastro.registro);
    };
    $scope.visualizar = function() {
        $scope.DiagnosticoNvg.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        verRegistro();
    };
    $scope.voltar = function() {
        $scope.DiagnosticoNvg.voltar();
        var estadoAtual = $scope.DiagnosticoNvg.estadoAtual();
        if (!meuEstado('filtro') && ['FILTRANDO'].indexOf(estadoAtual) >= 0) {
            vaiPara('filtro');
        } else if (!meuEstado('lista') && ['LISTANDO', 'ESPECIAL'].indexOf(estadoAtual) >= 0) {
            vaiPara('lista');
        } else if (!meuEstado('form') && ['INCLUINDO', 'VISUALIZANDO', 'EDITANDO'].indexOf(estadoAtual) >= 0) {
            vaiPara('form');
        }
    };
    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

})('propriedade', 'DiagnosticoSubCtrl', 'Pessoas Vinculadas à Propriedade');