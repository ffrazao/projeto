package br.gov.df.emater.aterwebsrv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AterWebApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AterWebApplication.class, args);
	}

	// @Bean
	// CommandLineRunner init() {
	// return (String[] args) -> {
	// System.out.println(Criptografia.MD5("1a"));
	// };
	// }

	// @Bean
	// public InitializingBean insertDefaultUsers() {
	// return new InitializingBean() {
	// // @Autowired
	// // private UserRepository userRepository;
	//
	// private void addUser(String username, String password) {
	// Usuario user = new Usuario();
	// user.setUsername(username);
	// user.setPassword(new BCryptPasswordEncoder().encode(password));
	// // user.grantRole(username.equals("admin") ? UserRole.ADMIN :
	// // UserRole.USER);
	// // userRepository.save(user);
	// }
	//
	// @Override
	// public void afterPropertiesSet() {
	// addUser("admin", "admin");
	// addUser("user", "user");
	// }
	// };
	// }
}