/*
CLASE BOLA
 */

package trabajo_final;
/**
 *
 * @author carlos i Arnau
 */
// https://youtu.be/ClraP3olroU
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

public class Bola extends JComponent {

    //DECLARACIÓN DE ATRIBUTOS
    private int x, y;
    private int diametro, direccionX = 1, direccionY = 1;
    private Color colorRelleno, colorTrazado;

    public Bola(int datoDiametro, int datoX, int datoY, Color datoColorTrazado, Color datoColorRelleno) {
        diametro = datoDiametro;
        x = datoX;
        y = datoY;
        colorTrazado = datoColorTrazado;
        colorRelleno = datoColorRelleno;
        //creacion una bola con los datos dados por parametro
    }

    public void dibujar(Graphics2D g2) { //dibuja la bola en el JPanel
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorRelleno);
        g2.fill(new Ellipse2D.Float(x, y, diametro, diametro));
        g2.setColor(colorTrazado);
        g2.draw(new Ellipse2D.Float(x, y, diametro, diametro));
    }

}
