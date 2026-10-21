import javax.sound.sampled.*;
import java.net.URL;

public class Som {

    private Clip clip;
    private AudioInputStream audioStream;
    private volatile boolean tocando = false;

    public void tocarSom(String caminhoArquivo) {
        if (caminhoArquivo == null || caminhoArquivo.trim().isEmpty()) {
            System.err.println("O caminho do áudio não pode estar vazio!");
            return;
        }

        try {
            // Libera o som anterior, caso ainda exista.
            liberarRecursos();

            URL arquivo = getClass().getResource(caminhoArquivo);

            if (arquivo == null) {
                System.err.println("Arquivo de áudio não encontrado: " + caminhoArquivo);
                return;
            }

            audioStream = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            // Libera os recursos automaticamente quando o som terminar.
            clip.addLineListener(new javax.sound.sampled.LineListener() {
                @Override
                public void update(LineEvent evento) {
                    if (evento.getType() == LineEvent.Type.START) {
                        tocando = true;
                    } else if (evento.getType() == LineEvent.Type.STOP) {
                        tocando = false;
                        liberarRecursos();
                    }
                }
            });
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();

        } catch (Exception e) {
            System.err.println("Erro ao carregar o som: " + e.getMessage());
            liberarRecursos();
        }
    }

    public boolean terminou() {
        return !estaTocando();
    }

    public boolean estaTocando() {
        if (clip == null) {
            return false;
        }

        // Confirma o estado do clip e libera os recursos se ele já acabou.
        if (clip.isRunning()) {
            return true;
        }else

        if (clip.getMicrosecondPosition() >= clip.getMicrosecondLength()
                || !clip.isActive()) {
            liberarRecursos();
        }

        return tocando;
    }

    public long getPosicaoMilisegundos() {
        if (clip != null && tocando) {
            return clip.getMicrosecondPosition() / 1000;
        }

        return 0;
    }

    private void liberarRecursos() {
        if (clip != null) {
            try {
                clip.stop();
                clip.close();
            } catch (Exception e) {
                // Ignora: o clip já pode estar fechado pelo LineListener.
            }
            clip = null;
            tocando = false;
        }

        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (Exception e) {
                System.err.println("Erro ao fechar o áudio: " + e.getMessage());
            }

            audioStream = null;
        }
    }
}