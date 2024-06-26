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
               }
           }
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
        fratilKontroll(antall,fra,til);  //kontrellere at alle verdiene er gjeldige
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
        if(antall==0) return true; // true for tømme lister
        else return false;
    }
    //hjelpemetoder
    private void fratilKontroll(int antallListe, int fra ,int til){
        if(fra<0 || til > antallListe){  //fra og til må være inn i interevalle
            throw new IndexOutOfBoundsException("feil intervall");
        }
    if (fra > til ){
        throw new IllegalArgumentException("feil intervall"); //fra må være større enn til
    }
    }


    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi);
        Node<T> current=hale;
        if(tom()){ //antall er lik null
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
        if(indeks<antall/2){ // hvis indeksen i det første halvet av interevallet begginn å søke fra hode
            for(int i=0; i<indeks; i++){
                current=current.neste;
            }return current;
        }else{
            current=hale; // hvis indeksen i det andre halvet av interevallet begginn å søke fra hale
            for(int i=antall-1; i>indeks; i--){
                current=current.forrige;
            }return current;
        }
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        //sjekk indeksen
        if(indeks > antall) {
            throw new IndexOutOfBoundsException("feil, index må ikke være større enn antall noder");}
        else if(indeks <0){
            throw new IndexOutOfBoundsException("feil, index må ikke være negativ");}
        //sjekk verdien
        if(verdi==null) throw new NullPointerException("verdien må ikke være null");
        Node<T> current=new Node(verdi, null, null);
        if(tom()){
            hode=hale=current;
        }
        else if(indeks==0){ //oppdater hode
            hode.forrige=current;
            current.neste=hode;
            hode=current;
        }else if(indeks==antall){ //oppdater hale
            hale.neste=current;
            current.forrige=hale;
            hale=current;
        }else { //for å legge en ny node mellom to noder
            Node<T> r=finnNode(indeks);
            Node<T> p=r.forrige;
            current.neste=r;
            current.forrige=p;
            p.neste=current;
            r.forrige=current;

        }
        antall ++;
        endringer ++;
    }

    @Override
    public boolean inneholder(T verdi) {
      if(indeksTil(verdi)==-1) return false; //brukte måten indeksTil
      return true;
    }
private void indeksKontroll(int indeks){
        if(indeks>=antall || indeks<0) throw new IndexOutOfBoundsException("feil index"); // for å ta indeksen i et riktig interevalle
}
    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
            Node<T> current=hode;
            for(int i=0;i<antall;i++){
             if(current.verdi.equals(verdi)){
                 return i; //return indeksen
            }current=current.neste;
        } return -1; //hvis verdien finnes ikke
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi);
        indeksKontroll(indeks);
        T gammelVerdi=finnNode(indeks).verdi;
        Node<T> current=finnNode(indeks);
         current.verdi=nyverdi; //oppdater verdien
         endringer ++;
         return gammelVerdi;

    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) {
            return false;
        }
        Node<T> current = hode;
        if (verdi.equals(hode.verdi)) {
            if (antall>1) {
                hode = hode.neste; //fjern første verdi
                hode.forrige = null;
            } else if(antall==1) {
                hode = hale = null;
            } antall--;
            endringer++;
            return true;
        }
        else if (verdi.equals(hale.verdi)) {
            hale = hale.forrige; //fjern siste verdi
            hale.neste = null;
            antall--;
            endringer++;
            return true;
        } else {
            for (int i = 1; i < antall - 1; i++) { //fjern en node mellom to noder
                current = current.neste;
                if (verdi.equals(current.verdi)) {
                    current.forrige.neste = current.neste;
                    current.neste.forrige = current.forrige;
                    antall--;
                    endringer++;
                    return true;
                }
            }
        }return false;
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks);
        Node<T> current=hode;
        T verdi ;
        if(indeks==0){  //Hode fjernes hvis indeksen=0
            if(current.neste!=null){
                hode=current.neste;
                hode.forrige=null;

            }else {
                hode=hale=null;
            } verdi=current.verdi;

        }else if(indeks==antall-1){//Hale fjernes hvis indeksen=Antall-1
            current=hale;
            hale=current.forrige;
            hale.neste=null;
            verdi=current.verdi;
        }else { //mellomNoder fjernes
            for(int i=0; i<indeks;i++){
                current=current.neste;
            }
            current.forrige.neste=current.neste;
            current.neste.forrige=current.forrige;
           verdi=current.verdi;
        }
        antall--;
        endringer++;
        return verdi;
    }

    @Override
    public void nullstill() {
        Node<T> t=hode;
       for(int i=1; i<antall-1;i++){ //alle nodene mellom hode og hale fjernes
           t=t.neste;
           t.forrige=null;
       }
       hode=hale=null; //hode og hale fjernes
       antall=0;
       endringer++;
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
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks);
        return new DobbeltLenketListeIterator(indeks);
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
           denne=finnNode(indeks);
           fjernOK=false;
           iteratorendringer=endringer;
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            if(iteratorendringer!=endringer){
                throw new ConcurrentModificationException("iterattorendringene er ikke lik endringer");
            }
            if(!hasNext()){
                throw new NoSuchElementException("tømme verdier");
            }
            T currrentVerdi=denne.verdi;
            denne=denne.neste;
            fjernOK=true;
            return currrentVerdi;

        }

        @Override
        public void remove() {
            if(!fjernOK) throw new IllegalStateException("metoden kan ikke fjerner noen elementer");
            if(endringer!=iteratorendringer){
                throw new ConcurrentModificationException(" endringer og iteratorendringer er forskjellige");
            }
            fjernOK=false;
            Node<T> p;
            if(denne==null){
                p=hale;
            }else {
                p=denne.forrige;
            }

            if (p==hode){ // p er venstre peker til denne
                if(antall==1){
                    hode=hale=null; // hvis er det bare en node
                }else {
                    hode=hode.neste; // fjern hode
                    hode.forrige=null;
                }
            }else if(p==hale){
                hale=hale.forrige; //fjern hale
                hale.neste=null;
            }else {
                p.forrige.neste=p.neste; // fjern p
                p.neste.forrige=p.forrige;
            }
            antall--;
            endringer++;
            iteratorendringer++; // endre antall, nedringer og iteratorendringer
        }

    } // class DobbeltLenketListeIterator
    public static <T> void bytt(Liste<T> liste, int indeks1, int indeks2) {
        T element1 = liste.hent(indeks1);
        T element2 = liste.hent(indeks2);

        liste.oppdater(indeks1, element2);
        liste.oppdater(indeks2, element1);
    }


    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        int n = liste.antall();

        for (int i = 1; i < n; i++) {
            T element = liste.hent(i);
            int j = i;

            while (j > 0 && c.compare(element, liste.hent(j - 1)) < 0) {
                bytt(liste, j, j - 1);
                j--;
            }

            liste.oppdater(j, element);
        }
    }




   /* public static void main(String[] args) {
        String[] s = {"Ole", null, "Per", "Kari", null};
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>(s);
        liste.leggInn(2,"Aos");
        System.out.println(liste.antall() + " " + liste.tom());
        liste .leggInn("Kenan");
        System.out.println(liste.toString());
        liste.fjern(4);
        System.out.println(liste.toString()+" " +liste.fjern(2));
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
        System.out.println(liste.indeksTil("tt"));

    }*/

} // class DobbeltLenketListe


