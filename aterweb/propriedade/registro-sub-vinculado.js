(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.vinculado)) {
            $scope.cadastro.registro.vinculado = [];
        }
        $scope.subVinculadoNvg = new FrzNavegadorParams($scope.cadastro.registro.vinculado, 4);
    };
    if (!$modalInstance) {
        init();
    }

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
            controller: 'PessoaCtrl',
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
            $scope.subVinculadoNvg.setDados(cadastroModificado);
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    if ($modalInstance === null) {
        // se objeto modal esta vazio abrir de forma normal
        $scope.modalEstado = null;
        $scope.navegador.dados[0].vinculado = [];
        //aki
        for (var i = 0; i < 11; i++) {
            $scope.navegador.dados[0].vinculado.push({id: i, nome: 'nome ' + i, cpf: (333*i), tpExploracao: 'P', ha :(2.7*i), situacao : 'S' });
        }
        $scope.subVinculadoNvg.setDados($scope.navegador.dados[0].vinculado);
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
        if ($scope.subVinculadoNvg.selecao.tipo === 'U') {
            $scope.cadastro.original = $scope.subVinculadoNvg.selecao.item;
        } else {
            $scope.cadastro.original = $scope.subVinculadoNvg.selecao.items[$scope.subVinculadoNvg.folhaAtual];
        }
        $scope.cadastro.registro = angular.copy($scope.cadastro.original);
        $scope.subVinculadoNvg.refresh();
    };
    $scope.seleciona = function(item) {
        if (!angular.isObject(item)) {
            return;
        }
        // apoio a selecao de linhas na listagem
        if ($scope.subVinculadoNvg.selecao.tipo === 'U') {
            $scope.subVinculadoNvg.selecao.item = item;
        } else {
            var its = $scope.subVinculadoNvg.selecao.items;
            for (var i in its) {
                if (angular.equals(its[i], item)) {
                    its.splice(i, 1);
                    return;
                }
            }
            $scope.subVinculadoNvg.selecao.items.push(item);
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
        $scope.subVinculadoNvg.mudarEstado('ESPECIAL');
        $scope.subVinculadoNvg.botao('edicao').visivel = false;
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
        $scope.subVinculadoNvg.submitido = true;
        if ($scope.frm.formulario.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }
        $scope.subVinculadoNvg.voltar();
        $scope.subVinculadoNvg.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        $scope.subVinculadoNvg.submitido = false;
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
            if ($scope.subVinculadoNvg.selecao.tipo === 'U') {
                $scope.subVinculadoNvg.dados.splice(utilSrv.indiceDe($scope.subVinculadoNvg.dados, $scope.subVinculadoNvg.selecao.item), 1);
                $scope.subVinculadoNvg.selecao.item = null;
                $scope.subVinculadoNvg.mudarEstado('LISTANDO');
                vaiPara('lista');
            } else {
                var reg = $scope.subVinculadoNvg.selecao.items[$scope.subVinculadoNvg.folhaAtual];
                $scope.subVinculadoNvg.dados.splice(utilSrv.indiceDe($scope.subVinculadoNvg.dados, reg), 1);
                $scope.subVinculadoNvg.selecao.items.splice(utilSrv.indiceDe($scope.subVinculadoNvg.selecao.items, reg), 1);
                if (!$scope.subVinculadoNvg.selecao.items.length) {
                    $scope.subVinculadoNvg.mudarEstado('LISTANDO');
                    vaiPara('lista');
                } else {
                    if ($scope.subVinculadoNvg.folhaAtual >= $scope.subVinculadoNvg.selecao.items.length) {
                        $scope.subVinculadoNvg.folhaAtual = $scope.subVinculadoNvg.selecao.items.length -1;
                    }
                    verRegistro();
                    $scope.voltar();
                }
            }
        } else if (meuEstado('lista')) {
            if ($scope.subVinculadoNvg.selecao.tipo === 'U') {
                $scope.subVinculadoNvg.dados.splice(utilSrv.indiceDe($scope.subVinculadoNvg.dados, $scope.subVinculadoNvg.selecao.item), 1);
                $scope.subVinculadoNvg.selecao.item = null;
            } else {
                for (var item = $scope.subVinculadoNvg.selecao.items.length; item--;) {
                    $scope.subVinculadoNvg.dados.splice(utilSrv.indiceDe($scope.subVinculadoNvg.dados, $scope.subVinculadoNvg.selecao.items[item]), 1);
                }
                $scope.subVinculadoNvg.selecao.items = [];
            }
            $scope.voltar();
        }
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.confirmarFiltrar = function() {
        $scope.subVinculadoNvg.mudarEstado('LISTANDO');
        vaiPara('lista');
        $scope.subVinculadoNvg.setDados($scope.cadastro.lista);
    };
    $scope.confirmarIncluir = function() {
        if (!$scope.confirmar()) {
            return;
        }
        $scope.subVinculadoNvg.dados.push($scope.cadastro.registro);
        if ($scope.subVinculadoNvg.selecao.tipo === 'U') {
            $scope.subVinculadoNvg.selecao.item = $scope.cadastro.registro;
        } else {
            $scope.subVinculadoNvg.folhaAtual = $scope.subVinculadoNvg.selecao.items.length;
            $scope.subVinculadoNvg.selecao.items.push($scope.cadastro.registro);
        }
        $scope.subVinculadoNvg.refresh();

        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.editar = function() {
        $scope.subVinculadoNvg.mudarEstado('EDITANDO');
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
        $scope.subVinculadoNvg.mudarEstado('FILTRANDO');
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
            $scope.subVinculadoNvg.setDados($scope.cadastro.registro.vinculado);
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    $scope.informacao = function() {};
    $scope.limpar = function() {
        var e = $scope.subVinculadoNvg.estadoAtual();
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
        $scope.subVinculadoNvg.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        verRegistro();
    };
    $scope.voltar = function() {
        $scope.subVinculadoNvg.voltar();
        var estadoAtual = $scope.subVinculadoNvg.estadoAtual();
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

})('propriedade', 'RegistroVinculadoCtrl', 'Pessoas Vinculadas à Propriedade');