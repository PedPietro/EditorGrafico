package program;

import java.awt.*;

public class Retangulo extends Ponto{
    private Ponto pontoFinal;

    public Retangulo(int x1, int y1, int x2, int y2, Color cor){
        super(x1, y1, cor);

        pontoFinal = new Ponto(x2, y2, cor);
    }

    public void draw(Graphics g){
        g.setColor(super.getCor());
        g.drawRect(pontoFinal.getX(), pontoFinal.getY(), super.getX() - pontoFinal.getX(), super.getY() - pontoFinal.getY());
    }

    public String toString(){
        String valor = String.format("%-5s%-5d%-5d%-5d%-5d%-5d%-5d%-5d", "r", getX(), getY(), getCor().getRed(), getCor().getGreen(), getCor().getBlue(), pontoFinal.getX(), pontoFinal.getY());
        return valor;
    }
}