/* global moment */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        'ngInject';
        
        var UsuarioSrv = {
            funcionalidade: 'USUARIO',
            endereco: $rootScope.servicoUrl + '/usuario',
            abrir : function(scp) {
              
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'PessoaTipo',
                   'PessoaSituacao',
                   'UnidadeOrganizacional',
                   'Situacao',
                   'Confirmacao',
                   'UsuarioStatusConta',
                   'Perfil',
                ]}).success(function(resposta) {
                    if (resposta && resposta.mensagem === 'OK') {
                        var i = 0;
                        scp.cadastro.apoio.pessoaTipoList            = angular.isArray(scp.cadastro.apoio.pessoaTipoList) ? angular.merge(scp.cadastro.apoio.pessoaTipoList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.pessoaSituacaoList        = angular.isArray(scp.cadastro.apoio.pessoaSituacaoList) ? angular.merge(scp.cadastro.apoio.pessoaSituacaoList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.unidadeOrganizacionalList = angular.isArray(scp.cadastro.apoio.unidadeOrganizacionalList) ? angular.merge(scp.cadastro.apoio.unidadeOrganizacionalList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.situacaoList              = angular.isArray(scp.cadastro.apoio.situacaoList) ? angular.merge(scp.cadastro.apoio.situacaoList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList           = angular.isArray(scp.cadastro.apoio.confirmacaoList) ? angular.merge(scp.cadastro.apoio.confirmacaoList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.usuarioStatusContaList    = angular.isArray(scp.cadastro.apoio.usuarioStatusContaList) ? angular.merge(scp.cadastro.apoio.usuarioStatusContaList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.perfilList                = angular.isArray(scp.cadastro.apoio.perfilList) ? angular.merge(scp.cadastro.apoio.perfilList, resposta.resultado[i++]) : resposta.resultado[i++];
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
            novo : function(modelo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/novo', modelo);
            },
            incluir : function(usuario) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', usuario);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(usuario) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', usuario);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return UsuarioSrv;
    }
]);

})('principal', 'UsuarioSrv');

