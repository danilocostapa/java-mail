package enviando.email;


public class AppTest {
	
	@org.junit.Test // não precisamos criar tela, clicar no botao para enviar o email - mesma coisa de usar o metodo main
	public void testEmail() throws Exception{
		
		StringBuilder stringBuilderTextoEmail = new StringBuilder(); // para enviar com HTML
		stringBuilderTextoEmail.append("Olá, <br/><br/>");
		stringBuilderTextoEmail.append("Você está recebendo meu email enviado com JAVA puro e personalizado com HTML!<br/><br/>");
		stringBuilderTextoEmail.append("Espero que goste da minha estilização!<br/><br/>");
		
		stringBuilderTextoEmail.append("<b>Login:<b/> danilo@teste.com.br<br/><br/>");
		stringBuilderTextoEmail.append("<b>Senha:<b/> senhateste123<br/><br/>");
		
		stringBuilderTextoEmail.append("<a target=\"_blank\"href=\"https://www.facebook.com/danilo.costa.355744\" style=\"color:#2525a7; padding:14px 25px; text-aligne:center; text-decoration:none; display:inline-block; border-radius:30px; font-size:20px; font-family:courier; border:3px solid green; background-color:#99DA39;\">Para acessar meu perfil, click aqui!<br/><br/>");
		
		stringBuilderTextoEmail.append("<span style=\"font-size:8px;\">Ass: Danilo Costa<span/>");
		
		/*instanciar o objeto email*/
		
		ObjetoEnviaEmail enviaEmail = new ObjetoEnviaEmail("userjavaestudos@gmail.com", 
															"Danilo Costa", 
															"Teste de envio de email com Java", 
															stringBuilderTextoEmail.toString());
		
		enviaEmail.enviarEmailAnexo(true);
	}
}
