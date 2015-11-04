(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',

        function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {
            // inicializacao
            var init = function() {
                if (!angular.isObject($scope.cadastro.registro.enderecoList)) {
                    $scope.cadastro.registro.enderecoList = [];
                }
                $scope.pessoaEnderecoNvg = new FrzNavegadorParams($scope.cadastro.registro.enderecoList, 5);
            };
            if (!$modalInstance) {
                init();
            }
            /*if ($modalInstance === null) {
                $scope.navegador.dados[0].enderecoList = [];
                for (var i = 0; i < 11; i++) {
                    $scope.navegador.dados[0].enderecoList.push({
                        id: i,
                        nome: 'nome ' + i,
                        cpf: (333 * i),
                        tpExploracao: 'P',
                        ha: (2.7 * i),
                        situacao: 'S'
                    });
                }
                $scope.pessoaEnderecoNvg.setDados($scope.navegador.dados[0].enderecoList);
            }*/
            
            // inicio rotinas de apoio
            // $scope.seleciona = function(pessoaEnderecoNvg, item) { };
            // $scope.mataClick = function(pessoaEnderecoNvg, event, item){ };
            // fim rotinas de apoio
            // inicio das operaçoes atribuidas ao navagador
            $scope.abrir = function() {
                $scope.pessoaEnderecoNvg.mudarEstado('ESPECIAL');
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
                mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', 'Cadastro de Endereço', item, 'lg').then(function(conteudo) {
                    // processar o retorno positivo da modal
                    if (!angular.isArray($scope.cadastro.registro.enderecoList)) {
                        init();
                    }
                    $scope.cadastro.registro.enderecoList.push(conteudo);
                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            // inicio mapa
            $scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };
            // fim mapa

        } // fim função
    ]);
})('pessoa', 'PessoaEnderecoCtrl', 'Endereço vinculado à pessoa');