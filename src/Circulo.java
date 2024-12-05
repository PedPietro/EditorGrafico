import java.awt.*;
public class Circulo extends Ponto{

    private int raio;

    public Circulo()
    {
        super();
        setRaio(0);
        setCor(Color.black);
    }

    public Circulo(int xCentro, int yCentro, int novoRaio, Color novaCor)
    {
        super(xCentro, yCentro, novaCor);
        setRaio(novoRaio);
    }
    public void setRaio(int novoRaio) {
        raio = novoRaio;
    }

    public int getRaio() {
        return raio;
    }

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(getX()-raio, getY()-raio, 2*raio,2*raio);
    }

    public String toString()
    {
        return transformaString("c",5)+
                transformaString(super.getX(),5)+
                transformaString(super.getY(),5)+

                transformaString(getCor().getRed(),5)+
                transformaString(getCor().getGreen(),5)+
                transformaString(getCor().getBlue(),5)+

                transformaString(getRaio(), 5);
    }
}
