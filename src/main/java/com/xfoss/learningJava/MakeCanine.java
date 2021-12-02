abstract class Animal {}

abstract class Canine extends Animal {
    public void roam () {}
}

class Dog extends Canine {}

public class MakeCanine {
    public void go () {
        Canine c;
        c = new Dog ();
        c = new Canine ();
        c.roam();
    }
}
