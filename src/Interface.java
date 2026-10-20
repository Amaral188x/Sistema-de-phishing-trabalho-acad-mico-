import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import java.util.ArrayList;


public class Interface extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
    private JFrame tela = new JFrame();
    private ArrayList<Image> fundo = new ArrayList<>();
    private int idFrame = 1;
    private Timer timerGeral;
    private String caminho;
    private int totalFrames = 845;
    private Som jarvis = new Som();
    private boolean neutro = true;
    private boolean autoDestruicao = false;
    private boolean iniciando = true;
    private boolean podeTocarSom = true;
    private boolean somIniciandoTocado = false;
    private boolean loginExibido = false;

    public Interface() {
        // JANELA
        tela.setSize(1920, 1080);
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setResizable(false);
        tela.setLocationRelativeTo(null);
        tela.setTitle("PHISHING 2000");
        tela.add(this);
        tela.setVisible(true);

        // OTIMIZAÇÃO DE LEITURA DE IMAGENS (evita FileCacheImageInputStream / Stream Closed)
        ImageIO.setUseCache(false);

        // EVENTOS
        requestFocusInWindow();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        // TIMER GERAL
        timerGeral = new Timer(16, this);
        Thread carregarFundo = new Thread(new Runnable() {
            @Override

            public void run(){
                while (true) {
                    try{
                        // FASE 1: INICIANDO SISTEMA
                        if(iniciando){
                            neutro = false;
                            caminho = "/fundo/iniciando sistema/";
                            totalFrames = 47;

                            // Toca o som de "iniciando sistema" uma única vez
                            if(podeTocarSom && !somIniciandoTocado){
                                somIniciandoTocado = true;
                                podeTocarSom = false;
                                jarvis.tocarSom("/sons/jarvis/iniciando sistema.wav");
                            }

                            // Quando o som termina, libera para o próximo som
                            if(somIniciandoTocado && !jarvis.estaTocando()){
                                podeTocarSom = true;
                            }

                            // FASE 2: SISTEMA INICIADO (após o primeiro som terminar)
                            if(idFrame >= totalFrames && somIniciandoTocado && !jarvis.estaTocando()){
                                if(!loginExibido){
                                    login();
                                    loginExibido = true;
                                }
                                neutro = false;
                                caminho = "/fundo/sistema iniciado/";
                                totalFrames = 73;

                                if(podeTocarSom){
                                    podeTocarSom = false;
                                    jarvis.tocarSom("/sons/jarvis/sistema iniciado.wav");
                                    if(jarvis.terminou()){
                                        iniciando = false;
                                        neutro = true;
                                        podeTocarSom = true;
                                    }
                                }
                            }
                        }

                        // MODO NEUTRO: quando nada está tocando e não há auto destruição,
                        // a interface fica com a animação de fundo neutra
                        if(!jarvis.estaTocando() && !iniciando && !autoDestruicao){
                            neutro = true;
                            caminho = "/fundo/neutro/";
                            totalFrames = 845;
                        }

                        // AUTO DESTRUIÇÃO
                        if(autoDestruicao){
                            neutro = false;
                            caminho = "/fundo/auto destruicao/";
                            totalFrames = 220;
                            if(podeTocarSom){
                                podeTocarSom = false;
                                jarvis.tocarSom("/sons/jarvis/destruicao.wav");
                            }
                        }
                        if(idFrame < totalFrames){
                            if(fundo.size() < 20){
                                BufferedImage frameTemp = ImageIO.read(getClass().getResource(caminho + "(" + idFrame + ").png"));
                                idFrame ++;
                                synchronized(fundo){
                                    fundo.add(frameTemp);
                                }
                            }
                        }else{
                            idFrame = 1;
                            if(!neutro && autoDestruicao){
                                neutro = !neutro;
                                autoDestruicao = false;
                                podeTocarSom = true;
                            }
                        }
                        repaint();
                        Thread.sleep(18);

                    } catch (Exception ERRO) {
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fundo.size() >= 3) {
            g.drawImage(fundo.get(1), 0, 0, getWidth(), getHeight(), null);
            if (fundo.size() >= 5) {
                fundo.remove(0);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TIMER 16ms - reservado para lógica de animação/jogo futura
    }

    // TECLADO

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_0) {
            autoDestruicao = !autoDestruicao;
            neutro = !neutro;
            System.out.println("pressionado");
            idFrame = 1;
            synchronized (fundo) {
                fundo.clear();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // MOUSE

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

    // LOGIN

    public void login() {
        JLabel lblusuario = new JLabel("usuário");
        lblusuario.setBounds(30, 30, 100, 500);
        tela.add(lblusuario);
        tela.repaint();
    }
}
