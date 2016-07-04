package br.gov.df.emater.aterwebsrv;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AterWebApplication extends SpringBootServletInitializer {

	protected final Log logger = LogFactory.getLog(getClass());

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AterWebApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return (String[] args) -> {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			logger.debug("BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);");
		};
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