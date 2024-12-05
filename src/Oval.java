import java.awt.*;
public class Oval extends Ponto
{
    private int raioA, raioB;

    public Oval()
    {
        super();
        setRaioA(0);
        setRaioB(0);
        setCor(Color.black);
    }
    public void setRaioA(int novoRaio) {
        raioA = novoRaio;
    }

    public void setRaioB(int novoRaio) {
        raioB = novoRaio;
    }

    public int getRaioA(){
        return raioA;
    }

    public int getRaioB(){
        return raioB;
    }

    public Oval(int xCentro, int yCentro, int novoRaioA, int novoRaioB, Color novaCor)
    {
        super(xCentro, yCentro, novaCor);
        setRaioA(novoRaioA);
        setRaioB(novoRaioB);
    }

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(getX()-raioA, getY()-raioB, 2*raioA,2*raioB);
    }

    public String toString()
    {
        return transformaString("o",5)+
                transformaString(super.getX(),5)+
                transformaString(super.getY(),5)+

                transformaString(getCor().getRed(),5)+
                transformaString(getCor().getGreen(),5)+
                transformaString(getCor().getBlue(),5)+

                transformaString(getRaioA(), 5)+
                transformaString(getRaioB(), 5);
    }
}
