package com.xfoss.Annotations;

import java.lang.annotation.*;

public class CustomAnnotationTypeDemo {
    public CustomAnnotationTypeDemo () {
        Villa v1 = new Villa("滨海别墅", "青岛市八大关风景区", 2700000);

        System.out.println(v1);
        v1.openFrontDoor();
        v1.openBackDoor();
    }

    public static void main (String[] args) {
        new CustomAnnotationTypeDemo();
    }
}
