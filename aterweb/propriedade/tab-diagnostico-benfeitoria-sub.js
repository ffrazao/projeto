(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';


angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv) {

    // inicializacao
    var init = function() {
        var tmp=[];
        if ( angular.isObject($scope.DiagnosticoNvg.selecao.item )) {
            tmp = $scope.DiagnosticoNvg.selecao.item.benfeitoria;
        }
        $scope.DiagnosticoBenfeitoriaNvg = new FrzNavegadorParams(tmp, 4);
    };
    if (!$modalInstance) {
        init();
    }

    $scope.$watch("DiagnosticoNvg.selecao.item", function () {
        if ($scope.DiagnosticoNvg.selecao.item !== null) {
            init();
        }
    });

    // inicio: atividades do Modal
    $scope.modalOk = function () {
        // Retorno da modal
        var resultado = [];
        for (var file in $scope.$flow.files) {
            resultado.push({nome: $scope.$flow.files[file].name});
        }
        $modalInstance.close(resultado);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.modalCancelar = function () {
        // Cancelar a modal
        $modalInstance.dismiss('cancel');
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.modalAbrir = function (size) {
        // abrir a modal
        var modalInstance = $modal.open({
            animation: true,
            template: '<ng-include src=\"\'pessoa/pessoa-modal.html\'\"></ng-include>',
            controller: 'DiagnosticoBenfeitoriaSubCtrl',
            size: size,
            resolve: {
                modalCadastro: function () {
                    return angular.copy($scope.cadastro);
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (cadastroModificado) {
            // processar o retorno positivo da modal
            $scope.DiagnosticoBenfeitoriaNvg.setDados(cadastroModificado);
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    if ($modalInstance === null) {
        // se objeto modal esta vazio abrir de forma normal
        $scope.modalEstado = null;
//        $scope.navegador.dados[0].vinculado = $scope.cadastro.registro.solo[3];
 //       $scope.DiagnosticoBenfeitoriaNvg.setDados($scope.navegador.dados[0].vinculado);
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
        if ($scope.DiagnosticoBenfeitoriaNvg.selecao.tipo === 'U') {
            $scope.cadastro.original = $scope.DiagnosticoBenfeitoriaNvg.selecao.item;
        } else {
            $scope.cadastro.original = $scope.DiagnosticoBenfeitoriaNvg.selecao.items[$scope.DiagnosticoBenfeitoriaNvg.folhaAtual];
        }
        $scope.cadastro.registro = angular.copy($scope.cadastro.original);
        $scope.DiagnosticoBenfeitoriaNvg.refresh();
    };
    $scope.seleciona = function(item) {
        if (!angular.isObject(item)) {
            return;
        }
        // apoio a selecao de linhas na listagem
        if ($scope.DiagnosticoBenfeitoriaNvg.selecao.tipo === 'U') {
            $scope.DiagnosticoBenfeitoriaNvg.selecao.item = item;
        } else {
            var its = $scope.DiagnosticoBenfeitoriaNvg.selecao.items;
            for (var i in its) {
                if (angular.equals(its[i], item)) {
                    its.splice(i, 1);
                    return;
                }
            }
            $scope.DiagnosticoBenfeitoriaNvg.selecao.items.push(item);
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
        $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('ESPECIAL');
        $scope.DiagnosticoBenfeitoriaNvg.botao('edicao').visivel = false;
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
        $scope.DiagnosticoBenfeitoriaNvg.submitido = true;
        if ($scope.frm.formulario.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }
        $scope.DiagnosticoBenfeitoriaNvg.voltar();
        $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        $scope.DiagnosticoBenfeitoriaNvg.submitido = false;
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
            if ($scope.DiagnosticoBenfeitoriaNvg.selecao.tipo === 'U') {
                $scope.DiagnosticoBenfeitoriaNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoBenfeitoriaNvg.dados, $scope.DiagnosticoBenfeitoriaNvg.selecao.item), 1);
                $scope.DiagnosticoBenfeitoriaNvg.selecao.item = null;
                $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('LISTANDO');
                vaiPara('lista');
            } else {
                var reg = $scope.DiagnosticoBenfeitoriaNvg.selecao.items[$scope.DiagnosticoBenfeitoriaNvg.folhaAtual];
                $scope.DiagnosticoBenfeitoriaNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoBenfeitoriaNvg.dados, reg), 1);
                $scope.DiagnosticoBenfeitoriaNvg.selecao.items.splice(utilSrv.indiceDe($scope.DiagnosticoBenfeitoriaNvg.selecao.items, reg), 1);
                if (!$scope.DiagnosticoBenfeitoriaNvg.selecao.items.length) {
                    $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('LISTANDO');
                    vaiPara('lista');
                } else {
                    if ($scope.DiagnosticoBenfeitoriaNvg.folhaAtual >= $scope.DiagnosticoBenfeitoriaNvg.selecao.items.length) {
                        $scope.DiagnosticoBenfeitoriaNvg.folhaAtual = $scope.DiagnosticoBenfeitoriaNvg.selecao.items.length -1;
                    }
                    verRegistro();
                    $scope.voltar();
                }
            }
        } else if (meuEstado('lista')) {
            if ($scope.DiagnosticoBenfeitoriaNvg.selecao.tipo === 'U') {
                $scope.DiagnosticoBenfeitoriaNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoBenfeitoriaNvg.dados, $scope.DiagnosticoBenfeitoriaNvg.selecao.item), 1);
                $scope.DiagnosticoBenfeitoriaNvg.selecao.item = null;
            } else {
                for (var item = $scope.DiagnosticoBenfeitoriaNvg.selecao.items.length; item--;) {
                    $scope.DiagnosticoBenfeitoriaNvg.dados.splice(utilSrv.indiceDe($scope.DiagnosticoBenfeitoriaNvg.dados, $scope.DiagnosticoBenfeitoriaNvg.selecao.items[item]), 1);
                }
                $scope.DiagnosticoBenfeitoriaNvg.selecao.items = [];
            }
            $scope.voltar();
        }
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.confirmarFiltrar = function() {
        $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('LISTANDO');
        vaiPara('lista');
        $scope.DiagnosticoBenfeitoriaNvg.setDados($scope.cadastro.lista);
    };
    $scope.confirmarIncluir = function() {
        if (!$scope.confirmar()) {
            return;
        }
        $scope.DiagnosticoBenfeitoriaNvg.dados.push($scope.cadastro.registro);
        if ($scope.DiagnosticoBenfeitoriaNvg.selecao.tipo === 'U') {
            $scope.DiagnosticoBenfeitoriaNvg.selecao.item = $scope.cadastro.registro;
        } else {
            $scope.DiagnosticoBenfeitoriaNvg.folhaAtual = $scope.DiagnosticoBenfeitoriaNvg.selecao.items.length;
            $scope.DiagnosticoBenfeitoriaNvg.selecao.items.push($scope.cadastro.registro);
        }
        $scope.DiagnosticoBenfeitoriaNvg.refresh();

        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.editar = function() {
        $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('EDITANDO');
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
        $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('FILTRANDO');
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
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'vinculadoFrm.html',
            controller: pNmController,
            size: 'lg',
            resolve: {
                modalCadastro: function () {
                    return angular.copy($scope.cadastro);
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (cadastroModificado) {
            // processar o retorno positivo da modal
            if (!$scope.cadastro.registro.vinculado) {
                $scope.cadastro.registro.vinculado = [];
            }
            for (var i in cadastroModificado) {
                $scope.cadastro.registro.vinculado.push(cadastroModificado[i]);
            }
            $scope.DiagnosticoBenfeitoriaNvg.setDados($scope.cadastro.registro.vinculado);
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    $scope.informacao = function() {};
    $scope.limpar = function() {
        var e = $scope.DiagnosticoBenfeitoriaNvg.estadoAtual();
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
        $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        verRegistro();
    };
    $scope.voltar = function() {
        $scope.DiagnosticoBenfeitoriaNvg.voltar();
        var estadoAtual = $scope.DiagnosticoBenfeitoriaNvg.estadoAtual();
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

})('propriedade', 'DiagnosticoBenfeitoriaSubCtrl', 'Pessoas Vinculadas à Propriedade');