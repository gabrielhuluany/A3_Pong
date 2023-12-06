import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

/**
 *
 * @author Gabriel Bonifácio Huluany Gonzalez RA 125111372859
 */

public class Renderer {
    private static GLWindow window = null;
    private static int screenWidth = 1366, screenHeight = 768;

    private static void init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        window = GLWindow.create(caps);
        window.setSize(screenWidth, screenHeight);
        window.setResizable(false);
        window.setFullscreen(true);
        window.setVisible(true);

        Cena cena = new Cena();
        window.addGLEventListener(cena); //Adiciona a cena à janela
        window.addKeyListener(new Keyboard(cena)); //Adiciona o teclado à cena
        window.addMouseListener(new Mouse(cena)); //Adiciona o mouse à cena

        //window.requestFocus();
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); //Inicia o loop de animação

        //Encerra a aplicação adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        init();
    }
}
