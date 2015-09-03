(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv', 'mensagemSrv',

        function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv, mensagemSrv) {
            // inicializacao
            var init = function() {
                if (!angular.isObject($scope.cadastro.registro.diagnosticoCaptacaoList)) {
                    $scope.cadastro.registro.diagnosticoCaptacaoList = [];
                }
                $scope.pessoaDiagnosticoCaptacaoNvg = new FrzNavegadorParams($scope.cadastro.registro.diagnosticoCaptacaoList, 5);
            };
            if (!$modalInstance) {
                init();
            }
            if ($modalInstance === null) {
                $scope.navegador.dados[0].diagnosticoCaptacaoList = [];
                for (var i = 0; i < 11; i++) {
                    $scope.navegador.dados[0].diagnosticoCaptacaoList.push({
                        id: i,
                        nome: 'nome ' + i,
                        cpf: (333 * i),
                        tpExploracao: 'P',
                        ha: (2.7 * i),
                        situacao: 'S'
                    });
                }
                $scope.pessoaDiagnosticoCaptacaoNvg.setDados($scope.navegador.dados[0].diagnosticoCaptacaoList);
            }
            // inicio rotinas de apoio
            // $scope.seleciona = function(pessoaDiagnosticoCaptacaoNvg, item) { };
            // $scope.mataClick = function(pessoaDiagnosticoCaptacaoNvg, event, item){ };
            // fim rotinas de apoio
            // inicio das operaçoes atribuidas ao navagador
            $scope.abrir = function() {
                $scope.pessoaDiagnosticoCaptacaoNvg.mudarEstado('ESPECIAL');
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
            $scope.excluir = function() {};
            $scope.filtrar = function() {};
            $scope.folhearAnterior = function() {};
            $scope.folhearPrimeiro = function() {};
            $scope.folhearProximo = function() {};
            $scope.folhearUltimo = function() {};
            $scope.editar = function() {
                $scope.incluir();
            };
            $scope.incluir = function() {
                var item = {};
                $scope.abreModal(item);
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
            $scope.abreModal = function(item) {
                // abrir a modal
                mensagemSrv.confirmacao(true, 'pessoa/' + item.arquivo, item.descricao, item, item.tamanho).then(function(conteudo) {
                    // processar o retorno positivo da modal

                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
        } // fim função
    ]);

})('pessoa', 'PessoaDiagnosticoCaptacaoCtrl', 'Captação Diagnóstico vinculado à pessoa');