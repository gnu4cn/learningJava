package com.xfoss.Annotations;

import java.lang.annotation.*;

@Documented
@interface ClassPreamble {
    String author();
    String date();
    int currentRevision() default 1;
    String lastModified() default "N/A";
    String lastModifiedBy() default "N/A";
    String[] reviewers();
}

@ClassPreamble (
    author = "Lenny Peng",
    date = "5/26/2022",
    lastModified = "5/26/2022",
    reviewers = {"Lenny", "Ryan"}
)
public class Villa extends Building {
    private float price;

    public Villa (String n) {
        super(n);
    }

    public Villa (String n, String a) {
        super(n, a);
    }

    public Villa (String n, float p) {
        super(n);
        price = p;
    }

    public Villa (String n, String a, float p) {
        super(n, a);
        price = p;
    }

    @Override
    public void openFrontDoor () {
        System.out.println("别墅前门打开");
    }

    @Override
    public void openBackDoor () {
        System.out.println("别墅后门打开");
    }

    @Override
    public String toString () {
        return String.format("%s, 价格：%,.2f\n", super.toString(), price);
    }
}
