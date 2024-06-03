/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template fichero, choose Tools | Templates
 * and open the template in the editor.
 */
package trabajo_final;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author carlos i arnau
 */
// https://youtu.be/ClraP3olroU
public class laberinto extends JFrame {

    Container panelContenidos;
    //Atributos Int
    final int finalFichero = -1, dimensionTablero = 900;
    int longFilas, longColumnas, ladoX, ladoY, seleccion = 0;
    int posInicialFila, posInicialColumna, posFinalFila, posFinalColumna;
    //Inicializamos el laberinto con el primer fichero
    String ficheroElegido = "maze1.txt";
    //Creación de objetos de otras clases
    private casilla[][] casillaObj;
    private Bola bola;
    panelLaberinto panel = new panelLaberinto();
    EventosTeclado et = new EventosTeclado();
    BufferedImage imagen;

    //Construcutor
    public laberinto() {
        //llamamos al método inicialización
        inicializacion();
    }

    private void inicializacion() {

        // bola.addKeyListener(EventosTeclado);
        panelContenidos = getContentPane();
        setLayout(new BorderLayout());
        //asociación al contenedor JFrame del administrador de layout BorderLayout
        panelContenidos.setLayout(new BorderLayout());
        //inclusión del contenedor panel en el area central del panel de contenidos del JFrame
        setResizable(false);
        setSize(909, 955);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addKeyListener(new EventosTeclado());
        //dirijimos la atención de la ejecución al objeto PanelTablero
        setFocusable(true);

        leerFicheroMaze();
        //Declaracion componentes JMenuBar
        JMenu ficheroMenu = new JMenu("FICHERO");

        JMenuItem abrirMenu = new JMenuItem("ABRIR");
        abrirMenu.addActionListener(gestorEventos);

        JMenuItem reiniciarMenu = new JMenuItem("REINICIAR FICHA");
        reiniciarMenu.addActionListener(gestorEventos);

        JMenuItem borrarMenu = new JMenuItem("SALIR");
        borrarMenu.addActionListener(gestorEventos);

        ficheroMenu.add(abrirMenu);
        ficheroMenu.add(reiniciarMenu);
        ficheroMenu.add(borrarMenu);
        ////////DECLARACIÓN COMPONENTE JMenuBar barraMenu
        JMenuBar barraMenu = new JMenuBar();
        barraMenu.add(ficheroMenu);

        add(panel, BorderLayout.CENTER);
        add(barraMenu, BorderLayout.NORTH);
    }
    ActionListener gestorEventos = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evento) {
            switch (evento.getActionCommand()) {
                case "ABRIR": //ELIGE LABERINTO A JUGAR Y DA VALOR A ELECCION QUE NOS PERMITIRA CREAR EL STRING CORRESPONDIENTE
                    seleccion = JOptionPane.showOptionDialog(null, "ELIJA EL LABERINTO",
                            "PULSAR BOTÓN", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"LABERINTO 1", "LABERINTO 2", "LABERINTO 3", "LABERINTO 4"}, "2");
                    eleccionFichero();
                    leerFicheroMaze();
                    repaint();
                    break;

                case "REINICIAR FICHA":
                    reiniciarBola();
                    //repaint();
                    break;
                case "SALIR":
                    System.exit(0);
                    break;
            }
        }
    };

    private class panelLaberinto extends JPanel {

        public panelLaberinto() {

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //PINTAR TABLERO
            //hacemos un rectangulo relleno que ocupe todo el JPanel naranja
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.orange);
            Rectangle2D rectangulo = new Rectangle2D.Double(0, 0, dimensionTablero, dimensionTablero);
            g2d.fill(rectangulo);

            for (int fila = 0; fila < (longFilas); fila++) {
                for (int columna = 0; columna < (longColumnas); columna++) {
                    //iteracion toda la matriz para ver si esta ocupada y pintar bola dependiedo estado
                    if (casillaObj[fila][columna].isOcupada()) { // si esta ocupada 
                        int movimientoX = (14 + ((columna) * (92)));
                        int movimientoY = (8 + ((fila) * 60));
                        bola = new Bola(45, movimientoX, movimientoY, Color.white, Color.black);
                        bola.dibujar(g2d);

                    }
                    repaint(); //actualiza jpanel
                }
            }
            // PINTA LAS LINEAS DE LA MATRIZ
            BasicStroke grosor = new BasicStroke(6);
            g2d.setStroke(grosor);
            g2d.setColor(Color.black);
            //declaracion stroke para las lineas a pintar
            int dimXActual = 0;
            int dimYActual = 0;
            ladoX = dimensionTablero / longFilas; //longitud X casilla
            ladoY = dimensionTablero / longColumnas; //longitud Y casilla
            //Pintamos el borde superior del tablero de la primera fila,
            //que no tiene el sud de la fila de arriba pq es la primera
            for (int fila = 0; fila <= 1; fila++) {
                g2d.drawLine(dimXActual, dimYActual, dimensionTablero, dimYActual);
            }
            //pintamos todo el laberinto, sin las del norte, ya que el sur
            //asigna las lineas del norte de la de abajo
            for (int fila = 0; fila < longFilas; fila++) {
                for (int columna = 0; columna < longColumnas; columna++) {
                    if (casillaObj[fila][columna].sur == 1) {
                        g2d.drawLine(dimXActual, dimYActual + ladoX, dimXActual + ladoY, dimYActual + ladoX);
                    }
                    if (casillaObj[fila][columna].este == 1) {
                        g2d.drawLine(dimXActual + ladoY, dimYActual, dimXActual + ladoY, dimYActual + ladoX);
                    }
                    if (casillaObj[fila][columna].oeste == 1) {
                        g2d.drawLine(dimXActual, dimYActual, dimXActual, dimYActual + ladoX);
                    }
                    dimXActual = dimXActual + ladoY;
                }
                dimXActual = 0;
                dimYActual = dimYActual + ladoX;
            }
            //pintar la imagen a traves del bufferedimage
            try {
                imagen = ImageIO.read(new File("salida.jpg"));
            } catch (Exception E) {
                JOptionPane.showMessageDialog(panel, "NO SE HA PODIDO INICIAR"
                        + " LA IMAGEN DE SALIDA");
            }

            g2d.drawImage(imagen, (14 + ((posFinalColumna) * (80))), (8 + ((posFinalFila) * 58)), ladoY, ladoX, panel);
        }

    }

    //Métodos
    private void mensajeVictoria() {
        //muestra un optionpane con el mensaje que hemos ganado
        JOptionPane.showMessageDialog(panelContenidos, "HAS GANADO !!!!");

    }

    private void eleccionFichero() {
        //devuelve un string qu estara en el filereader como constructor
        //sera el que dictamine el maze a leer
        ficheroElegido = "maze" + (seleccion + 1) + ".txt";
    }

    private void posicionInicialBola() { //nos dara la posicion inicial de la bola en el laberinto que toque
        Random rand = new Random();
        posInicialFila = (int) rand.nextInt(longFilas); //fila donde se pintara la bola inicialmente     
        posInicialColumna = (int) rand.nextInt(longColumnas);  //columna donde se pintara la bola inicialmente
    }

    private void reiniciarBola() {//Vuelve a poner la bola en la ficha inicial
        for (int fila = 0; fila < longFilas; fila++) {
            for (int columna = 0; columna < longColumnas; columna++) {
                if (casillaObj[fila][columna].estado()) {
                    casillaObj[fila][columna].cambiarEstado();
                }

            }
            casillaObj[posInicialFila][posInicialColumna].setOcupada();
        }
    }

    private casilla[][] leerFicheroMaze() { //lee el fiochero y crea el objeto matricial 2d de casillas
        casilla[][] matriz = null;
        int gruposArray[] = new int[4];
        try {
            //leera de 4 en 4 para ir completando cada casilla
            BufferedReader br = new BufferedReader(new FileReader(ficheroElegido)); //ficheroElegido
            longFilas = Integer.parseInt(br.readLine()); //dos primeras lineas son numero filas y columnas
            longColumnas = Integer.parseInt(br.readLine());
            posicionInicialBola(); //llamamos al metodo aqui, q necesita los valores de longFilas y Columnas
            matriz = new casilla[longFilas][longColumnas];

            for (int fila = 0; fila < longFilas; fila++) {
                for (int columna = 0; columna < longColumnas; columna++) {
                    for (int grupos = 0; grupos < 4; grupos++) {
                        gruposArray[grupos] = Character.getNumericValue(br.read());
                    }
                    matriz[fila][columna] = new casilla(gruposArray[0], gruposArray[1], gruposArray[2], gruposArray[3]);
                }
                br.read();
            }
            posFinalFila = Integer.parseInt(br.readLine()); //dos ultimas lineas son posicion victoria
            posFinalColumna = Integer.parseInt(br.readLine());
            br.close(); //Cerramos el lector de ficheros
        } catch (FileNotFoundException e) {
            System.out.println("No se encuentra archivo");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("No se pudo convertir a entero");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error accediendo al archivo.");
            e.printStackTrace();
        }
        casillaObj = matriz; //le da al atributo global la matriz de casillas construida
        casillaObj[posInicialFila][posInicialColumna].setOcupada(); //ocupa la casilla inicial
        if (casillaObj[posFinalFila][posFinalColumna - 1].isOcupada()) { //existe la posibilidad que la casilla inicial sea la de victoria
            mensajeVictoria(); //sale el mensaje de victoria
        }
        return matriz;
    }

    //clase gestión de eventos del teclado
    private class EventosTeclado implements KeyListener {

        @Override
        //eventos teclado que espera A W S D para mover la bola en la matriz
        //esta se mueve a partir de la liberacion y ocupacion de las casillas
        public void keyPressed(KeyEvent ke) {
            boolean cambio = false;
            int llave = ke.getKeyCode();
            for (int fila = 0; fila < longFilas; fila++) {
                for (int columna = 0; columna < longColumnas; columna++) {
                    if (casillaObj[fila][columna].estado()) {
                        //En función de la casilla pulsada
                        switch (llave) {
                            //Si la casilla pulsada es la casilla de dirección Arriba
                            case KeyEvent.VK_W:
                                if ((fila != 0) && (casillaObj[fila][columna].norte != 1)) {
                                    casillaObj[fila - 1][columna].setOcupada();
                                    casillaObj[fila][columna].setLiberada();
                                }
                                cambio = true;
                                break;
                            //Si la casilla pulsada es la casilla de dirección Derecha    
                            case KeyEvent.VK_D:
                                if ((columna != 9) && (casillaObj[fila][columna].este != 1)) {
                                    casillaObj[fila][columna + 1].setOcupada();
                                    casillaObj[fila][columna].setLiberada();
                                }
                                cambio = true;
                                break;
                            //Si la casilla pulsada es la casilla de dirección Abajo
                            case KeyEvent.VK_S:
                                if ((fila != 14) && (casillaObj[fila][columna].sur != 1)) {
                                    casillaObj[fila + 1][columna].setOcupada();
                                    casillaObj[fila][columna].setLiberada();
                                }
                                cambio = true;
                                break;
                            //Si la casilla pulsada es la casilla de dirección Izquierda    
                            case KeyEvent.VK_A:
                                if ((columna != 0) && (casillaObj[fila][columna].oeste != 1)) {
                                    casillaObj[fila][columna - 1].setOcupada();
                                    casillaObj[fila][columna].setLiberada();
                                }
                                cambio = true;
                                break;
                        }
                        break;
                    }
                }
                //si ha habido ya un cambio se finaliza el tratamiento
                if (cambio) {
                    break;
                }
            }
            repaint();
            if (casillaObj[posFinalFila][posFinalColumna - 1].estado()) {
                mensajeVictoria();
            }
        }

        @Override
        public void keyTyped(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    }

    public static void main(String[] args) {
        new laberinto();
    }
}
