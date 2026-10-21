import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


public class Interface extends JPanel implements ActionListener,MouseMotionListener,MouseListener,KeyListener {
    private JFrame tela = new JFrame();
    private ArrayList<Image> fundo = new ArrayList<>();
    private int idFrame = 1;
    private Timer timerGeral;
    private String caminho;
    private int totalFrames = 845;
    private Som jarvis = new Som();
    private boolean neutro = false;
    private boolean autoDestruicao = false;
    private boolean iniciando = true;
    private boolean podeTocarSom = true;
    private boolean mostrarLogin = false;
    private JLabel lblusuario = new JLabel("USUÁRIO:");
    private JLabel lblSenha = new JLabel("SENHA:");
    private JPasswordField campoSenha = new JPasswordField();
    private JTextField campoUsuario = new JTextField();
    private String usuario;
    private int tentativas = 3;
    private boolean mostrarMenu = false;
    private boolean carregarMenu = false; 
    private JButton btnVerDados = new JButton("Ver Dados TXT");

    private String maneUsuario = "emanuel";
    private String maneSenha = "007";

    private String luisUsuario = "luis";
    private String luiseSenha = "1233";

     private String amaralUsuario = "amaral";
    private String amaralSenha = "402893";

    

    public Interface(){
        tela.setSize(1920, 1080);
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setResizable(false);
        tela.setLocationRelativeTo(null);
        tela.setTitle("PHISHING 2000");
        requestFocusInWindow();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        tela.add(this);
        tela.setVisible(true);

        setLayout(null);

        lblusuario.setBounds(30, 30, 100, 30);
        lblusuario.setForeground(java.awt.Color.WHITE);
        lblusuario.setVisible(false);
        add(lblusuario);

        lblSenha.setBounds(30, 100, 100, 30);
        lblSenha.setForeground(java.awt.Color.WHITE);
        lblSenha.setVisible(false);
        add(lblSenha);

        campoSenha.setBounds(90, 110, 500, 16);
        campoSenha.setVisible(false);
        add(campoSenha);

        campoUsuario.setBounds(90, 40, 500,16);
        campoUsuario.setVisible(false);
        add(campoUsuario);

       



        btnVerDados.setBounds(90, 150, 150, 30);


        btnVerDados.addActionListener(e -> {
    
            JanelaDados janela = new JanelaDados();
            janela.setVisible(true);
        });


        

        ActionListener acaoLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioDigitado = campoUsuario.getText();
                String senhaDigitada = new String(campoSenha.getPassword());
                
                // Valida os dados usando as variáveis que você já declarou na classe
                if((usuarioDigitado.equalsIgnoreCase(maneUsuario) && senhaDigitada.equals(maneSenha))) {
                    jarvis.tocarSom("sons/jarvis/emanuel.wav");
                    caminho = "fundo/emanuel/";
                    neutro = false;
                    totalFrames = 139;
                    mostrarLogin = false;
                    if(jarvis.terminou()){
                        carregarMenu = true;
                    }
                    campoSenha.setVisible(false);
                    campoUsuario.setVisible(false);

                    lblusuario.setVisible(false);
                    lblSenha.setVisible(false);
                    return;
                    
               
                } 
                if (usuarioDigitado.equals(luisUsuario) && senhaDigitada.equals(luiseSenha)){
                    jarvis.tocarSom("sons/jarvis/luis.wav");
                    mostrarLogin = false;
                    neutro = false;
                    if(!jarvis.estaTocando()){
                        carregarMenu = true;
                    }
                    campoSenha.setVisible(false);
                    campoUsuario.setVisible(false);

                    lblusuario.setVisible(false);
                    lblSenha.setVisible(false);

                    caminho = "fundo/luis/";
                    totalFrames = 127;
                    return;
                }
                if(usuarioDigitado.equals(amaralUsuario) && senhaDigitada.equals(amaralSenha)){
                    jarvis.tocarSom("sons/jarvis/amaral.wav");
                    mostrarLogin = false;
                    if(jarvis.terminou()){
                        carregarMenu = true;
                    }
                    neutro = false;
                    campoSenha.setVisible(false);
                    campoUsuario.setVisible(false);

                    lblusuario.setVisible(false);
                    lblSenha.setVisible(false);

                    caminho = "fundo/amaral/";
                    totalFrames = 128;
                    return;

                }else{
                    tentativas -= 1;
                    if(tentativas == 2){
                        jarvis.tocarSom("sons/jarvis/2 tentativas.wav");
                        neutro = false;
                        caminho = "fundo/restam 2 tentativas/";
                        totalFrames = 131;
                        return ;

                    }
                    if(tentativas == 1){
                        jarvis.tocarSom("sons/jarvis/1 tentativa.wav");
                        neutro = false;
                        caminho = "fundo/resta 1 tentativa/";
                        totalFrames = 119;
                        return;

                    }
                    if(tentativas <= 0){
                        jarvis.tocarSom("sons/jarvis/destruicao.wav");
                        neutro = false;
                        caminho = "fundo/auto destruicao/";
                        totalFrames = 219;
                        campoSenha.setVisible(false);
                        campoUsuario.setVisible(false);
                        mostrarLogin = false;
                        lblusuario.setVisible(false);
                        lblSenha.setVisible(false);
                        return;

                    }


                    }
                    
                    
            }
            
        };

        campoUsuario.addActionListener(acaoLogin);
        campoSenha.addActionListener(acaoLogin);

        campoUsuario.requestFocusInWindow();
        campoSenha.requestFocusInWindow();

        

        lblusuario.setVisible(false);
        lblSenha.setVisible(false);
        
        ImageIO.setUseCache(false);

        

        timerGeral = new Timer(33,this);
        Thread carregarFundo = new Thread(new Runnable() {
            @Override 
            public void run() {
                while (true) {
                    try {  
                        
                        
                        if(carregarMenu && !jarvis.estaTocando()){
                            if(!jarvis.estaTocando()){
                                    mostrarMenu = true;
                                    carregarMenu = false;
                                    System.out.println("menu iniciado");
                                
                                }

                            if(podeTocarSom){
                                jarvis.tocarSom("sons/jarvis/carregando interface.wav");
                                podeTocarSom = false;
                                neutro = false;
                                caminho = "fundo/carregando interface/";
                                totalFrames = 137;
                                
                                
                            }
                            
                           
                            
                        }

                        if(mostrarMenu){
                            
                            neutro = false;
                            if(podeTocarSom){
                                podeTocarSom = false;
                                jarvis.tocarSom("sons/jarvis/pronto.wav");
                                caminho = "fundo/pronto/";
                                totalFrames = 56;
                                mostrarMenu = false;
                                add(btnVerDados);
                            }
                        }
                        

                        if (neutro) {
                            caminho = "fundo/neutro/";
                            totalFrames = 845; // Garante que o neutro volta ao tamanho certo
                        }

                        if(iniciando){
                            
                            neutro = false;
                            caminho = "fundo/iniciando sistema/";
                            
                            totalFrames = 47;
                            if(podeTocarSom){
                                jarvis.tocarSom("sons/jarvis/iniciando sistema.wav");
                                podeTocarSom = false;
                            }
                        }
                        if(mostrarLogin){
                            lblusuario.setVisible(true);
                            lblSenha.setVisible(true);
                            campoSenha.setVisible(true);
                            campoUsuario.setVisible(true);
                        }
                        if (autoDestruicao) {
                            neutro = false;
                            caminho = "fundo/auto destruicao/";
                            totalFrames = 220; // Tamanho da pasta de destruição
                            
                            if (podeTocarSom) {
                                jarvis.tocarSom("sons/jarvis/destruicao.wav");
                                podeTocarSom = false;
                                
                            }
                        }

                        // Sincronização por áudio se estiver a tocar, senão avança por frame rate
                        if (!neutro) {
                            long tempoAtualMs = jarvis.getPosicaoMilisegundos();
                            idFrame = (int) (tempoAtualMs / 33.33) + 1;
                            
                        } else {
                            if (fundo.size() < 20) {
                                idFrame++;
                            }
                        }

                        // Tratamento de limites de acordo com o modo atual
                        if (idFrame >= totalFrames || idFrame <= 0) {
                            if(iniciando){
                                idFrame = 1;
                                iniciando = false;
                                mostrarLogin = true;
                            }
                            if (autoDestruicao) {
                                idFrame = 1;
                                autoDestruicao = false;
                                  
                            } if(neutro) {
                                idFrame = 1; // Loop normal no neutro
                            }
                        }

                        // Carrega o frame sincronizado
                        if (fundo.size() < 20) {
                            if(idFrame <= 0 || idFrame >= totalFrames){
                                idFrame = 1;
                            }
                            BufferedImage frameTemp = ImageIO.read(getClass().getResource(caminho + "(" + idFrame + ").png"));
                            synchronized(fundo) {
                                fundo.add(frameTemp);
                            }
                        }

                        if(!jarvis.estaTocando()){
                            neutro = true;
                            podeTocarSom = true;
                            totalFrames = 845;
                        }
                        
                        repaint();
                        Thread.sleep(18);
                        
                    } catch(Exception ERRO) {
                        System.err.println("ERRO AO CARREGAR FUNDO! CÓDIGO DE ERRO: " + ERRO);
                        ERRO.printStackTrace();
                    }
                }
            }
        });

        carregarFundo.start();
        timerGeral.start();
        
    }

    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if(fundo.size() >= 3){
            g.drawImage(fundo.get(1),0, 0, getWidth(), getHeight(),null);
            if(fundo.size() >= 5){
                fundo.remove(0);
            }
        }
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
    }


    //MOUSE

    @Override
    public void mouseDragged(MouseEvent e) {
       
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
        

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }

    //TECLADO
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_0){
            autoDestruicao = !autoDestruicao;
            neutro = !neutro;    
            System.out.println("pressionado");
            idFrame = 1;
            fundo.clear();

            synchronized(fundo){
                fundo.clear();
            }

        } 

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String usuario = campoUsuario.getText();
            String senha = new String(campoSenha.getPassword());
            if(usuario.equalsIgnoreCase("mane") && senha.equals("007") ){
                jarvis.tocarSom("sons/jarvis/emanuel.wav");

            }else{
                jarvis.tocarSom("sons/jarvis/emanuel.wav");
            }
        }
        
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    
}
