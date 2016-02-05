(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'FormularioSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, FormularioSrv) {
        var PropriedadeRuralSrv = {
            funcionalidade: 'PROPRIEDADE_RURAL',
            endereco: $rootScope.servicoUrl + '/propriedade-rural',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'UnidadeOrganizacional',
                   'Comunidade',
                   'SituacaoFundiaria',
                   'AreaUtil',
                   'SistemaProducao',
                   'Confirmacao',
                   'PropriedadeRuralVinculoTipo',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.unidadeOrganizacionalList = resposta.resultado[0];
                        scp.cadastro.apoio.comunidadeList = resposta.resultado[1];
                        scp.cadastro.apoio.situacaoFundiariaList = resposta.resultado[2];
                        scp.cadastro.apoio.areaUtilList = resposta.resultado[3];
                        scp.cadastro.apoio.sistemaProducaoList = resposta.resultado[4];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[5];
                        scp.cadastro.apoio.propriedadeRuralVinculoTipoList = resposta.resultado[6];
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
            novo : function(propriedadeTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo');
            },
            incluir : function(propriedade) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', propriedade);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(propriedade) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', propriedade);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },

            // funcoes especiais
            formularioFiltrarComColeta : function(filtro) {
                return FormularioSrv.filtrarComColeta(filtro);
            },
            filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade : function (filtro) {
                return $http.post(this.endereco + '/filtrar-por-publico-alvo-unidade-organizacional-comunidade', filtro);
            }

        };
        return PropriedadeRuralSrv;
    }
]);

})('principal', 'PropriedadeRuralSrv');