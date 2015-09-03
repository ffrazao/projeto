(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv', 'mensagemSrv',
        function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv, mensagemSrv) {
            // inicializacao
            var init = function() {
                if (!angular.isObject($scope.cadastro.filtro.grupoSocial)) {
                    $scope.cadastro.filtro.grupoSocial = [];
                }
                $scope.subGrupoSocialNvg = new FrzNavegadorParams($scope.cadastro.filtro.grupoSocial);
            };
            if (!$modalInstance) {
                init();
            }
            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                var resultado = [];
                for (var file in $scope.$flow.files) {
                    resultado.push({
                        nome: $scope.$flow.files[file].name
                    });
                }
                $modalInstance.close(resultado);
                toastr.info('Operação realizada!', 'Informação');
            };
            $scope.modalCancelar = function() {
                // Cancelar a modal
                $modalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.modalAbrir = function(size) {
                // abrir a modal
                var modalInstance = $modal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function() {
                            return angular.copy($scope.cadastro);
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal
                    $scope.subGrupoSocialNvg.setDados(cadastroModificado);
                }, function() {
                    // processar o retorno negativo da modal
                });
            };
            if ($modalInstance === null) {
                // se objeto modal esta vazio abrir de forma normal
                $scope.modalEstado = null;
                for (var i = 0; i < 0; i++) {
                    $scope.subGrupoSocialNvg.dados.push({
                        id: i,
                        nome: 'nome ' + i
                    });
                }
            } else {
                // recuperar o item
                $scope.modalEstado = 'filtro';
                // atualizar o cadastro
                //        $scope.cadastro = angular.copy(modalCadastro);
            }
            // fim: atividades do Modal
            // inicio rotinas de apoio
            var vaiPara = function(estado) {
                if ($scope.modalEstado) {
                    $scope.modalEstado = estado;
                } else {
                    //            $state.go('^.' + estado);
                }
            };
            var meuEstado = function(estado) {
                if ($scope.modalEstado) {
                    return $scope.modalEstado === estado;
                } else {
                    //          return $state.is('^.' + estado);
                }
            };
            var verRegistro = function() {
                if ($scope.subGrupoSocialNvg.selecao.tipo === 'U') {
                    $scope.cadastro.original = $scope.subGrupoSocialNvg.selecao.item;
                } else {
                    $scope.cadastro.original = $scope.subGrupoSocialNvg.selecao.items[$scope.subGrupoSocialNvg.folhaAtual];
                }
                $scope.cadastro.registro = angular.copy($scope.cadastro.original);
                $scope.subGrupoSocialNvg.refresh();
            };
            $scope.seleciona = function(item) {
                if (!angular.isObject(item)) {
                    return;
                }
                // apoio a selecao de linhas na listagem
                if ($scope.subGrupoSocialNvg.selecao.tipo === 'U') {
                    $scope.subGrupoSocialNvg.selecao.item = item;
                } else {
                    var its = $scope.subGrupoSocialNvg.selecao.items;
                    for (var i in its) {
                        if (angular.equals(its[i], item)) {
                            its.splice(i, 1);
                            return;
                        }
                    }
                    $scope.subGrupoSocialNvg.selecao.items.push(item);
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
                $scope.subGrupoSocialNvg.mudarEstado('ESPECIAL');
                $scope.subGrupoSocialNvg.botao('edicao').visivel = false;
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
                $scope.subGrupoSocialNvg.submitido = true;
                if ($scope.frm.formulario.$invalid) {
                    toastr.error('Verifique os campos marcados', 'Erro');
                    return false;
                }
                $scope.subGrupoSocialNvg.voltar();
                $scope.subGrupoSocialNvg.mudarEstado('VISUALIZANDO');
                vaiPara('form');
                $scope.subGrupoSocialNvg.submitido = false;
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
                    if ($scope.subGrupoSocialNvg.selecao.tipo === 'U') {
                        $scope.subGrupoSocialNvg.dados.splice(utilSrv.indiceDe($scope.subGrupoSocialNvg.dados, $scope.subGrupoSocialNvg.selecao.item), 1);
                        $scope.subGrupoSocialNvg.selecao.item = null;
                        $scope.subGrupoSocialNvg.mudarEstado('LISTANDO');
                        vaiPara('lista');
                    } else {
                        var reg = $scope.subGrupoSocialNvg.selecao.items[$scope.subGrupoSocialNvg.folhaAtual];
                        $scope.subGrupoSocialNvg.dados.splice(utilSrv.indiceDe($scope.subGrupoSocialNvg.dados, reg), 1);
                        $scope.subGrupoSocialNvg.selecao.items.splice(utilSrv.indiceDe($scope.subGrupoSocialNvg.selecao.items, reg), 1);
                        if (!$scope.subGrupoSocialNvg.selecao.items.length) {
                            $scope.subGrupoSocialNvg.mudarEstado('LISTANDO');
                            vaiPara('lista');
                        } else {
                            if ($scope.subGrupoSocialNvg.folhaAtual >= $scope.subGrupoSocialNvg.selecao.items.length) {
                                $scope.subGrupoSocialNvg.folhaAtual = $scope.subGrupoSocialNvg.selecao.items.length - 1;
                            }
                            verRegistro();
                            $scope.voltar();
                        }
                    }
                } else if (meuEstado('lista')) {
                    if ($scope.subGrupoSocialNvg.selecao.tipo === 'U') {
                        $scope.subGrupoSocialNvg.dados.splice(utilSrv.indiceDe($scope.subGrupoSocialNvg.dados, $scope.subGrupoSocialNvg.selecao.item), 1);
                        $scope.subGrupoSocialNvg.selecao.item = null;
                    } else {
                        for (var item = $scope.subGrupoSocialNvg.selecao.items.length; item--;) {
                            $scope.subGrupoSocialNvg.dados.splice(utilSrv.indiceDe($scope.subGrupoSocialNvg.dados, $scope.subGrupoSocialNvg.selecao.items[item]), 1);
                        }
                        $scope.subGrupoSocialNvg.selecao.items = [];
                    }
                    $scope.voltar();
                }
                toastr.info('Operação realizada!', 'Informação');
            };
            $scope.confirmarFiltrar = function() {
                $scope.subGrupoSocialNvg.mudarEstado('LISTANDO');
                vaiPara('lista');
                $scope.subGrupoSocialNvg.setDados($scope.cadastro.lista);
            };
            $scope.confirmarIncluir = function() {
                if (!$scope.confirmar()) {
                    return;
                }
                $scope.subGrupoSocialNvg.dados.push($scope.cadastro.registro);
                if ($scope.subGrupoSocialNvg.selecao.tipo === 'U') {
                    $scope.subGrupoSocialNvg.selecao.item = $scope.cadastro.registro;
                } else {
                    $scope.subGrupoSocialNvg.folhaAtual = $scope.subGrupoSocialNvg.selecao.items.length;
                    $scope.subGrupoSocialNvg.selecao.items.push($scope.cadastro.registro);
                }
                $scope.subGrupoSocialNvg.refresh();
                toastr.info('Operação realizada!', 'Informação');
            };
            $scope.editar = function() {
                $scope.subGrupoSocialNvg.mudarEstado('EDITANDO');
                vaiPara('form');
                verRegistro();
            };
            $scope.excluir = function() {
                $scope.pegarConfirmacao('Confirme a exclusao!').result.then(function() {
                    $scope.exibirAlerta('Ok, Em breve estes arquivos serao apagados');
                }, function() {
                    // processar o retorno negativo da modal
                });
            };
            $scope.filtrar = function() {
                $scope.subGrupoSocialNvg.mudarEstado('FILTRANDO');
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
                    templateUrl: 'grupoSocialFrm.html',
                    controller: pNmController,
                    size: 'lg',
                    resolve: {
                        modalCadastro: function() {
                            return angular.copy($scope.cadastro);
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal
                    if (!$scope.cadastro.filtro.grupoSocial) {
                        $scope.cadastro.filtro.grupoSocial = [];
                    }
                    for (var i in cadastroModificado) {
                        $scope.cadastro.filtro.grupoSocial.push(cadastroModificado[i]);
                    }
                    $scope.subGrupoSocialNvg.setDados($scope.cadastro.filtro.grupoSocial);
                }, function() {
                    // processar o retorno negativo da modal
                });
            };
            $scope.informacao = function() {};
            $scope.limpar = function() {
                var e = $scope.subGrupoSocialNvg.estadoAtual();
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
                $scope.subGrupoSocialNvg.mudarEstado('VISUALIZANDO');
                vaiPara('form');
                verRegistro();
            };
            $scope.voltar = function() {
                $scope.subGrupoSocialNvg.voltar();
                var estadoAtual = $scope.subGrupoSocialNvg.estadoAtual();
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
})('pessoa', 'FiltroSubGrupoSocialCtrl', 'Filtro de Grupos Sociais');