/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabajo_final;

/**
 *
 * @author carlos i Arnau
 */
// https://youtu.be/ClraP3olroU
public class casilla {

    //DECLARACIÓN DE ATRIBUTOS
    private boolean ocupada = false;
    int norte, este, sur, oeste;

    //MÉTODO CONSTRUCTORES
    public casilla(int norte, int este, int sur, int oeste) {
        this.norte = norte;
        this.este = este;
        this.sur = sur;
        this.oeste = oeste;
    }
    
    //setters getters y estado
    
    public void setOcupada() {
        ocupada = true;
    }

    public void setNorte(int x) {
        this.norte = x;
    }

    public void setEste(int y) {
        this.este = y;
    }

    public void setSur(int z) {
        this.sur = z;
    }

    public void setOeste(int w) {
        this.oeste = w;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public int getNorte() {
        return norte;
    }

    public int getEste() {
        return este;
    }

    public int getSur() {
        return sur;
    }

    public int getOeste() {
        return oeste;
    }

    //MÉTODO QUE LIBERA UNA CASILLA
    public void setLiberada() {
        ocupada = false;
    }

    //MÉTODO QUE DEVUELVE EL ESTADO DE UNA CASILLA
    public boolean estado() {
        return ocupada;
    }

    //MÉTODO QUE CAMCIA EL ESTADO DE UNA CASILLA
    public void cambiarEstado() {
        ocupada = !ocupada;
    }

}
