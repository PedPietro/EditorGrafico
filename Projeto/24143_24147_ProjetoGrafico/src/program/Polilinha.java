package program;

import java.awt.*;

public class Polilinha extends Ponto{
    Ponto[] pontos = new Ponto[100];
    int qtosPontos = 0;

    public Polilinha(int x, int y, Color cor){
        super(x, y, cor);
    }

    public void appendPonto(Ponto novoPonto){
        pontos[qtosPontos] = novoPonto;
        qtosPontos ++;
    }

    public void draw(Graphics g){
        g.setColor(super.getCor());
        for(int i = 0; i < qtosPontos; i ++){
            g.drawLine((i > 0) ? pontos[i - 1].getX() : super.getX(), (i > 0) ? pontos[i - 1].getY() : super.getY(), pontos[i].getX(), pontos[i].getY());
        }
    }

    public String toString(){
        String valor = String.format("%-5s%-5d%-5d%-5d%-5d%-5d", "poli", getX(), getY(), getCor().getRed(), getCor().getGreen(), getCor().getBlue());
        for(int i = 0; i < qtosPontos; i ++){
            valor += String.format("%-5d%-5d", pontos[i].getX(), pontos[i].getY());
        }
        return valor;
    }
}
