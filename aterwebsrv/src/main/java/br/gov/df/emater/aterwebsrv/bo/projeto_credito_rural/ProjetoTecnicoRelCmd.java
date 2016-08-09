package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

@Service("ProjetoCreditoRuralProjetoTecnicoRelCmd")
public class ProjetoTecnicoRelCmd extends _Comando {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@Autowired
	private _Relatorio relatorio;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer[] idList = (Integer[]) contexto.getRequisicao();

		if (ArrayUtils.isEmpty(idList)) {
			throw new BoException("Nenhum projeto informado");
		}

		List<ProjetoCreditoRural> lista = dao.findAll(Arrays.asList(idList));
		
		if (CollectionUtils.isEmpty(lista)) {
			throw new BoException("Nenhum projeto encontrado com os parâmetros informados");
		}
		
		List<byte[]> resultList = new ArrayList<>();
		for (ProjetoCreditoRural projeto: lista) {
			
			List<ProjetoCreditoRural> pList = new ArrayList<>();
			pList.add(projeto);
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("Usuario", getUsuario(contexto.getUsuario().getName()));
			parametros.put("RelatorioNome", "PROJETO TÉCNICO");
			
			JasperPrint principal = relatorio.montarRelatorio("projeto_credito_rural/ProjetoTecnicoRel", parametros, pList);
			
			parametros.put("RelatorioNome", "CAPA");
			JasperPrint capa = relatorio.montarRelatorio("projeto_credito_rural/Capa", parametros, pList);
			

			// montar o resultado final
			for (JRPrintPage pagina : capa.getPages()) {
				principal.addPage(pagina);
			}

			resultList.add(relatorio.imprimir(principal));
		}
		
		byte[] result = null;

		int i = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		for (ProjetoCreditoRural projeto: lista) {
			ZipEntry entry = new ZipEntry(projeto.getAtividade().getCodigo().concat(".pdf"));
			byte[] arquivo = resultList.get(i++);
			entry.setSize(arquivo.length);
			zos.putNextEntry(entry);
			zos.write(arquivo);
			zos.closeEntry();
		}
		zos.close();
		result = baos.toByteArray();
		
		contexto.setResposta(result);

		return false;
	}

}
