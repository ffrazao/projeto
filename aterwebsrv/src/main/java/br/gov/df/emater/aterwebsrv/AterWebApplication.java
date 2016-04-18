package br.gov.df.emater.aterwebsrv;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@SpringBootApplication
public class AterWebApplication {

	public static void main(String[] args) throws Exception {		
//		System.out.println(Criptografia.MD5("1a"));
		SpringApplication.run(AterWebApplication.class, args);
	}

	@Bean
	public InitializingBean insertDefaultUsers() {
		return new InitializingBean() {
			// @Autowired
			// private UserRepository userRepository;

			private void addUser(String username, String password) {
				Usuario user = new Usuario();
				user.setUsername(username);
				user.setPassword(new BCryptPasswordEncoder().encode(password));
				// user.grantRole(username.equals("admin") ? UserRole.ADMIN :
				// UserRole.USER);
				// userRepository.save(user);
			}

			@Override
			public void afterPropertiesSet() {
				addUser("admin", "admin");
				addUser("user", "user");
			}
		};
	}
}