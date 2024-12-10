package program;

import java.awt.*;

public class Oval extends Ponto{
    private int raio1, raio2;

    public Oval(int x, int y, int r1, int r2, Color cor){
        super(x, y, cor);
        setRaioA(r1);
        setRaioB(r2);
    }

    public int getRaioA(){
        return this.raio1;
    }

    public int getRaioB(){
        return this.raio2;
    }

    public void setRaioA(int r1){
        this.raio1 = r1;
    }

    public void setRaioB(int r2){
        this.raio2 = r2;
    }

    public void draw(Graphics g){
        g.setColor(super.getCor());        
        g.drawOval(super.getX() - this.raio1, super.getY() - this.raio2, 2 * this.raio1, 2 * this.raio2);
    }

    public String toString(){
        String valor = String.format("%-5s%-5d%-5d%-5d%-5d%-5d%-5d%-5d", "o", getX(), getY(), getCor().getRed(), getCor().getGreen(), getCor().getBlue(), raio1, raio2);
        return valor;
    }
}
