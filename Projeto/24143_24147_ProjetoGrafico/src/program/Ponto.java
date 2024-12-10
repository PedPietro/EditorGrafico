package program;

import java.awt.*;

public class Ponto {
    private int x, y;
    private Color cor;

    public Ponto(int x, int y, Color cor){
        this.x = x;
        this.y = y;
        this.cor = cor;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Color getCor(){
        return this.cor;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setCor(Color cor){
        this.cor = cor;
    }

    public void draw(Graphics g){
        g.setColor(this.cor);
        g.drawLine(x, y, x, y);
    }

    public String toString(){
        String valor = String.format("%-5s%-5d%-5d%-5d%-5d%-5d", "p", x, y, cor.getRed(), cor.getGreen(), cor.getBlue());
        return valor;
    }
}
