package enviando.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ObjetoEnviaEmail {
	
	/*atributos*/
	private String userName = "daniloestudos3@gmail.com";
	private String senha = "zxwmwxtelefbwtav";
	
	private String listaDestinatarios = "";
	private String nomeRemetente = "";
	
	private String assuntoEmail = "";
	private String textoEmail = "";
	
	/*construtor*/
	public ObjetoEnviaEmail(String listaDestinatarios, String nomeRemetente, String assuntoEmail, String textoEmail) {
		this.listaDestinatarios = listaDestinatarios;
		this.nomeRemetente = nomeRemetente;
		this.assuntoEmail = assuntoEmail;
		this.textoEmail = textoEmail;
	}
	
	public void enviarEmail(boolean envioHtml) throws Exception{
		
		/*olhe as configurações do smtp do seu email - no meu caso é gmail*/
		
		/*configurando as propriedades*/
		Properties properties = new Properties();
		
		properties.put("mail.smtp.ssl.trust", "*"); // autenticar com segurança ssl
		properties.put("mail.smtp.auth", "true"); // autorização
		properties.put("mail.smtp.starttls", "true"); // autenticação
		properties.put("mail.smtp.host", "smtp.gmail.com"); // servidor do gmail Google
		properties.put("mail.smtp.port", "465"); // porta do servidor
		properties.put("mail.smtp.socketFactory.port", "465"); // especifica a porta a ser conectada pelo socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // Classe socket de conexão ao SMTP
		
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(userName, senha);
			}
		});
		
		/*destinatários*/
		Address[] toUser = InternetAddress.parse(listaDestinatarios);
		
		Message message = new MimeMessage(session);
		
		message.setFrom(new InternetAddress(userName, nomeRemetente)); /*quem está enviando*/
		message.setRecipients(Message.RecipientType.TO, toUser); /*email de destino*/
		message.setSubject(assuntoEmail); /*assunto do email*/
		
		/*condição para enviar com HTML*/
		if(envioHtml) {
			message.setContent(textoEmail, "text/html; charset=utf-8");
			Transport.send(message);
		}
		else {
			message.setText(textoEmail); /*corpo do email*/
			Transport.send(message);
			
		}
		
	}
	
	public void enviarEmailAnexo(boolean envioHtml) throws Exception{
		
		/*olhe as configurações do smtp do seu email - no meu caso é gmail*/
		
		/*configurando as propriedades*/
		Properties properties = new Properties();
		
		properties.put("mail.smtp.ssl.trust", "*"); // autenticar com segurança ssl
		properties.put("mail.smtp.auth", "true"); // autorização
		properties.put("mail.smtp.starttls", "true"); // autenticação
		properties.put("mail.smtp.host", "smtp.gmail.com"); // servidor do gmail Google
		properties.put("mail.smtp.port", "465"); // porta do servidor
		properties.put("mail.smtp.socketFactory.port", "465"); // especifica a porta a ser conectada pelo socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // Classe socket de conexão ao SMTP
		
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(userName, senha);
			}
		});
		
		/*destinatários*/
		Address[] toUser = InternetAddress.parse(listaDestinatarios);
		
		Message message = new MimeMessage(session);
		
		message.setFrom(new InternetAddress(userName, nomeRemetente)); /*quem está enviando*/
		message.setRecipients(Message.RecipientType.TO, toUser); /*email de destino*/
		message.setSubject(assuntoEmail); /*assunto do email*/
		
		/*Parte 1 do email que é o texto e descrição*/
		MimeBodyPart corpoEmail = new MimeBodyPart();
		
		/*condição para enviar com HTML*/
		if(envioHtml) {
			corpoEmail.setContent(textoEmail, "text/html; charset=utf-8");
		}
		else {
			corpoEmail.setText(textoEmail); /*corpo do email*/
		}
		
		/*Criando lista de PDF*/
		List<FileInputStream> arquivos = new ArrayList<FileInputStream>();
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		
		
		Multipart multipart = new MimeMultipart();/*criando o corpo do email*/
		multipart.addBodyPart(corpoEmail);
		
		int index = 0;
		
		for (FileInputStream fileInputStream : arquivos) {
			
			/*parte 2 do email, que são os anexos em PDF ou qq outra coisa*/
			MimeBodyPart anexoEmail = new MimeBodyPart();
			
			/*onde é passado o simulador de pdf, passa o arquivo gravado no Bd ou qq outro loacl*/
			anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(fileInputStream, "application/pdf")));
			anexoEmail.setFileName("anexoEmail "+(index + 1)+".pdf");
			
			// juntando a parte 1 com a parte 2
			
			multipart.addBodyPart(anexoEmail);
			
			index++;
			
		}
		
		
		message.setContent(multipart); // setando para o envio
		
		Transport.send(message);
		
	}
	
	/*Esse método simula p PDF ou qualquer arquivo que possa ser enviado no anexo do email
	 * Voce pode pegar o arquivo no banco de dados base64, byte[], Stream de Arquivos
	 * pode estar em um banco de dados, ou em uma pasta
	 * retorna um PDF em branco com o texto do paragrafo de exemplo.
	 * */
	private FileInputStream simuladorDePDF() throws Exception {
		Document document = new Document();
		File file  = new File("FileAnexo.pdf");
		file.createNewFile(); //criando o arquivo em tempo de execução
		PdfWriter.getInstance(document, new FileOutputStream(file)); // pegando o documento e escrevendo dentro dele o conteudofile
		document.open(); // abrindo
		document.add(new Paragraph("Conteúdo do PDF com Java Mail, Texto do PDF!"));
		document.close(); // fechando
		
		return new FileInputStream(file); // retorna um PDF
	}

}
