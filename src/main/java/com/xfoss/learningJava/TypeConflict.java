package com.xfoss.learningJava;

public class TypeConflict {
    public void go () {
            Dog aDog = new Dog ();
            Object sameDog = getObject (aDog);
        }

    public Object getObject (Object o) {
            return o;
        }
}
