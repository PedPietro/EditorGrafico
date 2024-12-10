package example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ExemploJFrame {
    public static JFrame main;

    public static void main(String[] args) {
         main = new MyMain("Manuel");

         main.addWindowListener(new WindowAdapter() {
             @Override
             public void windowClosing(WindowEvent e) {
                 System.exit(0);
             }
         });
    }

    public static class Actions implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser j = new JFileChooser();

            j.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int status = j.showOpenDialog(main);

            if(status == JFileChooser.APPROVE_OPTION){
                File arquivo = j.getSelectedFile();
                try{
                    BufferedReader handlerRead = new BufferedReader(new FileReader(arquivo));
                    System.out.println(handlerRead.readLine());
                    handlerRead.close();
                }
                catch(Exception err){
                    System.out.println("Erro");
                }
            }
        }
    }
}

