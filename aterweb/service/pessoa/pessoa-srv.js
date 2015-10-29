(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv) {
        var PessoaSrv = {
            funcionalidade: 'PESSOA',
            endereco: $rootScope.servicoUrl + '/pessoa',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'PessoaTipo',
                   'PessoaGenero',
                   'PessoaGeracao',
                   'PessoaSituacao',
                   'PublicoAlvoSegmento',
                   'PublicoAlvoCategoria',
                   'Setor',
                   'Comunidade',
                   'Confirmacao',
                   'ConfirmacaoDap',
                   'Pais',
                   'Escolaridade',
                   'Profissao',
                   'EstadoCivil',
                   'RegimeCasamento',
                   'PessoaNacionalidade',
                   'CamOrgao',
                   'ConfirmacaoDap',
                   'CnhCategoria',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.pessoaTipoList = resposta.resultado[0];
                        scp.cadastro.apoio.generoList = resposta.resultado[1];
                        scp.cadastro.apoio.pessoaGeracaoList = resposta.resultado[2];
                        scp.cadastro.apoio.pessoaSituacaoList = resposta.resultado[3];
                        scp.cadastro.apoio.publicoAlvoSegmentoList = resposta.resultado[4];
                        scp.cadastro.apoio.publicoAlvoCategoriaList = resposta.resultado[5];
                        scp.cadastro.apoio.setorList = resposta.resultado[6];
                        scp.cadastro.apoio.comunidadeList = resposta.resultado[7];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[8];
                        scp.cadastro.apoio.confirmacaoDapList = resposta.resultado[9];
                        scp.cadastro.apoio.paisList = resposta.resultado[10];
                        scp.cadastro.apoio.escolaridadeList = resposta.resultado[11];
                        scp.cadastro.apoio.profissaoList = resposta.resultado[12];
                        scp.cadastro.apoio.estadoCivilList = resposta.resultado[13];
                        scp.cadastro.apoio.regimeCasamentoList = resposta.resultado[14];
                        scp.cadastro.apoio.nacionalidadeList = resposta.resultado[15];
                        scp.cadastro.apoio.camOrgaoList = resposta.resultado[16];
                        scp.cadastro.apoio.confirmacaoDapList = resposta.resultado[17];
                        scp.cadastro.apoio.cnhCategoriaList = resposta.resultado[18];
                    }
                });
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(pessoaTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo', {params: {modelo: pessoaTipo}});
            },
            incluir : function(pessoa) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', pessoa);
            },
            visualizar : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
            },
            editar : function(pessoa) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', pessoa);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return PessoaSrv;
    }
]);

})('principal', 'PessoaSrv');