import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

/**
 *
 * @author Gabriel Bonifácio Huluany Gonzalez RA 125111372859
 */

public class Keyboard implements KeyListener {
    private Cena cena;

    public Keyboard(Cena cena) {
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Pressionou a tecla: " + e.getKeyCode());

        switch (e.getKeyCode()) {
            //Encerrar aplicação
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_LEFT:
                if (cena.bastaoX > -0.8 && !cena.pausado) {
                    cena.bastaoX -= 0.1f;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (cena.bastaoX < 0.8 && !cena.pausado) {
                    cena.bastaoX += 0.1f;
                }
                break;
            case KeyEvent.VK_P:
                //Pausar partida
                cena.pausado = !cena.pausado;
                break;
            case KeyEvent.VK_ENTER:
                //Começar partida
                if (cena.fase == 0) {
                    cena.fase = 1;
                }
                if (cena.fase == 3) {
                    cena.fase = 0;
                    cena.resetar();
                    cena.fase = 1;
                }
                break;
            case KeyEvent.VK_BACK_SPACE:
                //Encerrar partida
                if (cena.fase == 3) {
                    cena.fase = 0;
                    cena.resetar();
                }
                if(cena.fase == 1 || cena.fase == 2) {
                    cena.fase = 3;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
