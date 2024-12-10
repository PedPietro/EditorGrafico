package program;

import java.awt.*;

public class Linha extends Ponto{
    private Ponto pontoFinal;

    public Linha(int x1, int y1, int x2, int y2, Color cor){
        super(x1, y1, cor);

        pontoFinal = new Ponto(x2, y2, cor);
    }

    public int getXL(){
        return this.pontoFinal.getX();
    }

    public int getYL(){
        return this.pontoFinal.getY();
    }

    public void setXL(int x){
        this.pontoFinal.setX(x);
    }

    public void setYL(int y){
        this.pontoFinal.setY(y);
    }

    public void draw(Graphics g){
        g.setColor(super.getCor());
        g.drawLine(super.getX(), super.getY(), pontoFinal.getX(), pontoFinal.getY());
    }

    public String toString(){
        String valor = String.format("%-5s%-5d%-5d%-5d%-5d%-5d%-5d%-5d", "l", getX(), getY(), getCor().getRed(), getCor().getGreen(), getCor().getBlue(), pontoFinal.getX(), pontoFinal.getY());
        return valor;
    }
}
