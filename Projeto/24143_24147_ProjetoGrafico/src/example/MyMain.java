package example;

import javax.swing.*;
import java.awt.*;

public class MyMain extends JFrame {
    public JButton a, b;

    public JToolBar bar;

    public Container caixa;

    public JInternalFrame frame;

    public MyMain(String nome){
        super(nome);

        a = new JButton("Miguelito 2");
        b = new JButton("Dora");

        a.addActionListener(new ExemploJFrame.Actions());

        bar = new JToolBar();
        bar.setLayout(new FlowLayout());
        bar.add(a);
        bar.add(b);

        bar.setFloatable(false);

        caixa = this.getContentPane();
        caixa.setLayout(new BorderLayout());
        caixa.add(bar, BorderLayout.NORTH);

        frame = new JInternalFrame("Nenhum Arquivo Aberto", false, false, false, false);
        frame.setVisible(true);
        caixa.add(frame, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }
}
