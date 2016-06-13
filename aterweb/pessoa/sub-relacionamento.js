/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.relacionamentoList)) {
            $scope.cadastro.registro.relacionamentoList = [];
        }
        $scope.pessoaRelacionamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.relacionamentoList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        return true;
    };
    var editarItem = function (destino, item) {
        $scope.modalSelecinarRelacionado();
    };
    $scope.modalSelecinarRelacionado = function (size) {
        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'pessoa/pessoa-modal.html',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function() {
                    return $scope.cadastroBase();
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            var reg = null;
            if (resultado.selecao.tipo === 'U') {
                reg = {pessoa: {id: resultado.selecao.item[0], nome: resultado.selecao.item[1], pessoaTipo: resultado.selecao.item[3], genero: resultado.selecao.item[9]}};
                $scope.preparaClassePessoa(reg.pessoa);
                captaRelacionamento(reg);
            } else {
                for (var i in resultado.selecao.items) {
                    reg = {pessoa: {id: resultado.selecao.items[i][0], nome: resultado.selecao.items[i][1], pessoaTipo: resultado.selecao.items[i][3], genero: resultado.selecao.items[i][9]}};
                    $scope.preparaClassePessoa(reg.pessoa);
                    captaRelacionamento(reg);
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            captaRelacionamento({});
        });
    };

    var captaRelacionamento = function(reg, destino) {
        if (!reg) {
            reg = {};
        }
        var item = angular.copy(reg);
        if (!item.relacionador) {
            item.relacionador = angular.copy($scope.cadastro.registro);
        }
        if (!item.relacionado) {
            item.relacionado = angular.copy(reg.pessoa);
        }
        var iniciar = function(scpInt) {
            $scope.scpInt = scpInt;

            if (!scpInt.conteudo) {
                scpInt.conteudo = {};
            }
            if (!scpInt.apoio) {
                scpInt.apoio = {relacionamentoTipoList: [], relacionamentoFuncaoList: []};
            }
            scpInt.UtilSrv = UtilSrv;
            scpInt.apoio.pessoaTipoList = angular.copy($scope.cadastro.apoio.pessoaTipoList);
            scpInt.apoio.estadoList = angular.copy($scope.cadastro.apoio.estadoList);
            scpInt.apoio.generoList = angular.copy($scope.cadastro.apoio.generoList);
            scpInt.apoio.paisList = angular.copy($scope.cadastro.apoio.paisList);
            scpInt.apoio.nascimentoEstadoList = angular.copy($scope.cadastro.apoio.nascimentoEstadoList);
            scpInt.apoio.nascimentoMunicipioList = angular.copy($scope.cadastro.apoio.nascimentoMunicipioList);
            scpInt.apoio.regimeCasamentoList = angular.copy($scope.cadastro.apoio.regimeCasamentoList);
            scpInt.apoio.escolaridadeList = angular.copy($scope.cadastro.apoio.escolaridadeList);
            scpInt.apoio.profissaoList = angular.copy($scope.cadastro.apoio.profissaoList);
            scpInt.apoio.escolaridadeList = angular.copy($scope.cadastro.apoio.escolaridadeList);

            var relacionamentoTipo = null;
            $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                if (relacionamentoTipo !== item.tipoId) {
                    relacionamentoTipo = item.tipoId;
                    if (
                        item.tipoRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                        item.tipoRelacionado.indexOf(reg.pessoa ? reg.pessoa.pessoaTipo : 'PF') >= 0
                        ) {
                        scpInt.apoio.relacionamentoTipoList.push({id: item.tipoId, nome: item.tipoNome});
                    }
                }
            });

            scpInt.$watch('conteudo.relacionamento.relacionamentoTipo', function(newValue, oldValue) {
                scpInt.apoio.relacionamentoFuncaoList = [];

                if (newValue) {
                    $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                        if (newValue.id === item.tipoId) {
                            if (
                                item.relacionadorPessoaTipo.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                                item.relacionadoPessoaTipo.indexOf(reg.pessoa ? reg.pessoa.pessoaTipo : 'PF') >= 0
                                ) {
                                scpInt.apoio.relacionamentoFuncaoList.push(item);
                            }
                        }
                    });
                }

            });

            scpInt.$watch('conteudo.nascimento', function(newValue, oldValue) {
                if (!$scope.scpInt.conteudo) {
                    $scope.scpInt.conteudo = {};
                }
                if (!$scope.scpInt.apoio) {
                    $scope.scpInt.apoio = {};
                }
                $scope.scpInt.conteudo.idade = null;
                $scope.scpInt.conteudo.geracao = null;
                $scope.scpInt.apoio.geracao = null;
                var nascimento = null;
                if (!newValue) {
                    return;
                }
                // captar a data de nascimento
                if(newValue instanceof Date) {
                    nascimento = newValue;
                } else {
                    // converter caso necessario
                    if (newValue.length < 10) {
                        return;
                    }
                    var partes = newValue.substr(0, 10).split('/');
                    nascimento = new Date(partes[2],partes[1]-1,partes[0]);
                }
                var hoje = new Date();
                var idade = hoje.getFullYear() - nascimento.getFullYear();
                if ( new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate()) < 
                        new Date(hoje.getFullYear(), nascimento.getMonth(), nascimento.getDate()) ) {
                    idade--;
                }
                $scope.scpInt.conteudo.idade = idade >= 0 ? idade : null;
                if (idade >= 0 && idade < 12) {
                    $scope.scpInt.conteudo.geracao = 'C';
                    $scope.scpInt.apoio.geracao = 'Criança';
                } else if (idade >= 12 && idade < 18) {
                    $scope.scpInt.conteudo.geracao = 'J';
                    $scope.scpInt.apoio.geracao = 'Jovem';
                } else if (idade >= 18 && idade < 60) {
                    $scope.scpInt.conteudo.geracao = 'A';
                    $scope.scpInt.apoio.geracao = 'Adulto';
                } else if (idade >= 60 && idade < 140) {
                    $scope.scpInt.conteudo.geracao = 'I';
                    $scope.scpInt.apoio.geracao = 'Idoso';
                } else {
                    $scope.scpInt.apoio.geracao = 'Inválido';
                }
            });
            scpInt.$watch('conteudo.nascimentoPais.id', function(newValue, oldValue) {
                if (newValue) {
                    UtilSrv.dominioLista($scope.scpInt.apoio.nascimentoEstadoList, {ent:['Estado'], npk: ['pais.id'], vpk: [newValue]});
                } else {
                    $scope.scpInt.apoio.nascimentoEstadoList = [];
                }
            });
            scpInt.$watch('conteudo.nascimentoEstado.id', function(newValue, oldValue) {
                if (newValue) {
                    UtilSrv.dominioLista($scope.scpInt.apoio.nascimentoMunicipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue]});
                } else {
                    $scope.scpInt.apoio.nascimentoMunicipioList = [];
                }
            });
            scpInt.$watch('conteudo.nascimentoPais.id + conteudo.naturalizado', function(newValue, oldValue) {
                if (!$scope.scpInt.conteudo) {
                    $scope.scpInt.conteudo = {};
                }
                if (!$scope.scpInt.apoio) {
                    $scope.scpInt.apoio = {};
                }

                $scope.scpInt.conteudo.nacionalidade = null;
                $scope.scpInt.apoio.nacionalidade = null;
                if (!($scope.scpInt.conteudo.nascimentoPais && $scope.scpInt.conteudo.nascimentoPais.id)) {
                    $scope.scpInt.conteudo.naturalizado = null;
                    $scope.scpInt.conteudo.nascimentoEstado = null;
                    $scope.scpInt.conteudo.nascimentoMunicipio = null;
                    return;
                }
                if ($scope.scpInt.conteudo.nascimentoPais && $scope.scpInt.conteudo.nascimentoPais.padrao === 'S') {
                    $scope.scpInt.conteudo.nacionalidade = 'BN'; 
                    $scope.scpInt.conteudo.naturalizado = false;
                } else {
                    $scope.scpInt.conteudo.nascimentoEstado = null;
                    $scope.scpInt.conteudo.nascimentoMunicipio = null;
                    $scope.scpInt.conteudo.nacionalidade = $scope.scpInt.conteudo.naturalizado ? 'NA' : 'ES';
                }
                if ($scope.scpInt.conteudo.nacionalidade) {
                    $scope.scpInt.apoio.nacionalidade = UtilSrv.indiceDePorCampo($scope.scpInt.apoio.nacionalidadeList, $scope.scpInt.conteudo.nacionalidade, 'codigo');
                }
            });
        };


        mensagemSrv.confirmacao(true, 'pessoa/sub-relacionamento-form.html', null, item, null, jaCadastrado, null, iniciar).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (!$scope.cadastro.registro.relacionamentoList) {
                $scope.cadastro.registro.relacionamentoList = [];
                $scope.pessoaRelacionamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.relacionamentoList, 4);
            }
            var registro = {
                "relacionamento": {
                    "@class" : "br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento", 
                    "relacionamentoTipo": angular.copy(conteudo.relacionamento.relacionamentoTipo)
                }, 
                "relacionamentoFuncao": angular.copy(conteudo.relacionamentoFuncao)
            };
            delete conteudo.relacionamento;
            delete conteudo.relacionamentoFuncao;
            if (conteudo.relacionado) {
                registro.pessoa = angular.copy(conteudo.relacionado);
            } else {
                registro = angular.extend(conteudo, registro);
                delete registro.relacionado;
            }
            if (destino) {
                angular.copy(registro, destino);
            } else {
                $scope.cadastro.registro.relacionamentoList.push(registro);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaRelacionamentoNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {relacionamento: {}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        if ($scope.pessoaRelacionamentoNvg.selecao.tipo === 'U' && $scope.pessoaRelacionamentoNvg.selecao.item) {
            captaRelacionamento($scope.pessoaRelacionamentoNvg.selecao.item, $scope.pessoaRelacionamentoNvg.selecao.item);
        } else if ($scope.pessoaRelacionamentoNvg.selecao.items && $scope.pessoaRelacionamentoNvg.selecao.items.length) {
            for (i = $scope.pessoaRelacionamentoNvg.selecao.items.length -1; i >= 0; i--) {
                captaRelacionamento($scope.pessoaRelacionamentoNvg.selecao.items[i], $scope.pessoaRelacionamentoNvg.selecao.items[i]);
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaRelacionamentoNvg.selecao.tipo === 'U' && $scope.pessoaRelacionamentoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.relacionamentoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, $scope.pessoaRelacionamentoNvg.selecao.item.relacionamento.endereco)) {
                        //$scope.cadastro.registro.relacionamentoList.splice(j, 1);
                        $scope.cadastro.registro.relacionamentoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaRelacionamentoNvg.selecao.item = null;
                $scope.pessoaRelacionamentoNvg.selecao.selecionado = false;
            } else if ($scope.pessoaRelacionamentoNvg.selecao.items && $scope.pessoaRelacionamentoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.relacionamentoList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaRelacionamentoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, $scope.pessoaRelacionamentoNvg.selecao.items[i].relacionamento.endereco)) {
                            //$scope.cadastro.registro.relacionamentoList.splice(j, 1);
                            $scope.cadastro.registro.relacionamentoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaRelacionamentoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaRelacionamentoNvg.selecao.items.splice(i, 1);
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

    // inicio dos watches
/*    $scope.$watch(function($scope) {
        if (!$scope.cadastro.registro.relacionamentoList || !$scope.cadastro.registro.relacionamentoList.length) {
            return null;
        }
        return $scope.cadastro.registro.relacionamentoList.map(function(obj) {
            if (!obj || !obj.relacionamento) {
                return undefined;
            }
            return {obj: obj, relacionamentoTipo: obj.relacionamento.relacionamentoTipo};
        });
    }, function(newValue, oldValue) {
        // TODO ajustar as funcoes do relacionamento
        if (!newValue) {
            return;
        }
        newValue.apoio = {relacionamentoTipoList: [], relacionamentoFuncaoList: []};
        if (!$scope.cadastro.registro.pessoaTipo || !newValue.pessoa) {
            return;
        }
        var relacionamentoTipo = null;
        $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
            if (relacionamentoTipo !== item.tipoId) {
                relacionamentoTipo = item.tipoId;
                if (
                    item.tipoRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                    item.tipoRelacionado.indexOf(newValue.pessoa.pessoaTipo) >= 0
                    ) {
                    newValue.apoio.relacionamentoTipoList.push({id: item.tipoId, nome: item.tipoNome});
                }
            }
        });
        if (newValue.relacionamento && newValue.relacionamento.relacionamentoTipo) {
            $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                if (newValue.relacionamento.relacionamentoTipo === item.tipoId) {
                    if (
                        item.relacionadorRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                        item.relacionadorRelacionado.indexOf(newValue.pessoa.pessoaTipo) >= 0 &&

                        item.relacionadoRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                        item.relacionadoRelacionado.indexOf(newValue.pessoa.pessoaTipo) >= 0
                        ) {
                        newValue.apoio.relacionamentoFuncaoList.push({id: item.id, nomeSeFeminino: item.nomeSeFeminino, nomeSeMasculino: item.nomeSeMasculino, configTemporario: item.configTemporario});
                    }
                }
            });
        }
        console.log(newValue.apoio);
    }, true);*/
    // fim dos watches

} // fim função
]);

})('pessoa', 'PessoaRelacionamentoCtrl', 'Relacionamento vinculado à pessoa');