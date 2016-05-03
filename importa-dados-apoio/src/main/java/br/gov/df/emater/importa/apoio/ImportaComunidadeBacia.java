package br.gov.df.emater.importa.apoio;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.gov.df.emater.importa.Importador;

public class ImportaComunidadeBacia extends Importador {

	public ImportaComunidadeBacia(Connection con) {
		super(con);
	}

	@Override
	public void executar() throws Exception {
		File tempDir = criarTempDir();

		List<Map<String, Object>> mapa = criarMapaDoArquivoExcel(new File(tempDir, "comunidade.xls"), 0);

		for (Map<String, Object> linha : mapa) {
			Integer IDCOM = ((Double) linha.get("IDCOM")).intValue();
			String COMUNIDADE = (String) linha.get("COMUNIDADE");
			String BACIA = (String) linha.get("BACIA");
			String REGIAO = (String) linha.get("REGIAO");

			int comunidadeId = atualiza("comunidade", "unidade_organizacional", 1, COMUNIDADE, IDCOM);
			int baciaHidrograficaId = atualiza("bacia_hidrografica", null, null, BACIA, null);

			vincula(comunidadeId, baciaHidrograficaId);

			atualizaCidade(comunidadeId, REGIAO);

		}
		System.out.println("Fim!");
	}

	private void atualizaCidade(int comunidadeId, String rEGIAO) throws Exception {
		try (PreparedStatement ps = con.prepareStatement("select id from pessoa.cidade where nome like ? and municipio_id = 1")) {
			ps.setString(1, rEGIAO);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int cidade = rs.getInt(1);
					try (PreparedStatement psUpd = con.prepareStatement("update ater.comunidade set cidade_id = ? where id = ?")) {
						psUpd.setInt(1, cidade);
						psUpd.setInt(2, comunidadeId);
						psUpd.executeUpdate();
					}
				} else {
					System.out.format("Não Encontrado [%s]\n", rEGIAO);
				}
			}
		}
	}

	private void vincula(int comunidadeId, int baciaHidrograficaId) {
		try {
			PreparedStatement ps = con.prepareStatement("insert into ater.comunidade_bacia_hidrografica (comunidade_id, bacia_hidrografica_id) values (?, ?)");
			ps.setInt(1, comunidadeId);
			ps.setInt(2, baciaHidrograficaId);
			ps.execute();
		} catch (Exception e) {
			System.out.format("já vinculado! [%d] - [%d]\n", comunidadeId, baciaHidrograficaId);
		}
	}

	protected Integer atualiza(String tabela, String colunaPai, Integer ancestralId, String nome, Integer codigo) throws SQLException {
		Integer result = null;

		if (nome == null || nome.trim().length() == 0) {
			return null;
		}

		StringBuilder sql;

		sql = new StringBuilder();
		sql.append("select id").append("\n");
		sql.append("from ater.").append(tabela).append("\n");
		sql.append("where nome like ?").append("\n");
		if (colunaPai != null) {
			sql.append("  and ").append(colunaPai).append("_id = ?").append("\n");
		}
		PreparedStatement selectPs = con.prepareStatement(sql.toString());

		sql = new StringBuilder();
		sql.append("update ater.").append(tabela).append("\n");
		sql.append("set nome = ?").append("\n");
		sql.append("   ,codigo");
		if ("comunidade".equals(tabela)) {
			sql.append("_sisater");
		}
		sql.append(" = ?").append("\n");
		sql.append("where id = ?").append("\n");
		PreparedStatement updatePs = con.prepareStatement(sql.toString());

		sql = new StringBuilder();
		sql.append("insert into ater.").append(tabela).append("\n");
		sql.append("   (nome, codigo");
		if ("comunidade".equals(tabela)) {
			sql.append("_sisater");
		}

		if (colunaPai != null) {
			sql.append(", ").append(colunaPai).append("_id\n");
		}
		sql.append(") values").append("\n");
		sql.append("   (?, ?").append("\n");
		if (colunaPai != null) {
			sql.append(", ?").append("\n");
		}
		sql.append(")").append("\n");
		PreparedStatement insertPs = con.prepareStatement(sql.toString());

		do {
			selectPs.setString(1, nome);
			if (ancestralId != null) {
				selectPs.setInt(2, ancestralId);
			}
			ResultSet rs = selectPs.executeQuery();

			PreparedStatement ps = null;
			int i = 0;
			if (rs.next()) {
				result = rs.getInt(1);

				ps = updatePs;

				ps.setString(++i, nome);
				ps.setBigDecimal(++i, codigo == null ? null : new BigDecimal(codigo));
				ps.setInt(++i, result);
			} else {
				ps = insertPs;

				ps.setString(++i, nome);
				ps.setBigDecimal(++i, codigo == null ? null : new BigDecimal(codigo));
				if (colunaPai != null) {
					ps.setInt(++i, ancestralId);
				}
			}
			ps.execute();
		} while (result == null);

		return result;
	}

}
