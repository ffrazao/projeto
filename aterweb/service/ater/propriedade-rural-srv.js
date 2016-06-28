(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'FormularioSrv', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, FormularioSrv, ComunidadeSrv) {
        var PropriedadeRuralSrv = {
            funcionalidade: 'PROPRIEDADE_RURAL',
            endereco: $rootScope.servicoUrl + '/propriedade-rural',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'UnidadeOrganizacional',
                   'Comunidade',
                   'ComunidadeBaciaHidrografica',
                   'SituacaoFundiaria',
                   'AreaUtil',
                   'SistemaProducao',
                   'Confirmacao',
                   'PropriedadeRuralVinculoTipo',
                   'FormaUtilizacaoEspacoRural',
                   'PendenciaTipo',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        var i = 0;
                        scp.cadastro.apoio.unidadeOrganizacionalList = resposta.resultado[i++];
                        scp.cadastro.apoio.comunidadeList = resposta.resultado[i++];
                        scp.cadastro.apoio.comunidadeBaciaHidrograficaList = resposta.resultado[i++];
                        scp.cadastro.apoio.situacaoFundiariaList = resposta.resultado[i++];
                        scp.cadastro.apoio.areaUtilList = resposta.resultado[i++];
                        scp.cadastro.apoio.sistemaProducaoList = resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[i++];
                        scp.cadastro.apoio.propriedadeRuralVinculoTipoList = resposta.resultado[i++];
                        scp.cadastro.apoio.formaUtilizacaoEspacoRuralList = resposta.resultado[i++];
                        scp.cadastro.apoio.pendenciaTipoList = resposta.resultado[i++];

                        scp.cadastro.apoio.baciaHidrograficaList = [];

                        var involucro = [];
                        scp.cadastro.apoio.formaUtilizacaoEspacoRuralList.forEach(function(elemento) {
                          delete elemento['@jsonId'];
                          involucro.push({'formaUtilizacaoEspacoRural': elemento});
                        });
                        scp.cadastro.apoio.formaUtilizacaoEspacoRuralList = involucro;
                    }
                });
                if ($rootScope.isAuthenticated()) {
                    var t = $rootScope.token;

                    scp.cadastro.apoio.localList = [];
                    var fltr = null;
                    if (scp.cadastro.apoio.unidadeOrganizacionalSomenteLeitura) {
                        fltr = {unidadeOrganizacionalList: scp.cadastro.filtro.unidadeOrganizacionalList};
                    } else {
                        fltr = {pessoaJuridicaList: (t && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) ? [angular.fromJson(t.lotacaoAtual.pessoaJuridica.id)] : null};
                    }
                    ComunidadeSrv.lista(fltr, scp.cadastro.apoio.localList, t);

                    if (!t || !t.lotacaoAtual || !t.lotacaoAtual.pessoaJuridica) {
                        toastr.warning('Não foi possível identificar a sua lotação', 'Erro ao carregar os dados');
                    }
                }
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
            excluir : function(propriedade) {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
                return $http.delete(this.endereco + '/excluir', {params: {'id': propriedade.id}});
            },

            // funcoes especiais
            formularioFiltrarComColeta : function(filtro) {
                return FormularioSrv.filtrarComColeta(filtro);
            },
            filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade : function (filtro) {
                return $http.post(this.endereco + '/filtro-publico-alvo-propriedade-rural-comunidade', filtro);
            }

        };
        return PropriedadeRuralSrv;
    }
]);

})('principal', 'PropriedadeRuralSrv');