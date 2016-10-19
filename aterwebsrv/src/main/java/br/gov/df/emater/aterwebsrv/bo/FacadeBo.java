package br.gov.df.emater.aterwebsrv.bo;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.chain.Command;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.gov.df.emater.aterwebsrv.dao.sistema.LogAcaoDao;
import br.gov.df.emater.aterwebsrv.dto.ater.ComunidadeListaDto;
import br.gov.df.emater.aterwebsrv.dto.ater.PropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.ater.PublicoAlvoPropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.funcional.UnidadeOrganizacionalCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.BemClassificacaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.BemProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.pessoa.CarteiraProdutorRelFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.pessoa.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesaApoioDto;
import br.gov.df.emater.aterwebsrv.dto.sistema.FuncionalidadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.sistema.LogAcaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.sistema.PerfilCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.sistema.UsuarioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Comando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class FacadeBo implements BeanFactoryAware {

	@Autowired
	private LogAcaoDao _logAcaoDao;

	private BeanFactory beanFactory;

	public _Contexto _executar(Principal usuario, String comandoNome) throws Exception {
		return this._executar(usuario, comandoNome, null);
	}

	public _Contexto _executar(Principal usuario, String comandoNome, Object requisicao) throws Exception {
		Command comando = (Command) this.beanFactory.getBean(comandoNome);
		_Contexto result = new _Contexto(usuario, comando.getClass().getName(), requisicao);
		comando.execute(result);
		return result;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void _logAcaoSalvar(LogAcao logAcao) throws Exception {
		logAcao.setRequisicao(UtilitarioString.limitarTextoEm(logAcao.getRequisicao(), 16777214));
		logAcao.setResposta(UtilitarioString.limitarTextoEm(logAcao.getResposta(), 16777214));
		logAcao.setErro(UtilitarioString.limitarTextoEm(logAcao.getErro(), 16777214));
		_logAcaoDao.save(logAcao);
	}

	@Transactional
	public _Contexto atividadeExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "AtividadeExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto atividadeFiltroExecutar(Principal usuario, AtividadeCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "AtividadeFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto atividadeFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "AtividadeFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto atividadeNovo(Principal usuario, Atividade atividade) throws Exception {
		return this._executar(usuario, "AtividadeNovoCh", atividade);
	}

	@Transactional
	public _Contexto atividadeSalvar(Principal usuario, Atividade atividade) throws Exception {
		return this._executar(usuario, "AtividadeSalvarCh", atividade);
	}

	@Transactional(readOnly = true)
	public _Contexto atividadeVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "AtividadeVisualizarCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto bemClassificacaoFiltroExecutar(Principal usuario, BemClassificacaoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "BemClassificacaoFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto bemProducaoFiltroExecutar(Principal usuario, BemProducaoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "BemProducaoFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoBemClassificacaoMatriz(Principal usuario) throws Exception {
		return this._executar(usuario, "IndiceProducaoBemClassificacaoMatrizCmd");
	}

	// Comunidade Lista
	@Transactional(readOnly = true)
	public _Contexto comunidadeLista(Principal usuario, ComunidadeListaDto filtro) throws Exception {
		return this._executar(usuario, "ComunidadeListaCmd", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto dominio(Principal usuario, String[] ent, String npk, String vpk, String order, String[] fetchs, String nomeEnum) throws Exception {
		Map<String, Object> requisicao = new HashMap<String, Object>();
		requisicao.put("entidade", ent);
		requisicao.put("nomeChavePrimaria", npk);
		requisicao.put("valorChavePrimaria", vpk);
		requisicao.put("order", order);
		requisicao.put("fetchs", fetchs);
		requisicao.put("nomeEnum", nomeEnum);

		return this._executar(usuario, "DominioCh", requisicao);
	}

	@Transactional(readOnly = true)
	public _Contexto enderecoNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "EnderecoNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto formularioColetaFiltroExecutar(Principal usuario, FormularioColetaCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "FormularioFiltrarComColetaCh", filtro);
	}

	@Transactional
	public _Contexto formularioColetar(Principal usuario, Coleta coleta) throws Exception {
		return this._executar(usuario, "FormularioColetarCh", coleta);
	}

	@Transactional
	public _Contexto formularioExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "FormularioExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto formularioFiltroExecutar(Principal usuario, FormularioCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "FormularioFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto formularioFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "FormularioFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto formularioNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "FormularioNovoCh");
	}

	@Transactional
	public _Contexto formularioSalvar(Principal usuario, Formulario formulario) throws Exception {
		return this._executar(usuario, "FormularioSalvarCh", formulario);
	}

	@Transactional(readOnly = true)
	public _Contexto formularioVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "FormularioVisualizarCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto formularioVisualizarPorCodigo(Principal usuario, String codigo, String posicao) throws Exception {
		Map<String, Object> requisicao = new HashMap<String, Object>();
		requisicao.put("codigo", codigo);
		requisicao.put("posicao", posicao);
		return this._executar(usuario, "FormularioVisualizarPorCodigoCh", requisicao);
	}

	@Transactional
	public _Contexto funcionalidadeExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "FuncionalidadeExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto funcionalidadeFiltroExecutar(Principal usuario, FuncionalidadeCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "FuncionalidadeFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto funcionalidadeFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "FuncionalidadeFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto funcionalidadeNovo(Principal usuario, Funcionalidade funcionalidade) throws Exception {
		return this._executar(usuario, "FuncionalidadeNovoCh", funcionalidade);
	}

	@Transactional
	public _Contexto funcionalidadeSalvar(Principal usuario, Funcionalidade funcionalidade) throws Exception {
		return this._executar(usuario, "FuncionalidadeSalvarCh", funcionalidade);
	}

	@Transactional
	public _Contexto funcionalidadeSalvarComando(Principal usuario, Comando comando) throws Exception {
		return this._executar(usuario, "FuncionalidadeSalvarComandoCh", comando);
	}

	@Transactional
	public _Contexto funcionalidadeSalvarModulo(Principal usuario, Modulo modulo) throws Exception {
		return this._executar(usuario, "FuncionalidadeSalvarModuloCh", modulo);
	}

	@Transactional(readOnly = true)
	public _Contexto funcionalidadeVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "FuncionalidadeVisualizarCh", id);
	}

	@Transactional
	public _Contexto indiceProducaoExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "IndiceProducaoExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoFiltroExecutar(Principal usuario, IndiceProducaoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "IndiceProducaoFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "IndiceProducaoFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoFiltroProducaoPropriedadeRural(Principal usuario, IndiceProducaoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "IndiceProducaoFiltrarProducaoPropriedadeRuralCmd", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoFiltroProducaoPublicoAlvo(Principal usuario, IndiceProducaoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "IndiceProducaoFiltrarProducaoPublicoAlvoCmd", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoNovo(Principal usuario, ProducaoProprietario producaoProprietario) throws Exception {
		return this._executar(usuario, "IndiceProducaoNovoCh", producaoProprietario);
	}

	@Transactional
	public _Contexto indiceProducaoSalvar(Principal usuario, ProducaoProprietario producaoProprietario) throws Exception {
		return this._executar(usuario, "IndiceProducaoSalvarCh", producaoProprietario);
	}

	@Transactional(readOnly = true)
	public _Contexto indiceProducaoVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "IndiceProducaoVisualizarCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto logAcaoFiltroExecutar(Principal usuario, LogAcaoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "LogAcaoFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto logAcaoFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "LogAcaoFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto logAcaoNovo(Principal usuario, LogAcao logAcao) throws Exception {
		return this._executar(usuario, "LogAcaoNovoCh", logAcao);
	}

	@Transactional
	public _Contexto logAcaoSalvar(Principal usuario, LogAcao logAcao) throws Exception {
		return this._executar(usuario, "LogAcaoSalvarCh", logAcao);
	}

	@Transactional
	public _Contexto logAcaoSalvarComando(Principal usuario, Comando comando) throws Exception {
		return this._executar(usuario, "LogAcaoSalvarComandoCh", comando);
	}

	@Transactional
	public _Contexto logAcaoSalvarModulo(Principal usuario, Modulo modulo) throws Exception {
		return this._executar(usuario, "LogAcaoSalvarModuloCh", modulo);
	}

	@Transactional(readOnly = true)
	public _Contexto logAcaoVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "LogAcaoVisualizarCh", id);
	}

	@Transactional
	public _Contexto perfilExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PerfilExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto perfilFiltroExecutar(Principal usuario, PerfilCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PerfilFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto perfilFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PerfilFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto perfilNovo(Principal usuario, Perfil perfil) throws Exception {
		return this._executar(usuario, "PerfilNovoCh", perfil);
	}

	@Transactional
	public _Contexto perfilSalvar(Principal usuario, Perfil perfil) throws Exception {
		return this._executar(usuario, "PerfilSalvarCh", perfil);
	}

	@Transactional
	public _Contexto perfilSalvarComando(Principal usuario, Comando comando) throws Exception {
		return this._executar(usuario, "PerfilSalvarComandoCh", comando);
	}

	@Transactional
	public _Contexto perfilSalvarModulo(Principal usuario, Modulo modulo) throws Exception {
		return this._executar(usuario, "PerfilSalvarModuloCh", modulo);
	}

	@Transactional(readOnly = true)
	public _Contexto perfilVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PerfilVisualizarCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaBuscarCep(Principal usuario, String cep) throws Exception {
		return this._executar(usuario, "PessoaBuscarCepCmd", cep);
	}

	@Transactional
	public _Contexto pessoaCarteiraProdutorRel(Principal usuario, CarteiraProdutorRelFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PessoaCarteiraProdutorRelCmd", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaCarteiraProdutorVerificar(Principal usuario, CarteiraProdutorRelFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PessoaCarteiraProdutorVerificarCmd", filtro);
	}

	@Transactional
	public _Contexto pessoaExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PessoaExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaFiltroExecutar(Principal usuario, PessoaCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PessoaFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PessoaFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaNovo(Principal usuario, Pessoa pessoa) throws Exception {
		return this._executar(usuario, "PessoaNovoCh", pessoa);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaPublicoAlvoPorPessoaId(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PessoaPublicoAlvoPorPessoaIdCmd", id);
	}

	@Transactional
	public _Contexto pessoaSalvar(Principal usuario, Pessoa pessoa) throws Exception {
		return this._executar(usuario, "PessoaSalvarCh", pessoa);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PessoaVisualizarCh", id);
	}

	@Transactional
	public _Contexto projetoCreditoRuralCalcularCronograma(Principal usuario, ProjetoCreditoRural projetoCreditoRural) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralCalcularCronogramaCh", projetoCreditoRural);
	}

	@Transactional
	public _Contexto projetoCreditoRuralCalcularFluxoCaixa(Principal usuario, ProjetoCreditoRural projetoCreditoRural) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralCalcularFluxoCaixaCh", projetoCreditoRural);
	}

	@Transactional
	public _Contexto projetoCreditoRuralExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto projetoCreditoRuralFiltroExecutar(Principal usuario, ProjetoCreditoRuralCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto projetoCreditoRuralFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto projetoCreditoRuralNovo(Principal usuario, ProjetoCreditoRural projetoCreditoRural) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralNovoCh", projetoCreditoRural);
	}

	@Transactional
	public _Contexto projetoCreditoRuralPlanilhaUpload(Principal usuario, ProjetoCreditoRuralReceitaDespesaApoioDto registro) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralPlanilhaUploadCh", registro);
	}

	@Transactional(readOnly = true)
	public _Contexto projetoCreditoRuralProjetoTecnicoRel(Principal usuario, Integer[] idList) throws Exception {
		return this._executar(usuario, "ProjetoCreditoRuralProjetoTecnicoRelCh", idList);
	}

	@Transactional
	public _Contexto propriedadeRuralExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PropriedadeRuralExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltrarPorPublicoAlvoPropriedadeRuralComunidade(Principal usuario, PublicoAlvoPropriedadeRuralCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltrarPorPublicoAlvoPropriedadeRuralComunidadeCmd", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltroExecutar(Principal usuario, PropriedadeRuralCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PropriedadeRuralNovoCh");
	}

	@Transactional
	public _Contexto propriedadeRuralSalvar(Principal usuario, PropriedadeRural propriedadeRural) throws Exception {
		return this._executar(usuario, "PropriedadeRuralSalvarCh", propriedadeRural);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PropriedadeRuralVisualizarCh", id);
	}

	@Transactional()
	public _Contexto relatorioCompilar(Principal usuario, String nome) throws Exception {
		return this._executar(usuario, "RelatorioCompilarCh", nome);
	}

	@Transactional
	public _Contexto segurancaEsqueciSenha(String email) throws Exception {
		return this._executar(null, "SegurancaEsqueciSenhaCh", email);
	}

	@Transactional
	public _Contexto segurancaRenovarSenha(Usuario usuario) throws Exception {
		return this._executar(null, "SegurancaRenovarSenhaCmd", usuario);
	}

	@Transactional
	public _Contexto segurancaSalvarPerfil(Principal principal, Usuario usuario) throws Exception {
		return this._executar(principal, "SegurancaSalvarPerfilCmd", usuario);
	}

	@Transactional(readOnly = true)
	public _Contexto segurancaVisualizarPerfil(Principal usuario, String username) throws Exception {
		return this._executar(usuario, "SegurancaVisualizarPerfilCmd", username);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Transactional(readOnly = true)
	public _Contexto unidadeOrganizacionalComunidade(Principal usuario, Integer pessoaJuridicaId) throws Exception {
		return this._executar(usuario, "UnidadeOrganizacionalComunidadeCmd", pessoaJuridicaId);
	}

	// Unidade Organizacional Lista
	@Transactional(readOnly = true)
	public _Contexto unidadeOrganizacionalFiltroExecutar(Principal usuario, UnidadeOrganizacionalCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "UnidadeOrganizacionalFiltroExecutarCh", filtro);
	}

	@Transactional
	public _Contexto usuarioExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "UsuarioExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto usuarioFiltroExecutar(Principal usuario, UsuarioCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "UsuarioFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto usuarioFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "UsuarioFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto usuarioNovo(Principal usuario, Usuario usr) throws Exception {
		return this._executar(usuario, "UsuarioNovoCh", usr);
	}

	@Transactional
	public _Contexto usuarioSalvar(Principal usuario, Usuario usr) throws Exception {
		return this._executar(usuario, "UsuarioSalvarCh", usr);
	}

	@Transactional(readOnly = true)
	public _Contexto usuarioVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "UsuarioVisualizarCh", id);
	}

	@Transactional(readOnly = true)
	public void utilArquivoDescer(Principal usuario, String arquivo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> requisicao = new HashMap<String, Object>();
		requisicao.put("usuario", usuario);
		requisicao.put("arquivo", arquivo);
		requisicao.put("request", request);
		requisicao.put("response", response);

		this._executar(usuario, "UtilArquivoDescerCmd", requisicao);
	}

	@Transactional
	public _Contexto utilArquivoSubir(Principal usuario, MultipartFile arquivo, HttpServletRequest request, ArquivoTipo tipo) throws Exception {
		Map<String, Object> requisicao = new HashMap<String, Object>();
		requisicao.put("usuario", usuario);
		requisicao.put("arquivo", arquivo);
		requisicao.put("request", request);
		requisicao.put("tipo", tipo);

		return this._executar(usuario, "UtilArquivoSubirCh", requisicao);
	}

}