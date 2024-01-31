package clases;/**plantilla**/
//JUAN PABLO ALMEIDA SILVAN

public class ClasePrincipal /**clase**/{
    
    public static void main(String[] args){//instancia
        Proceso1 hilo1 = new Proceso1();
        Proceso2 hilo2 = new Proceso2();   
         hilo1.start();
           hilo2.start();      
    }
}
   //sin lainstancia no se puede lanzar un hilo