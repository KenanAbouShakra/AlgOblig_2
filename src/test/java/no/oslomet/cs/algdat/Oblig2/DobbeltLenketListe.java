package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.*;


public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     *
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() { //konstrektør
        hode=null;
        hale=null;
        antall=0;
        endringer=0;
    }

    public DobbeltLenketListe(T[] a) {
       if(a==null) throw new NullPointerException("Tabellen a er null!");
       if(a.length>0){
           int i=0;
           for(;i<a.length;++i){
               if(a[i]!=null) {
                   hode=new Node<>(a[i]);  //for å gi hode den første verdien i listen som ikke er null.
                   antall ++;
                   break;
               }}
               hale=hode;
               if(hode!=null){
                   i++;
                   for(;i<a.length;i++) {
                       if (a[i] != null) {
                           hale.neste = new Node<>(a[i], hale, null); // hale akal flytte fra en verdi til neste verdi i hele listen
                           hale = hale.neste;
                           antall++;
                       }
                   }
           }
       }
    }

    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall,fra,til);
        Liste<T> a=new DobbeltLenketListe<>();
        for(int i=fra; i<til; i++){
            a.leggInn(finnNode(i).verdi);
        }
        return a;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        if(antall==0) return true;
        else return false;
    }
    //hjelpemetoder
    private void fratilKontroll(int antallListe, int fra ,int til){
        if(fra<0 || til > antallListe){
            throw new IndexOutOfBoundsException("feil intervall");
        }
    if (fra > til ){
        throw new IllegalArgumentException("feil intervall");
    }
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi);
        Node<T> current=hale;
        if(tom()){
            hode=hale=new Node<>(verdi);
        }else {
            current.neste=new Node<>(verdi);
            hale=current.neste;
            hale.forrige=current;
        }
        antall ++;
        endringer ++;
        return true;
    }
    private Node<T> finnNode(int indeks) {
        indeksKontroll(indeks);
        Node<T> current=hode;
        if(indeks<2){
            if(indeks==0) return current;
            return current.neste;
        }else{
            current=hale;
            for(int i=antall-1; i>indeks; i--){
                current=current.forrige;
            }return current;
        }
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }
private void indeksKontroll(int indeks){
        if(indeks>=antall || indeks<0) throw new IndexOutOfBoundsException("feil index");
}
    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi);
        indeksKontroll(indeks,false);
        T gammelVerdi=finnNode(indeks).verdi;
        Node<T> current=finnNode(indeks);
         current.verdi=nyverdi;
         endringer ++;
         return gammelVerdi;

    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
       Node<T> current=hode;
       StringBuilder sb=new StringBuilder();
       sb.append("[");

       if(tom()){
           sb.append("]"); //skal printe bare [] i tomme lister
       }else{
           while (current.neste!=null){
               sb.append(current.verdi);
               sb.append(", ");
               current=current.neste;//current skal ta alle verdiene i listen

           }
           sb.append(current.verdi); //siste verdi i listen
           sb.append("]");
       }
        return sb.toString();
    }

    public String omvendtString() {
        Node<T> current=hale;
        StringBuilder sb=new StringBuilder();
        sb.append("[");

        if(tom()){
            sb.append("]"); //skal printe bare [] i tomme lister
        }else{
            while (current.forrige!=null){
                sb.append(current.verdi);
                current=current.forrige; //current skal ta alle verdiene i listen
                sb.append(", ");
            }
            sb.append(current.verdi); // første verdi i listen
            sb.append("]");
        }
        return sb.toString();    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        String[] s = {"Ole", null, "Per", "Kari", null};
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>(s);
        System.out.println(liste.antall() + " " + liste.tom());
        liste .leggInn("Kenan");
        System.out.println(liste.toString());
        DobbeltLenketListe<Integer> liste1 = new DobbeltLenketListe<>();
        System.out.println(liste1.toString() + " " + liste1.omvendtString());
        for (int i = 1; i <= 3; i++) {
            liste1.leggInn(i);
            System.out.println(liste1.toString() + " " + liste1.omvendtString());
        }
        System.out.println(liste.finnNode(3).verdi);
        Character[] c = {'A','B','C','D','E','F','G','H','I','J'};
        DobbeltLenketListe<Character> liste2 = new DobbeltLenketListe<>(c);
        System.out.println(liste2.subliste(3,8));  // [D, E, F, G, H]
        System.out.println(liste2.subliste(5,5));  // []
        System.out.println(liste2.subliste(8,liste2.antall()));
    }


} // class DobbeltLenketListe


