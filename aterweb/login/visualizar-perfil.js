/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController, ['$scope', '$uibModalInstance', 'toastr', 'mensagemSrv', 'SegurancaSrv',
function ($scope, $uibModalInstance, toastr, mensagemSrv, SegurancaSrv) {

        $scope.iniciar = function() {
            SegurancaSrv.visualizarPerfil().success(function(resposta) {
                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                    $scope.original = resposta.resultado;
                    $scope.apoio = {pessoaEmailList: []};
                    if ($scope.original.pessoa.emailList) {                    
                        $scope.original.pessoa.emailList.forEach( function(elemento) {
                            $scope.apoio.pessoaEmailList.push(elemento);
                        });
                    }
                    $scope.reiniciar();
                } else {
                    toastr.error(resposta.mensagem, 'Erro ao pesquisar dados do perfil');
                    $uibModalInstance.dismiss('cancel');
                }
            });
        };
        $scope.reiniciar = function() {
          $scope.submitted = false;
          $scope.registro = angular.copy($scope.original);
          if ($scope.$parent.visualizarPerfilForm) {
            $scope.$parent.visualizarPerfilForm.$setPristine();
          }
          $('#username').focus();
        };

        $scope.iniciar();

      // métodos de apoio
      $scope.submitForm = function () {
        if (!$scope.$parent.visualizarPerfilForm.$valid) {
          $scope.submitted = true;
          toastr.error('Verifique os campos marcados', 'Erro');
          return;
        }

        removerCampo($scope.registro, ['@jsonId']);

        SegurancaSrv.salvarPerfil($scope.registro).success(function (resposta) {
            if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                toastr.success('Perfil alterado, faça um novo login', 'Sucesso');
                $uibModalInstance.dismiss('success');
                $scope.executarLogout();
            } else {
                toastr.error(resposta.mensagem, 'Erro ao salvar o perfil');
            }
        });
      };

      $scope.cancelar = function () {
        $uibModalInstance.dismiss('cancel');
      };

    $scope.selecionaFotoPerfil = function() {
        var conf = 
            '<div class="form-group">' +
            '    <div class="row">' +
            '       <div class="col-md-12">' +
            '           <label class="control-label text-center" for="nomeArquivo">Foto do Perfil</label>' +
            '       </div>' +
            '    </div>' +
            '    <div class="row">' +
            '        <frz-arquivo ng-model="conteudo.nomeArquivo" tipo="PERFIL"></frz-arquivo>' +
            '    </div>' + 
            '</div>';
        mensagemSrv.confirmacao(false, conf, null, {
            'nomeArquivo': $scope.registro.perfilArquivo ? $scope.registro.perfilArquivo.localDiretorioWeb : null,
        }).then(function(conteudo) {
            // processar o retorno positivo da modal
            if (!conteudo.nomeArquivo) {
                toastr.error('Nenhum arquivo selecionado', 'Erro ao captar Imagem');
            } else {
                $scope.registro.pessoa.perfilArquivo = {md5: conteudo.nomeArquivo.md5, localDiretorioWeb: conteudo.nomeArquivo.nomeArquivo};
            }
        }, function() {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.verificaDisponibilidadeUsername = function (username) {

        SegurancaSrv.visualizarPerfil(username).success(function(resposta) {
            if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                if (resposta.resultado && resposta.resultado.id !== $scope.registro.id) {
                    toastr.error('Este nome de usuário já está em uso!', 'Erro');
                } else {
                    toastr.success('Nome de usuário disponível!', 'Sucesso');
                }
            } else {
                if (resposta.mensagem === 'Usuário não cadastrado') {
                    toastr.success('Nome de usuário disponível!', 'Sucesso');
                } else {                
                    toastr.error(resposta.mensagem, 'Erro ao pesquisar dados do perfil');
                    $uibModalInstance.dismiss('cancel');
                }
            }
        });

    };
}]);

})('principal', 'VisualizarPerfilCtrl', 'Visualizar Perfil');