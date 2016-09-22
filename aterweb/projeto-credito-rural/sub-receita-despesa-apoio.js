/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'UtilSrv', 'mensagemSrv', '$log', 'ProjetoCreditoRuralSrv',
    function($scope, toastr, UtilSrv, mensagemSrv, $log, ProjetoCreditoRuralSrv) {

    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        
    };
    init();

    $scope.planilhaDownload = function (apoioItem, codigo) {
        var tipoArquivo = apoioItem + '_' + codigo; 
        if ($scope.cadastro.registro.arquivoList) {
            for (var i = 0; i < $scope.cadastro.registro.arquivoList.length; i++) {
                if ($scope.cadastro.registro.arquivoList[i].codigo === tipoArquivo) {
                    return $scope.servicoUrl + '/arquivo-descer?arquivo=' + $scope.cadastro.registro.arquivoList[i].arquivo.md5;
                }
            }
        }
        return 'download/modelo/projeto-credito-rural/evolucaoRebanho' + codigo + '2015.xls';
    };

    $scope.planilhaUpload = function (apoioItem, codigo) {
        var tipoArquivo = apoioItem + '_' + codigo; 
        var conf = 
            '<div class="form-group">' +
            '    <div class="row">' +
            '       <div class="col-md-12">' +
            '           <label class="control-label text-center" for="nomeArquivo">Upload Planilha ' + codigo + ' </label>' +
            '       </div>' +
            '    </div>' +
            '    <div class="row">' +
            '        <frz-arquivo ng-model="conteudo.nomeArquivo" tipo="ARQUIVOS"></frz-arquivo>' +
            '    </div>' + 
            '</div>';
        mensagemSrv.confirmacao(false, conf, null, {}).then(function(conteudo) {
            // processar o retorno positivo da modal
            if (!conteudo.nomeArquivo) {
                toastr.error('Nenhum arquivo selecionado', 'Erro ao captar arquivo');
            } else {
                ProjetoCreditoRuralSrv.planilhaUpload({projetoCreditoRural: $scope.cadastro.registro, arquivo: {md5: conteudo.nomeArquivo[0].md5}, codigo: tipoArquivo}).success(function(resposta) {
                    console.log(resposta);
                    /*
                    if (!$scope.cadastro.registro.arquivoList) {
                        $scope.cadastro.registro.arquivoList = [];
                    }
                    for (var i = 0; i < $scope.cadastro.registro.arquivoList.length; i++) {
                        if ($scope.cadastro.registro.arquivoList[i].codigo === tipoArquivo) {
                            $scope.cadastro.registro.arquivoList[i].arquivo = arquivo;
                            return;
                        }
                    }
                    $scope.cadastro.registro.arquivoList.push({codigo: tipoArquivo, arquivo: arquivo});
                    */
                });
            }
        }, function() {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.planilhaDesconsiderar = function (apoioItem, codigo) {

    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador

    // fim das operaçoes atribuidas ao navagador


} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralReceitaDespesaApoioCtrl', 'Apoio à Receitas e Despesas do Projeto de Crédito Rural');