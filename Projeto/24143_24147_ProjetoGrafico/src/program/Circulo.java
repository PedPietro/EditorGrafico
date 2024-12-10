package program;

import java.awt.*;

public class Circulo extends Ponto{
    private int raio;

    public Circulo(int x, int y, int r, Color cor){
        super(x, y, cor);
        this.setRaio(r);
    }

    public int getRaio(){
        return this.raio;
    }

    public void setRaio(int r){
        this.raio = r;
    }

    public void draw(Graphics g){
        g.setColor(super.getCor());
        g.drawOval(super.getX() - this.raio, super.getY() - this.raio, 2 * this.raio, 2 * this.raio);
    }

    public String toString(){
        String valor = String.format("%-5s%-5d%-5d%-5d%-5d%-5d%-5d", "c", getX(), getY(), getCor().getRed(), getCor().getGreen(), getCor().getBlue(), raio);
        return valor;
    }
}
