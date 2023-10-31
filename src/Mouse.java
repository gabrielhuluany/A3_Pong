import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

/**
 *
 * @author Gabriel Bonifácio Huluany Gonzalez RA 125111372859
 */

public class Mouse implements MouseListener {
    private Cena cena;

    public Mouse(Cena cena) {
        this.cena = cena;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("X: " + e.getX());
        //System.out.println("Y: " + e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {

    }
}
