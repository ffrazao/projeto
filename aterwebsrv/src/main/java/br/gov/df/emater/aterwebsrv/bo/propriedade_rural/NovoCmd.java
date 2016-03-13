package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralSituacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Area;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

@Service("PropriedadeRuralNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRural result = new PropriedadeRural();
		
		result.setSituacao(PropriedadeRuralSituacao.A);
		
		Endereco endereco = new Endereco();
		
		WKTReader wktReader = new WKTReader();
		endereco.setEntradaPrincipal((Point) wktReader.read("POINT (-15.732687616157767 -47.90378594955473)"));
		// endereco.setLatitude(new BigDecimal("-15.732687616157767"));
		// endereco.setLongitude(new BigDecimal("-47.90378594955473"));
		
		Polygon geom = (Polygon) wktReader.read("POLYGON((-15.732687616157767 -47.90378594955473, -15.7 -47.90378594955473, -15.732687616157767 -47.9, -15.732687616157767 -47.90378594955473))");
		
		Area area = new Area();
		area.setPoligono(geom);
		List<Area> areaList = new ArrayList<Area>();
		areaList.add(area);
		endereco.setAreaList(areaList);
		
		result.setEndereco(endereco);

		contexto.setResposta(result);

		return true;
	}

}