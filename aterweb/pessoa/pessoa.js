angular.module('pessoa', ['ui.bootstrap','ui.utils','ui.router','ngAnimate', 'frz.navegador']);

angular.module('pessoa').config(['$stateProvider', function($stateProvider) {

    $stateProvider.state('p.pessoa', {
        abstract: true,
        controller: 'PessoaCtrl',
        templateUrl: 'pessoa/pessoa.html',
        url: '/pessoa',
    });
    $stateProvider.state('p.pessoa.filtro', {
        templateUrl: 'pessoa/filtro.html',
        url: '',
    });
    $stateProvider.state('p.pessoa.lista', {
        templateUrl: 'pessoa/lista.html',
        url: '/lista',
    });
    $stateProvider.state('p.pessoa.form', {
        templateUrl: 'pessoa/form.html',
        url: '/form/:id',
    });
    /* Add New States Above */

}]);

angular.module('pessoa').controller('PessoaCtrl', 
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$modal', '$log', '$modalInstance', 'modalCadastro', 
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $modal, $log, $modalInstance, modalCadastro) {

    $scope.dateOptions = {
        formatYear: 'yyyy',
        startingDay: 1
    };
    $scope.open = function($event) {
        $scope.opened = true;
    };

    // inicializacao
    var init = function(cadastro) {
        $scope.nomeFormulario = 'Cadastro de Pessoas';
        $scope.frm = {};
        $scope.cadastro = cadastro != null ? cadastro : {filtro: {}, lista: [], registro: {}, original: {}};
        $scope.navegador = new FrzNavegadorParams($scope.cadastro.lista);
    };
    init(null);

    // inicio: atividades do Modal
    $scope.modalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $modalInstance.close($scope.cadastro);
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
            $scope.navegador.setDados(cadastroModificado.lista);
        }, function () {
            // processar o retorno negativo da modal
            $log.info('Modal dismissed at: ' + new Date());
        });
    };
    if ($modalInstance === null) {
        // se objeto modal esta vazio abrir de forma normal
        $scope.modalEstado = null;
        for (var i = 0; i < 2000; i++) {
            $scope.navegador.dados.push({id: i, nome: 'nome ' + i});
        }
    } else {
        // recuperar o item
        $scope.modalEstado = 'filtro';
        // atualizar o cadastro
        init(modalCadastro);
    }
    // fim: atividades do Modal

    // inicio rotinas de apoio
    var vaiPara = function (estado) {
        if ($scope.modalEstado) {
            $scope.modalEstado = estado;
        } else {
            $state.go('^.' + estado);
        }
    };
    var meuEstado = function (estado) {
        if ($scope.modalEstado) {
            return $scope.modalEstado === estado;
        } else {
            return $state.is('^.' + estado);
        }
    };
    var verRegistro = function() {
        if ($scope.navegador.selecao.tipo === 'U') {
            $scope.cadastro.original = $scope.navegador.selecao.item;
        } else {
            $scope.cadastro.original = $scope.navegador.selecao.items[$scope.navegador.folhaAtual];
        }
        $scope.cadastro.registro = angular.copy($scope.cadastro.original);
        $scope.navegador.refresh();
    };
    $scope.seleciona = function(item) {
        if (!angular.isObject(item)) {
            return;
        }
        // apoio a selecao de linhas na listagem
        if ($scope.navegador.selecao.tipo === 'U') {
            $scope.navegador.selecao.item = item;
        } else {
            var its = $scope.navegador.selecao.items;
            for (var i in its) {
                if (angular.equals(its[i], item)) {
                    its.splice(i, 1);
                    return;
                }
            }
            $scope.navegador.selecao.items.push(item);
        }
    };
    $scope.mataClick = function(event, item) {
        event.stopPropagation();
        $scope.seleciona(item);
    };
    // fim rotinas de apoio

    // inicio rotina para sincronizar estado da tela e navegador
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){ 
        var estadoAtual = $scope.navegador.estadoAtual();
        if (meuEstado('filtro') && ['FILTRANDO'].indexOf(estadoAtual) < 0) {
            $scope.navegador.mudarEstado('FILTRANDO');
        } else 
        if (meuEstado('lista') && ['LISTANDO', 'ESPECIAL'].indexOf(estadoAtual) < 0) {
            $scope.navegador.mudarEstado('LISTANDO');
        } else 
        if (meuEstado('form') && ['INCLUINDO', 'VISUALIZANDO', 'EDITANDO'].indexOf(estadoAtual) < 0) {
            $scope.navegador.mudarEstado('INCLUINDO');
        }
    });
    // fim rotina para sincronizar estado da tela e navegador

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        // ajustar o menu das acoes especiais
        $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [
        {
            nome: 'Enviar E-mail',
            descricao: 'Enviar e-mails',
            acao: function() {console.log($scope.navegador.tamanhoPagina);$scope.enviarEmailConfirmacao();},
            exibir: function() {
                return $scope.navegador.estadoAtual() === 'LISTANDO' && ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) ||
                ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0);
            },
        },
        {
            nome: 'Desbloquear Usuário',
            descricao: 'Desbloquear Usuários',
            acao: function() {console.log('sub acao click ' + this.nome);},
            exibir: function() {
                return $scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 1;
            },
        },
        ];
        // ao iniciar ajustar o navegador com o estado da tela
        if (meuEstado('filtro')) {
            $scope.navegador.mudarEstado('FILTRANDO');
        } else if (meuEstado('lista')) {
            $scope.navegador.mudarEstado('LISTANDO');
        } else if (meuEstado('form')) {
            $scope.navegador.mudarEstado('VISUALIZANDO');
        }
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
        $scope.navegador.submitido = true;
        if ($scope.frm.formulario.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }
        $scope.navegador.voltar();
        $scope.navegador.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        $scope.navegador.submitido = false;

        $scope.cadastro.registro.foto = "img/foto.gif";
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
            if ($scope.navegador.selecao.tipo === 'U') {
                $scope.navegador.dados.splice($scope.indiceDe($scope.navegador.dados, $scope.navegador.selecao.item), 1);
                $scope.navegador.selecao.item = null;
                $scope.navegador.mudarEstado('LISTANDO');
                vaiPara('lista');
            } else {
                var reg = $scope.navegador.selecao.items[$scope.navegador.folhaAtual];
                $scope.navegador.dados.splice($scope.indiceDe($scope.navegador.dados, reg), 1);
                $scope.navegador.selecao.items.splice($scope.indiceDe($scope.navegador.selecao.items, reg), 1);
                if (!$scope.navegador.selecao.items.length) {
                    $scope.navegador.mudarEstado('LISTANDO');
                    vaiPara('lista');
                } else {
                    if ($scope.navegador.folhaAtual >= $scope.navegador.selecao.items.length) {
                        $scope.navegador.folhaAtual = $scope.navegador.selecao.items.length -1;
                    }
                    verRegistro();
                    $scope.voltar();
                }
            }
        } else if (meuEstado('lista')) {
            if ($scope.navegador.selecao.tipo === 'U') {
                $scope.navegador.dados.splice($scope.indiceDe($scope.navegador.dados, $scope.navegador.selecao.item), 1);
                $scope.navegador.selecao.item = null;
            } else {
                for (var item = $scope.navegador.selecao.items.length; item--;) {
                    $scope.navegador.dados.splice($scope.indiceDe($scope.navegador.dados, $scope.navegador.selecao.items[item]), 1);
                }
                $scope.navegador.selecao.items = [];
            }
            $scope.voltar();
        }
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.confirmarFiltrar = function() {
        $scope.navegador.submitido = true;
        if ($scope.frm.filtro.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }
        $scope.navegador.mudarEstado('LISTANDO');
        vaiPara('lista');
        $scope.navegador.setDados($scope.cadastro.lista);
        $scope.navegador.submitido = false;
    };
    $scope.confirmarIncluir = function() {
        if (!$scope.confirmar()) {
            return;
        }
        $scope.navegador.dados.push($scope.cadastro.registro);
        if ($scope.navegador.selecao.tipo === 'U') {
            $scope.navegador.selecao.item = $scope.cadastro.registro;
        } else {
            $scope.navegador.folhaAtual = $scope.navegador.selecao.items.length;
            $scope.navegador.selecao.items.push($scope.cadastro.registro);
        }
        $scope.navegador.refresh();

        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.editar = function() {
        $scope.navegador.mudarEstado('EDITANDO');
        vaiPara('form');
        verRegistro();
        $scope.navegador.submitido = false;
    };
    $scope.excluir = function() {
        $scope.navegador.mudarEstado('EXCLUINDO');
    };
    $scope.filtrar = function() {
        $scope.navegador.mudarEstado('FILTRANDO');
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
        var conf =  '<div class="form-group">' +
                    '    <label class="col-md-4 control-label" for="cnfTipoPessoa">Incluir que tipo de Pessoa?</label>' +
                    '    <div class="col-md-8">' +
                    '        <label class="radio-inline" for="cnfTipoPessoa-0">' +
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-0" value="PJ" ng-model="confirmacao.tipoPessoa" required>' +
                    '            Pessoa Jurídica' +
                    '        </label>' +
                    '        <label class="radio-inline" for="cnfTipoPessoa-1">' +
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-1" value="PF" ng-model="confirmacao.tipoPessoa" required>' +
                    '            Pessoa Física' +
                    '        </label>' +
                    '         <div class="label label-danger" ng-show="confirmacaoFrm.cnfTipoPessoa.$error.required">' +
                    '            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                    '             Campo Obrigatório' +
                    '        </div>' +
                    '    </div>' +
                    '</div>';
        $scope.pegarConfirmacao(conf).then(function (cadastroModificado) {
            // processar o retorno positivo da modal
            $scope.cadastro.original = {tipoPessoa: cadastroModificado.tipoPessoa};
            $scope.cadastro.registro = angular.copy($scope.cadastro.original);
            $scope.navegador.mudarEstado('INCLUINDO');
            vaiPara('form');
            $scope.navegador.submitido = false;
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    $scope.informacao = function() {};
    $scope.limpar = function() {
        var e = $scope.navegador.estadoAtual();
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
        $scope.navegador.mudarEstado('VISUALIZANDO');
        vaiPara('form');
        verRegistro();
    };
    $scope.voltar = function() {
        $scope.navegador.voltar();
        var estadoAtual = $scope.navegador.estadoAtual();
        if (!meuEstado('filtro') && ['FILTRANDO'].indexOf(estadoAtual) >= 0) {
            vaiPara('filtro');
        } else if (!meuEstado('lista') && ['LISTANDO', 'ESPECIAL'].indexOf(estadoAtual) >= 0) {
            vaiPara('lista');
        } else if (!meuEstado('form') && ['INCLUINDO', 'VISUALIZANDO', 'EDITANDO'].indexOf(estadoAtual) >= 0) {
            vaiPara('form');
        }
    };
    // fim das operaçoes atribuidas ao navagador

    // inicio ações especiais
    $scope.enviarEmailConfirmacao = function () {
        var conf = $scope.pegarConfirmacao('Confirme o envio do e-mail?');

        conf.result.then(function () {
          $scope.exibirAlerta('E-mail enviado');
        }, function () {
          toastr.warning('O e-mail não foi enviado...', 'Atenção!');
        });

    };
    // fim ações especiais

}]);