package pruevasr;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Clase que extiende de JPanel para dibujar el gráfico
class pruevaaa extends JPanel {

    // Atributos para guardar la imagen y el objeto Graphics2D
    BufferedImage image;
    Graphics2D g2d;

    // Atributo para guardar las líneas que se dibujan en un objeto Stack
    Stack<Linea> lineas;

    // Constructor que inicializa los atributos y agrega los listeners del ratón
    public pruevaaa() {
        super();
        init();
    }

    private void init() {
        setBackground(Color.BLACK);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        g2d = image.createGraphics();
        g2d.setBackground(Color.yellow);
        g2d.setStroke(new BasicStroke(2));

        // Se crea el objeto Stack que guarda las líneas
        lineas = new Stack<>();

        MouseAdapter mouseHandler = new MouseAdapter() {
            private final Point curPoint = new Point();

            public void mousePressed(MouseEvent e) {
                curPoint.setLocation(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Se dibuja una línea desde el punto anterior hasta el punto actual
                g2d.drawLine(curPoint.x, curPoint.y, e.getX(), e.getY());
                // Se crea un objeto Linea con los puntos de la línea y se agrega a la pila
                Linea linea = new Linea(curPoint.x, curPoint.y, e.getX(), e.getY());
                lineas.push(linea);
                // Se actualiza el punto anterior con el punto actual
                curPoint.setLocation(e.getPoint());
                // Se repinta el panel
                repaint();

            }
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    // Método que borra la última línea que se dibujó
    public void borrar() {
        // Se verifica si la pila de líneas está vacía
        if (!lineas.isEmpty()) {
            // Si no está vacía, se saca la última línea de la pila
            Linea linea = lineas.pop();
            // Se borra la línea del objeto Graphics2D con el color de fondo
            g2d.setColor(g2d.getBackground());
            g2d.drawLine(linea.x1, linea.y1, linea.x2, linea.y2);
            // Se cambia el color del objeto Graphics2D al original
            g2d.setColor(Color.BLUE);
            // Se repinta el panel
            repaint();
        }
    }
}

// Clase que extiende de JFrame para crear las dos ventanas, el botón de borrado y sincronizarlos
class Ventana extends JFrame {

    // Atributos para guardar los dos paneles de dibujo y el botón de borrado
    private pruevaaa dibujo1;
    private pruevaaa dibujo2;
    private JButton botonBorrar;

    // Constructor que inicializa los atributos y crea las ventanas y el botón
    public Ventana() {
        // Se crea el primer panel de dibujo y se agrega a la primera ventana
        dibujo1 = new pruevaaa();
        dibujo1.setBackground(Color.WHITE);
        JFrame ventana1 = new JFrame("Ruben del carmen");
        ventana1.setMinimumSize(new Dimension(640, 480));
        ventana1.setResizable(false);
        ventana1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana1.add(dibujo1);
        ventana1.setLocationRelativeTo(null);
        ventana1.setVisible(true);

        // Se crea el segundo panel de dibujo y se agrega a la segunda ventana
        dibujo2 = new pruevaaa();
        dibujo2.setBackground(Color.WHITE);
        JFrame ventana2 = new JFrame("Ruben del carmen");
        ventana2.setMinimumSize(new Dimension(640, 480));
        ventana2.setResizable(false);
        ventana2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana2.add(dibujo2);
        ventana2.setLocation(ventana1.getX() + ventana1.getWidth(), ventana1.getY());
        ventana2.setVisible(true);

        // Se crea el botón de borrado y se agrega a la primera ventana
        botonBorrar = new JButton("Borrar");
        botonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Al presionar el botón, se borra la última línea de los dos paneles
                dibujo1.borrar();
                dibujo2.borrar();
            }
        });
        ventana1.add(botonBorrar, "South");

        // Se crea un hilo que se encarga de sincronizar los dos paneles de dibujo
        Thread sincronizador = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Se obtiene el objeto Graphics2D de cada panel
                    Graphics2D g1 = dibujo1.g2d;
                    Graphics2D g2 = dibujo2.g2d;
                    // Se verifica si hay algún cambio en el objeto Graphics2D del primer panel
                    if (!g1.equals(g2)) {
                        // Si hay cambio, se copia el objeto Graphics2D del primer panel al segundo
                        g2.drawImage(dibujo1.image, 0, 0, null);
                        // Se repinta el segundo panel
                        dibujo2.repaint();
                    }
                }
            }
        });
        // Se inicia el hilo
        sincronizador.start();
    }

    // Método principal para ejecutar el programa
    public static void main(String[] args) {
        // Se crea una instancia de la clase Ventana
        Ventana v = new Ventana();
    }
}

// Clase que representa una línea con sus coordenadas
class Linea {
    // Atributos para guardar las coordenadas de la línea
    int x1, y1, x2, y2;

    // Constructor que recibe las coordenadas de la línea
    public Linea(int x1, int y1, int x2, int y2) {
        // Se asignan los atributos
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}