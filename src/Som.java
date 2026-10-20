import javax.sound.sampled.*;
import java.io.File;

public class Som {

    private Clip clip;
    private AudioInputStream audioStream;

    
    public void tocarSom(String caminhoArquivo) {
        try {
            if(caminhoArquivo == null){
                System.out.println("O CAMINHO NÃO PODE ESTAR VAZIO!");
                return;
            }

            File arquivo = new File(caminhoArquivo);
            if (arquivo == null) {
                System.err.println("Arquivo de áudio não encontrado: " + caminhoArquivo);
                
            }

            audioStream = AudioSystem.getAudioInputStream(getClass().getResource(caminhoArquivo));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();

        } catch (Exception e) {
            System.err.println("Erro ao carregar o som: " + e.getMessage());
        }
    }

    public boolean terminou() {
        if (clip == null) return true;

        try {
                       // Não bloqueia: apenas verifica se o clip já acabou de tocar.
            // Se terminou, libera os recursos e retorna true uma única vez.
            if (clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                clip.close();
                audioStream.close();
                clip = null;
                audioStream = null;
                return true;
            }
            return false;

        } catch (Exception e) {
            System.err.println("Erro ao verificar o término do som: " + e.getMessage());
            return false;
        }
    }

    public boolean estaTocando(){
        if(clip == null){
            return false;
        }
        return clip.isRunning();
    }
}