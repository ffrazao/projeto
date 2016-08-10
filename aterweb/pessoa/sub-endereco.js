/* global StringMask:false, removerCampo, isUndefOrNull */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'EnderecoSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, EnderecoSrv) {
    'ngInject';
    
    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.enderecoList)) {
            $scope.cadastro.registro.enderecoList = [];
        }
        if (!$scope.pessoaEnderecoNvg) {
            $scope.pessoaEnderecoNvg = new FrzNavegadorParams($scope.cadastro.registro.enderecoList, 4);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var limpa = function(str) {
        if (!str) {
            return null;
        }
        return str.latinise().replace(/[^a-zA-Z0-9]/g,'').trim().toLowerCase();
    };
    var formataCep = function(numero) {
        if (!numero) {
            return null;
        }
        var cep = new StringMask('00000-000');
        var result = numero.toString().replace(/[^0-9]/g, '').slice(0, 11);
        result = cep.apply(result) || '';
        return result;
    };
    var jaCadastrado = function(conteudo) {
        var i, id, end, igual;
        for (i in $scope.cadastro.registro.enderecoList) {
            id = $scope.cadastro.registro.enderecoList[i].id;
            end = $scope.cadastro.registro.enderecoList[i].endereco;
            if (!end || !conteudo) {
                toastr.error('As informações estão incompletas!');
                return false;
            }
            if (!angular.equals(id, conteudo.chavePrincipal)) {
                if (    (  (isUndefOrNull(end.estado)      && isUndefOrNull(conteudo.estado))      || (end.estado && conteudo.estado && end.estado.id === conteudo.estado.id)) &&
                        (  (isUndefOrNull(end.municipio)   && isUndefOrNull(conteudo.municipio))   || (end.municipio && conteudo.municipio && end.municipio.id === conteudo.municipio.id)) &&
                        (  (isUndefOrNull(end.cidade)      && isUndefOrNull(conteudo.cidade))      || (end.cidade && conteudo.cidade && end.cidade.id === conteudo.cidade.id)) &&
                        (  (isUndefOrNull(end.logradouro)  && isUndefOrNull(conteudo.logradouro))  || (limpa(end.logradouro) === limpa(conteudo.logradouro))) &&
                        (  (isUndefOrNull(end.complemento) && isUndefOrNull(conteudo.complemento)) || (limpa(end.complemento) === limpa(conteudo.complemento))) &&
                        (  (isUndefOrNull(end.numero)      && isUndefOrNull(conteudo.numero))      || (limpa(end.numero) === limpa(conteudo.numero)))) {
                    toastr.error('Registro já cadastrado');
                    return false;
                }
            }
        }
        return true;
    };
    var editarItem = function (destino, item) {
        var endereco = angular.extend({}, item.endereco, {'chavePrincipal': item.id});
        if (endereco.cep) {
            endereco.cep = endereco.cep.toString().replace(/[^0-9]/g, '').slice(0, 11);
        }
        mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', null, endereco, null, jaCadastrado).then(function (conteudo) {
            init();
            // processar o retorno positivo da modal
            conteudo.cep = formataCep(conteudo.cep);
            if (destino) {
                destino = $scope.editarElemento(destino);
                destino.endereco = angular.copy(conteudo);
            } else {
                item.endereco = angular.copy(conteudo);
                $scope.cadastro.registro.enderecoList.push(item);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.definirPrincipalLocal = function (reg, local) {
        if (local === 'P') {
            if ($scope.cadastro.registro && $scope.cadastro.registro.enderecoList) {
                $scope.cadastro.registro.enderecoList.forEach(function(r) {
                    r.principal = 'N';
                });
            }
            $scope.definirPrincipal($scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList, reg);
        } else if (local === 'E') {
            if ($scope.cadastro.registro && $scope.cadastro.registro.publicoAlvo && $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList) {
                $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList.forEach(function(r) {
                    r.principal = 'N';
                });
            }
            $scope.definirPrincipal($scope.cadastro.registro.enderecoList, reg);
        }
    };

    $scope.definirPrincipal = function (lista, reg) {
        if (!lista || !reg) {
            return;
        }
        lista.forEach(function(r) {
            r.principal = angular.equals(reg, r) ? 'S': 'N';
        });
    };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaEnderecoNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        init();
        EnderecoSrv.novo().success(function (resposta) {
            var item = {endereco: resposta.resultado};
            item = $scope.criarElemento($scope.cadastro.registro, 'enderecoList', item);
            editarItem(null, item);
        });
    };
    $scope.editar = function() {
        var item = null;
        var j, i;
        if ($scope.pessoaEnderecoNvg.selecao.tipo === 'U' && $scope.pessoaEnderecoNvg.selecao.item) {
            item = angular.copy($scope.pessoaEnderecoNvg.selecao.item);
            editarItem($scope.pessoaEnderecoNvg.selecao.item, item);
        } else if ($scope.pessoaEnderecoNvg.selecao.items && $scope.pessoaEnderecoNvg.selecao.items.length) {
            for (i in $scope.pessoaEnderecoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.enderecoList) {
                    if (angular.equals($scope.pessoaEnderecoNvg.selecao.items[i].id, $scope.cadastro.registro.enderecoList[j].id)) {
                        item = angular.copy($scope.cadastro.registro.enderecoList[j]);
                        editarItem($scope.cadastro.registro.enderecoList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.enderecoList, ['@jsonId']);
            if ($scope.pessoaEnderecoNvg.selecao.tipo === 'U' && $scope.pessoaEnderecoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'enderecoList', $scope.pessoaEnderecoNvg.selecao.item);
            } else if ($scope.pessoaEnderecoNvg.selecao.items && $scope.pessoaEnderecoNvg.selecao.items.length) {
                for (i in $scope.pessoaEnderecoNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'enderecoList', $scope.pessoaEnderecoNvg.selecao.items[i]);
                }
            }
            $scope.pessoaEnderecoNvg.selecao.item = null;
            $scope.pessoaEnderecoNvg.selecao.items = [];
            $scope.pessoaEnderecoNvg.selecao.selecionado = false;
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

})('pessoa', 'PessoaEnderecoCtrl', 'Endereco vinculado à pessoa');